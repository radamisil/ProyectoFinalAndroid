package com.visight.adondevamos.ui.main.place

import android.content.Intent
import com.visight.adondevamos.data.entity.PlaceAverageAvailability
import com.visight.adondevamos.data.entity.Promotion
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.ui.base.BaseContract

interface PlaceDetailActivityContract {
    interface View: BaseContract.View {
        fun displayMessage(message: String)
        fun displayPromotions(promotions: List<Promotion>)
        fun setAvailabilityGraphic(availabilityList: List<PlaceAverageAvailability>)
    }

    interface Presenter: BaseContract.Presenter<PlaceDetailActivityContract.View> {
        fun getPromotions(placeId: String)
        fun getPlaceAvailability(placeId: String)
    }
}