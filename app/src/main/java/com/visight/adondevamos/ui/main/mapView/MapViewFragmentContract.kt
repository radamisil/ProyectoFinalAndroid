package com.visight.adondevamos.ui.main.mapView

import com.google.android.gms.maps.model.LatLng
import com.visight.adondevamos.data.entity.Promotion
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.data.remote.responses.PollAverageResponse
import com.visight.adondevamos.ui.base.BaseContract
import com.visight.adondevamos.ui.main.mapView.mapUtils.MapItem
import com.visight.adondevamos.ui.main.dialogs.PlacePreviewDialog

interface MapViewFragmentContract {
    interface View: BaseContract.View, PlacePreviewDialog.OnClickPreviewPlaceDialog {
        fun displayPlaces(placesList: List<MapItem>)
        fun displayCustomPlaces(placesList: List<MapItem>)
        fun displayMessage(message: String)
        fun displayPlacePreviewDialog(publicPlace: PublicPlace, pollAverageResponse: PollAverageResponse,
                                      promotions: List<Promotion>)
    }

    interface Presenter: BaseContract.Presenter<MapViewFragmentContract.View> {
        fun getPublicPlaces(location: LatLng, type: String?)
        fun getCustomPublicPlaces(type: String?)
        fun getSpecificPublicPlace(placeId: String)
        fun getAllPublicPlacesList() : MutableList<PublicPlace>?
        fun getPublicPlaceCurrentAvailability(publicPlace: PublicPlace)
        fun getPromotions(publicPlace: PublicPlace, pollAverageResponse: PollAverageResponse)
    }
}