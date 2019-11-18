package com.visight.adondevamos.ui.main.place.report

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.setPadding
import com.tbruyelle.rxpermissions2.RxPermissions
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.ui.main.dialogs.PlaceCapacityDialog
import com.visight.adondevamos.utils.*
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.activity_report_place_attendance.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.io.File

class ReportFromPlaceActivity : BaseActivity(), ReportFromPlaceActivityContract.View {
    var mPresenter: ReportFromPlaceActivityContract.Presenter? = null
    var mPermissionsDisposable: Disposable? = null
    var mPublicPlace: PublicPlace? = null
    var sendCapacityDialog: PlaceCapacityDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_place_attendance)
        setUpToolbar(toolbar, getString(R.string.text_enviar_mi_reporte), ivLogo)
        startPresenter()

        mPublicPlace = intent.getParcelableExtra(AppConstants.PUBLIC_PLACE)
        sendCapacityDialog = PlaceCapacityDialog()
        sendCapacityDialog!!.onClickSendPlaceCapacity = this

        fabTakePhoto.setOnClickListener {
            requestPermissionAndStartCameraActivity()
        }

        btnSendPhoto.setOnClickListener {
            progressBarReport.visibility = View.VISIBLE
            mPresenter!!.sendReport(mPublicPlace!!.placeId!!, false)
        }

        llOptionLow.setOnClickListener {
            mPresenter!!.setSurveySelectedOption(Availability.LOW.value)
        }

        llOptionMedium.setOnClickListener {
            mPresenter!!.setSurveySelectedOption(Availability.MEDIUM.value)
        }

        llOptionHigh.setOnClickListener {
            mPresenter!!.setSurveySelectedOption(Availability.HIGH.value)
        }

        btnSendReport.setOnClickListener {
            /*if(mPresenter!!.getSurveySelectedOption() != null){
                progressBarReport.visibility = View.VISIBLE
                mPresenter!!.sendReport(mPublicPlace!!.placeId!!, true)
            }*/
            sendCapacityDialog!!.show(supportFragmentManager, "capacityDialog")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun requestPermissionAndStartCameraActivity() {
        var rxPermissions = RxPermissions(this)
        mPermissionsDisposable = rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe { granted ->
                    if (granted) { // Always true pre-M
                        mPresenter!!.takePhoto()
                    } else {
                        rxPermissions.request(Manifest.permission.CAMERA)
                    }
                }
    }

    override fun displayMessage(message: String) {
        DisplayMessage().displayMessage(message, llContainer)
    }

    override fun takePhotoIntent(intent: Intent) {
        startActivityForResult(intent, AppConstants.REQUEST_IMAGE_CAPTURE)
    }

    override fun displayImage(photoPath: String) {
        ivPlaceImage.setPadding(0)
        GlideApp.with(getContext())
                .load(File(photoPath))
                .into(ivPlaceImage)
    }

    override fun onResponseSendPhoto(peopleNumber: Int) {
        var peopleMessage: String
        when {
            peopleNumber == 1 -> peopleMessage = getString(R.string.text_se_encontro_un_persona)
            peopleNumber > 1 -> peopleMessage = getString(R.string.text_se_encontraron_personas, peopleNumber)
            else -> peopleMessage = getString(R.string.text_no_se_encontraron_personas)
        }

        //displayMessage(peopleMessage)
        showMessage(peopleMessage, llContainer)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AppConstants.REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                mPresenter!!.takePhotoResult()
            }
        }
    }

    override fun onResponseReport(message: String?) {
        progressBarReport.visibility = View.GONE
        if(message == null){
            finish()
        }else{
            displayMessage(message)
        }
    }

    //TODO send capacity and form
    override fun onClickSendPlaceCapacity() {
        sendCapacityDialog!!.dismiss()
    }

    override fun startPresenter() {
        mPresenter = ReportFromPlaceActivityPresenter()
        mPresenter!!.startView(this)
    }


    override fun getContext(): Context {
        return this
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPermissionsDisposable != null && !mPermissionsDisposable!!.isDisposed) {
            mPermissionsDisposable!!.dispose()
        }
    }
}