package com.visight.adondevamos.ui.main.mapView

import com.google.android.gms.maps.model.LatLng
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.ui.base.BaseContract
import com.visight.adondevamos.ui.main.mapView.mapUtils.MapItem
import com.visight.adondevamos.utils.PlacePreviewDialog

interface MapViewFragmentContract {
    interface View: BaseContract.View, PlacePreviewDialog.OnClickPreviewPlaceDialog {
        fun displayPlaces(placesList: List<MapItem>)
        fun displayMessage(message: String)
    }

    interface Presenter: BaseContract.Presenter<MapViewFragmentContract.View> {
        fun getPublicPlaces(location: LatLng, type: String?)
        fun getSpecificPublicPlace(placeId: String)
        fun getAllPublicPlacesList() : MutableList<PublicPlace>?
    }
}