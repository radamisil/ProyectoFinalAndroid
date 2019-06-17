package com.visight.adondevamos.ui.main.mapView

class MapViewFragmentPresenter: MapViewFragmentContract.Presenter {
    var mView: MapViewFragmentContract.View? = null

    override fun startView(view: MapViewFragmentContract.View) {
        mView = view
    }

    override fun onDestroy() {
        mView = null
    }

    override fun getPublicPlaces(category: String) {
        
    }

}