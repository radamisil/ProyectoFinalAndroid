package com.visight.adondevamos.ui.main.mapView

import com.google.android.gms.maps.model.LatLng
import com.visight.adondevamos.data.remote.GooglePlacesService
import com.visight.adondevamos.data.remote.responses.GetPublicPlacesResponse
import com.visight.adondevamos.ui.main.mapView.mapUtils.MapItem
import com.visight.adondevamos.utils.AppConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MapViewFragmentPresenter : MapViewFragmentContract.Presenter {
    var mView: MapViewFragmentContract.View? = null
    var mCompositeDisposable: CompositeDisposable? = null
    var disposable: Disposable? = null
    var disposablePhoto: Disposable? = null

    override fun startView(view: MapViewFragmentContract.View) {
        mView = view
    }

    override fun onDestroy() {
        mView = null
        if(disposable != null){
            disposable!!.dispose()
        }
    }

    override fun getPublicPlaces(location: LatLng, type: String) {
        val locationToSend: String = location.latitude.toString() + "," + location.longitude.toString()

        disposable = GooglePlacesService().getClient().getPublicPlaces(
            locationToSend, AppConstants.searchRadius, type,
            "AIzaSyBMfHFvJTPHMgD5zBbRbuJdOjIOJ_HdL4o"
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map { t: GetPublicPlacesResponse ->
                t.results
            }
            .toObservable()  //TODO PREGUNTAR SI ESTO ERA NECESARIO
            .flatMapIterable { l -> l }
            .map { MapItem(it) }
            .toList()
            .subscribe { t1: List<MapItem>?, t2: Throwable? ->
                run {
                    if (t1 != null) {
                        mView!!.displayPlaces(t1)
                    }else{
                        t2!!.message?.let { mView!!.displayMessage(it) }
                    }
                }
            }
    }

    override fun getSpecificPublicPlace(placeId: String) {
        disposable = GooglePlacesService().getClient().getSpecificPublicPlace(
            placeId, "AIzaSyBMfHFvJTPHMgD5zBbRbuJdOjIOJ_HdL4o")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map { t: GetPublicPlacesResponse ->
                t.results
            }
            .toObservable()  //TODO PREGUNTAR SI ESTO ERA NECESARIO
            .flatMapIterable { l -> l }
            .map { MapItem(it) }
            .toList()
            .subscribe { t1: List<MapItem>?, t2: Throwable? ->
                run {
                    if (t1 != null) {
                        mView!!.displayPlaces(t1)
                    }else{
                        t2!!.message?.let { mView!!.displayMessage(it) }
                    }
                }
            }
    }

}