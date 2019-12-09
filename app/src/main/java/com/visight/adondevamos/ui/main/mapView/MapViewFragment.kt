package com.visight.adondevamos.ui.main.mapView

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.clustering.ClusterManager
import com.visight.adondevamos.ui.main.mapView.mapUtils.MapItem
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.visight.adondevamos.ui.main.mapView.mapUtils.MyMapClusterRenderer
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.data.remote.responses.PollAverageResponse
import com.visight.adondevamos.ui.main.user.MainActivity
import com.visight.adondevamos.utils.AppConstants
import com.visight.adondevamos.utils.DisplayMessage
import com.visight.adondevamos.ui.main.dialogs.PlacePreviewDialog
import com.visight.adondevamos.utils.showMessage
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.layout_content_main.*

class MapViewFragment : Fragment(), OnMapReadyCallback, MapViewFragmentContract.View {
    private var mPresenter: MapViewFragmentContract.Presenter? = null
    private lateinit var mMap: GoogleMap
    private lateinit var mClusterManager: ClusterManager<MapItem>
    private var mCurrentLocation: LatLng? = null
    private var mCurrentPlace: PublicPlace? = null
    private var mLocationDisposable: Disposable? = null
    private var mPlacePreviewDialog: PlacePreviewDialog? = null

    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_map, container, false)
        startPresenter()

        if(arguments != null){
            mCurrentLocation = arguments!!.getParcelable(AppConstants.CURRENT_LOCATION_KEY)
            mCurrentPlace = arguments!!.getParcelable(AppConstants.CURRENT_PLACE_KEY)
        }else {
            checkPermissions()
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        activity!!.fabMyLocation.visibility = View.VISIBLE
        activity!!.fabListMode.setImageResource(R.drawable.ic_list)

        return view
    }

    //TODO Permissions - change this to some other method?
    @SuppressLint("MissingPermission")
    private fun checkPermissions() {
        var rxPermissions = RxPermissions(activity as Activity)
        var rxLocation = RxLocation(activity as Activity)
        mLocationDisposable = rxPermissions
            .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe { granted ->
                if (granted) { // Always true pre-M
                    //TODO PREGUNTAR COMO HACER PARA QUE ME RECONOZCA EL PERMISO ANTERIOR SIN USAR EL WARNING
                    rxLocation.location().lastLocation().subscribe {
                        mCurrentLocation = LatLng(it.latitude, it.longitude)
                    }
                } else {
                    rxPermissions.request(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        if(mCurrentLocation != null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, AppConstants.mapZoomIn))
        }
        setUpClusterer()
    }

    private fun setUpClusterer() {
        mClusterManager = ClusterManager(activity, mMap)
        val mClusterRenderer = MyMapClusterRenderer(activity, mMap, mClusterManager);
        mClusterManager.renderer = mClusterRenderer

        mMap.setOnCameraIdleListener(mClusterManager)
        mMap.setOnMarkerClickListener(mClusterManager)

        mClusterManager.setOnClusterClickListener {
            val builder = LatLngBounds.builder()
            for (item in it.items) {
                builder.include(item.position)
            }
            val bounds = builder.build()
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            true
        }

        mClusterManager.setOnClusterItemClickListener {
            mPresenter!!.getPublicPlaceCurrentAvailability(it.publicPlace!!)
            true
            /*mPlacePreviewDialog = PlacePreviewDialog()
            mPlacePreviewDialog!!.onClickPreviewPlaceDialog = this

            var bundle = Bundle()
            bundle.putParcelable(AppConstants.PUBLIC_PLACE, it.publicPlace)
            mPlacePreviewDialog!!.arguments = bundle

            mPlacePreviewDialog!!.show(childFragmentManager, AppConstants.PLACE_PREVIEW_DIALOG)
            true*/
        }

        //from Autocomplete
        if(mCurrentPlace != null){
            mClusterManager.clearItems()
            var mapItem = MapItem(mCurrentPlace!!)
            mClusterManager.addItem(mapItem)

            var placePosition = LatLng(mCurrentPlace!!.geometry!!.location!!.lat, mCurrentPlace!!.geometry!!.location!!.lng)
            mMap.addMarker(MarkerOptions()
                    .position(placePosition)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_marker_available)))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placePosition, AppConstants.mapZoomInSpecificPlace))
        }else{
            //First time
            mPresenter!!.getPublicPlaces(mCurrentLocation!!, null)
        }
    }

    fun callMethodGetPublicPlaces(type: String){
        mPresenter!!.getPublicPlaces(mCurrentLocation!!, type)
    }

    fun setCameraToCurrentPosition(newPosition: LatLng){
        mCurrentLocation = newPosition
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, AppConstants.mapZoomIn))
    }

    //TODO check implementation
    override fun getContext(): Context {
        return activity!!.baseContext
    }

    override fun startPresenter() {
        mPresenter = MapViewFragmentPresenter()
        mPresenter!!.startView(this)
    }

    override fun displayMessage(message: String) {
        DisplayMessage().displayMessage(message, flMapContainer)
    }

    override fun displayPlaces(placesList: List<MapItem>) {
        mClusterManager.clearItems()
        if(placesList.isEmpty()){
            //displayMessage("No se encontraron lugares cercanos, prueba otra ubicación")
            activity!!.showMessage("No se encontraron lugares cercanos, por favor prueba otra ubicación", flMapContainer)
        }else{
            for(mapItem: MapItem in placesList){
                mClusterManager.addItem(mapItem)
            }
        }
        mClusterManager.cluster()
    }

    override fun onClickPlaceSeeMore(publicPlace: PublicPlace, pollAverageResponse: PollAverageResponse) {
        (activity as MainActivity).redirectToPlaceDetailActivity(publicPlace, pollAverageResponse)
    }

    override fun displayPlacePreviewDialog(publicPlace: PublicPlace, pollAverageResponse: PollAverageResponse) {
        mPlacePreviewDialog = PlacePreviewDialog()
        mPlacePreviewDialog!!.onClickPreviewPlaceDialog = this

        var bundle = Bundle()
        bundle.putParcelable(AppConstants.PUBLIC_PLACE, publicPlace)
        bundle.putParcelable(AppConstants.PUBLIC_PLACE_CURRENT_AVAILABILITY, pollAverageResponse)
        mPlacePreviewDialog!!.arguments = bundle

        mPlacePreviewDialog!!.show(childFragmentManager, AppConstants.PLACE_PREVIEW_DIALOG)
    }

    fun getPlacesList() : MutableList<PublicPlace>? {
        return mPresenter!!.getAllPublicPlacesList()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mLocationDisposable != null){
            mLocationDisposable!!.dispose()
        }
    }
}