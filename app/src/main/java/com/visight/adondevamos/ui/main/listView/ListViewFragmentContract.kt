package com.visight.adondevamos.ui.main.listView

import com.google.android.gms.maps.model.LatLng
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.ui.base.BaseContract

interface ListViewFragmentContract {
    interface View: BaseContract.View {
        fun displayPlaces(placesList: List<PublicPlace>)
        fun displayMessage(message: String)
    }

    interface Presenter: BaseContract.Presenter<ListViewFragmentContract.View> {
        fun setPublicPlacesList(list: List<PublicPlace>)
        fun getStoredPlacesList() : List<PublicPlace>
        fun getPublicPlaces(location: LatLng, type: String?)
    }
}