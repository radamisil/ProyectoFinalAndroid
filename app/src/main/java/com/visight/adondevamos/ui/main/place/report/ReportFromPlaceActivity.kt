package com.visight.adondevamos.ui.main.place.report

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tbruyelle.rxpermissions2.RxPermissions
import com.visight.adondevamos.R
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.utils.AppConstants
import com.visight.adondevamos.utils.GlideApp
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_report_place_attendance.*
import java.io.File

class ReportFromPlaceActivity : BaseActivity(), ReportFromPlaceActivityContract.View {
    var mPresenter: ReportFromPlaceActivityContract.Presenter? = null
    var mPermissionsDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_place_attendance)
        startPresenter()

        tvAvailability.setOnClickListener {
            requestPermissionAndStartCameraActivity()
        }

        btnGoBack.setOnClickListener {
            mPresenter!!.sendReport()
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

    }

    override fun takePhotoIntent(intent: Intent) {
        startActivityForResult(intent, AppConstants.REQUEST_IMAGE_CAPTURE)
    }

    override fun displayImage(photoPath: String) {
        GlideApp.with(getContext())
                .load(File(photoPath))
                .circleCrop()
                .into(ivPlaceImage)
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