package com.visight.adondevamos.ui.main.listView

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import com.visight.adondevamos.R
import com.visight.adondevamos.adapters.PlacesListAdapter
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.utils.AppConstants
import com.visight.adondevamos.utils.DisplayMessage
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.layout_content_main.*

class ListViewFragment : Fragment(), ListViewFragmentContract.View {
    private var mPlacesListAdapter: PlacesListAdapter? = null
    private var mCurrentLocation: LatLng? = null
    private var mLocationDisposable: Disposable? = null
    private var mPresenter: ListViewFragmentContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        startPresenter()
        if(arguments != null){
            mCurrentLocation = arguments!!.getParcelable(AppConstants.CURRENT_LOCATION_KEY)
            if(arguments!!.getParcelableArrayList<PublicPlace>(AppConstants.PLACES_LIST_KEY) != null){
                mPresenter!!.setPublicPlacesList(arguments!!.getParcelableArrayList<PublicPlace>(AppConstants.PLACES_LIST_KEY)
                        as List<PublicPlace>)
            }
        }else {
            checkPermissions()
        }

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPlacesList(mPresenter!!.getStoredPlacesList())
        activity!!.fabMyLocation.visibility = View.GONE
        activity!!.fabListMode.setImageResource(R.drawable.ic_map)
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

    override fun getContext(): Context {
        return activity!!.baseContext
    }

    override fun displayPlaces(placesList: List<PublicPlace>) {

    }

    override fun displayMessage(message: String) {

    }

    override fun startPresenter() {
        mPresenter = ListViewFragmentPresenter()
        mPresenter!!.startView(this)
    }

    private fun setPlacesList(list: List<PublicPlace>) {
        mPlacesListAdapter = PlacesListAdapter(list)
        rvPlaces.adapter = mPlacesListAdapter
        rvPlaces.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }
}