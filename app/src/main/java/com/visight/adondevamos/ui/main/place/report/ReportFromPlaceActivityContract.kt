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
        fun onResponseReport(message: String?, IAvalue: Int? = 0)
    }

    interface Presenter: BaseContract.Presenter<ReportFromPlaceActivityContract.View> {
        fun takePhoto()
        fun takePhotoResult()
        fun sendReport(placeId: String, shouldSendSurveySelectedOption: Boolean, capacity: Int)
        fun setSurveySelectedOption(selectedOption: String)
        fun getSurveySelectedOption(): String?
    }
}