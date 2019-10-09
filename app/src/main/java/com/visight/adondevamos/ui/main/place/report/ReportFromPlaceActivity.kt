package com.visight.adondevamos.ui.main.place.report

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tbruyelle.rxpermissions2.RxPermissions
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.utils.AppConstants
import com.visight.adondevamos.utils.DisplayMessage
import com.visight.adondevamos.utils.GlideApp
import com.visight.adondevamos.utils.showMessage
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.activity_report_place_attendance.*
import java.io.File

class ReportFromPlaceActivity : BaseActivity(), ReportFromPlaceActivityContract.View {
    var mPresenter: ReportFromPlaceActivityContract.Presenter? = null
    var mPermissionsDisposable: Disposable? = null
    var mPublicPlace: PublicPlace? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_place_attendance)
        startPresenter()



        mPublicPlace = intent.getParcelableExtra(AppConstants.PUBLIC_PLACE)

        fabTakePhoto.setOnClickListener {
            requestPermissionAndStartCameraActivity()
        }

        btnSendReport.setOnClickListener {
            mPresenter!!.sendReport(mPublicPlace!!.placeId!!)
        }
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
        GlideApp.with(getContext())
                .load(File(photoPath))
                .into(ivPlaceImage)
    }

    override fun onResponseSendPhoto(peopleNumber: Int) {
        var peopleMessage = "Se encontraron " + peopleNumber.toString() + " personas."
        if(peopleNumber == 1){
            peopleMessage = "Se encontró una persona."
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