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
import com.google.android.libraries.places.api.model.Place
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.utils.AppConstants
import com.visight.adondevamos.utils.PlacePreviewDialog
import io.reactivex.disposables.Disposable

class MapViewFragment : Fragment(), OnMapReadyCallback, MapViewFragmentContract.View {
    private var mPresenter: MapViewFragmentContract.Presenter? = null
    private lateinit var mMap: GoogleMap
    private lateinit var mClusterManager: ClusterManager<MapItem>
    private var mCurrentLocation: LatLng? = null
    private var mCurrentPlace: Place? = null
    private var mLocationDisposable: Disposable? = null
    private var mPlacePreviewDialog: PlacePreviewDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(com.visight.adondevamos.R.layout.fragment_map, container, false)
        startPresenter()

        if(arguments != null){
            mCurrentLocation = arguments!!.getParcelable(AppConstants.CURRENT_LOCATION_KEY)
            mCurrentPlace = arguments!!.getParcelable(AppConstants.CURRENT_PLACE_KEY)
        }else {
            checkPermissions()
        }

        val mapFragment = childFragmentManager.findFragmentById(com.visight.adondevamos.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
            //TODO implement function for marker click event
            mPlacePreviewDialog = PlacePreviewDialog()
            var bundle = Bundle()
            bundle.putParcelable(AppConstants.PUBLIC_PLACE, it.publicPlace)
            mPlacePreviewDialog!!.arguments = bundle
            mPlacePreviewDialog!!.show(childFragmentManager, AppConstants.PLACE_PREVIEW_DIALOG)
            true
        }

        if(mCurrentPlace != null){
            mPresenter!!.getSpecificPublicPlace(mCurrentPlace!!.id!!)
        }else{
            // Add cluster items (markers) to the cluster manager.
            mPresenter!!.getPublicPlaces(mCurrentLocation!!, "store")
        }
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

    override fun displayPlaces(placesList: List<MapItem>) {
        mClusterManager.clearItems()
        for(mapItem: MapItem in placesList){
            if(placesList.size == 1){
                mMap.addMarker(MarkerOptions()
                    .position(mapItem.publicPlacePosition!!)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_marker_available)))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapItem.publicPlacePosition, 1000f))
                break
            }
            mClusterManager.addItem(mapItem)
            mClusterManager.cluster()
        }
    }

    override fun displayMessage(message: String) {

    }

    override fun onDestroy() {
        super.onDestroy()
        if(mLocationDisposable != null){
            mLocationDisposable!!.dispose()
        }
    }
}