package com.visight.adondevamos.ui.main.place

import android.content.Intent
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.ui.base.BaseContract

interface PlaceDetailActivityContract {
    interface View: BaseContract.View {
        fun displayMessage(message: String)
    }

    interface Presenter: BaseContract.Presenter<PlaceDetailActivityContract.View> {

    }
}