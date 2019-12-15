package com.visight.adondevamos.ui.main.place

import androidx.annotation.NonNull
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.data.remote.AppServices
import com.visight.adondevamos.data.remote.GooglePlacesService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PlaceDetailActivityPresenter : PlaceDetailActivityContract.Presenter{
    var mView: PlaceDetailActivityContract.View? = null
    var mDisposable: Disposable? = null

    override fun startView(@NonNull view: PlaceDetailActivityContract.View) {
        mView = view
    }

    override fun onDestroy() {
        mView = null
    }

    //TODO PROMOTIONS - add API
    override fun getPromotions(placeId: String) {
        /*disposable = AppServices().getClient().getPromotions(placeId!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({ promotions ->
                mView!!.displayPromotions(promotions)
            }, {
                it.message?.let { mView!!.displayMessage("No se pudieron obtener las promociones del lugar, por favor inténtalo nuevamente") }
            })*/
        mView!!.displayPromotions(listOf())
    }

    //TODO AVAILABILITY GLOBAL - add API
    override fun getPlaceGlobalAvailability(placeId: String) {
        mDisposable = AppServices().getClient().getPlaceGlobalAverageAvailability(googlePlaceId = placeId!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({ placeAverageAvailabilityList ->
                mView!!.setAvailabilityGraphic(placeAverageAvailabilityList)
            }, {
                it.message?.let { mView!!.displayMessage("No se pudo obtener información del lugar, por favor inténtalo nuevamente") }
            })
        //mView!!.setAvailabilityGraphic(listOf())
    }

    //TODO AVAILABILITY GLOBAL - add API
    override fun getPlaceAvailability(placeId: String) {
        mDisposable = AppServices().getClient().getPlaceAverageAvailability(googlePlaceId = placeId!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({ pollAverageResponseData ->
                mView!!.setAvailability(pollAverageResponseData.Data!![0])
            }, {
                it.message?.let { mView!!.displayMessage("No se pudo obtener información del lugar, por favor inténtalo nuevamente") }
            })
        //mView!!.setAvailabilityGraphic(listOf())
    }
}