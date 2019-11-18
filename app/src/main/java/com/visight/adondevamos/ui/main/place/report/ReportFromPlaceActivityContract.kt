package com.visight.adondevamos.ui.main.place.report

import android.content.Intent
import com.visight.adondevamos.ui.base.BaseContract
import com.visight.adondevamos.ui.main.dialogs.PlaceCapacityDialog

interface ReportFromPlaceActivityContract {
    interface View: BaseContract.View, PlaceCapacityDialog.OnClickSendPlaceCapacity {
        fun displayMessage(message: String)
        fun takePhotoIntent(intent: Intent)
        fun displayImage(photoPath: String)
        fun onResponseSendPhoto(peopleNumber: Int)
        fun onResponseReport(message: String?)
    }

    interface Presenter: BaseContract.Presenter<ReportFromPlaceActivityContract.View> {
        fun takePhoto()
        fun takePhotoResult()
        fun sendReport(placeId: String, shouldSendSurveySelectedOption: Boolean)
        fun setSurveySelectedOption(selectedOption: Int)
        fun getSurveySelectedOption(): Int?
    }
}