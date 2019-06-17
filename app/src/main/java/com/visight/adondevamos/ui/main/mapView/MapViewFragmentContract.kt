package com.visight.adondevamos.ui.main.mapView

import com.visight.adondevamos.ui.base.BaseContract
import com.visight.adondevamos.ui.main.mapView.mapUtils.MapItem

interface MapViewFragmentContract {
    interface View: BaseContract.View {
        fun displayPlaces(placesList: List<MapItem>)
    }

    interface Presenter: BaseContract.Presenter<MapViewFragmentContract.View> {
        fun getPublicPlaces(category: String)
    }

}