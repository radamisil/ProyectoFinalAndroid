package com.visight.adondevamos.ui.main.place.report

import android.content.Intent
import com.visight.adondevamos.ui.base.BaseContract

interface ReportFromPlaceActivityContract {
    interface View: BaseContract.View {
        fun displayMessage(message: String)
        fun takePhotoIntent(intent: Intent)
        fun displayImage(photoPath: String)
    }

    interface Presenter: BaseContract.Presenter<ReportFromPlaceActivityContract.View> {
        fun takePhoto()
        fun takePhotoResult()
        fun sendReport()
    }
}