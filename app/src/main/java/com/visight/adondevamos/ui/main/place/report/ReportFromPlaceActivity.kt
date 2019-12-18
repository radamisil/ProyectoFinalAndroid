package com.visight.adondevamos.ui.main.place.report

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.setPadding
import com.tbruyelle.rxpermissions2.RxPermissions
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.ui.main.dialogs.PlaceCapacityDialog
import com.visight.adondevamos.ui.main.place.PlaceDetailActivity
import com.visight.adondevamos.ui.main.user.MainActivity
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
    var mReportFromDialog: Boolean? = false
    var sendCapacityDialog: PlaceCapacityDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_place_attendance)
        setUpToolbar(toolbar, getString(R.string.text_enviar_mi_reporte), ivLogo)
        startPresenter()

        mPublicPlace = intent.getParcelableExtra(AppConstants.PUBLIC_PLACE)
        mReportFromDialog = intent.getBooleanExtra(AppConstants.REPORT_FROM_DIALOG, false)
        sendCapacityDialog = PlaceCapacityDialog()
        sendCapacityDialog!!.onClickSendPlaceCapacity = this

        fabTakePhoto.setOnClickListener {
            requestPermissionAndStartCameraActivity()
        }

        fabChoosePhoto.setOnClickListener {
            requestPermissionAndStartGalleryActivity()
        }

        /*btnSendPhoto.setOnClickListener {
            progressBarReport.visibility = View.VISIBLE
            mPresenter!!.sendReport(mPublicPlace!!.placeId!!, false)
        }*/

        llOptionLow.setOnClickListener {
            setSelectedOptionIndicators(Availability.LOW.name)
            mPresenter!!.setSurveySelectedOption(Availability.LOW.name)
        }

        llOptionMedium.setOnClickListener {
            setSelectedOptionIndicators(Availability.MEDIUM.name)
            mPresenter!!.setSurveySelectedOption(Availability.MEDIUM.name)
        }

        llOptionHigh.setOnClickListener {
            setSelectedOptionIndicators(Availability.HIGH.name)
            mPresenter!!.setSurveySelectedOption(Availability.HIGH.name)
        }

        //TODO edit Send Report
        btnSendReport.setOnClickListener {
            /*if(mPresenter!!.getSurveySelectedOption() != null){
                progressBarReport.visibility = View.VISIBLE
                mPresenter!!.sendReport(mPublicPlace!!.placeId!!, true)
            }*/
            //sendCapacityDialog!!.show(supportFragmentManager, "capacityDialog")
            if(mPresenter!!.getSurveySelectedOption() != null){
                sendCapacityDialog!!.show(supportFragmentManager, "capacityDialog")
            }
        }
    }

    private fun setSelectedOptionIndicators(name: String) {
        when (name) {
            Availability.LOW.name -> {
                ivSelectedLow.visibility = View.VISIBLE
                ivSelectedMedium.visibility = View.GONE
                ivSelectedHigh.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tvOptionLow.setTextColor(resources.getColor(R.color.colorGreen, null))
                    tvOptionMedium.setTextColor(resources.getColor(R.color.colorGrey, null))
                    tvOptionHigh.setTextColor(resources.getColor(R.color.colorGrey, null))
                }
            }
            Availability.MEDIUM.name -> {
                ivSelectedLow.visibility = View.GONE
                ivSelectedMedium.visibility = View.VISIBLE
                ivSelectedHigh.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tvOptionLow.setTextColor(resources.getColor(R.color.colorGrey, null))
                    tvOptionMedium.setTextColor(resources.getColor(R.color.colorAccent, null))
                    tvOptionHigh.setTextColor(resources.getColor(R.color.colorGrey, null))
                }
            }
            Availability.HIGH.name -> {
                ivSelectedLow.visibility = View.GONE
                ivSelectedMedium.visibility = View.GONE
                ivSelectedHigh.visibility = View.VISIBLE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tvOptionLow.setTextColor(resources.getColor(R.color.colorGrey, null))
                    tvOptionMedium.setTextColor(resources.getColor(R.color.colorGrey, null))
                    tvOptionHigh.setTextColor(resources.getColor(R.color.colorRed, null))
                }
            }
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

    private fun requestPermissionAndStartGalleryActivity() {
        var rxPermissions = RxPermissions(this)
        mPermissionsDisposable = rxPermissions
            .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe { granted ->
                if (granted) { // Always true pre-M
                    mPresenter!!.choosePhoto()
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

    override fun choosePhotoIntent(intent: Intent) {
        startActivityForResult(intent, AppConstants.REQUEST_CHOOSE_IMAGE_FROM_GALLERY)
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
        /*if (requestCode == AppConstants.REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                mPresenter!!.takePhotoResult()
            }
        }*/
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.REQUEST_IMAGE_CAPTURE) {
                mPresenter!!.takePhotoResult()
            }else if(requestCode == AppConstants.REQUEST_CHOOSE_IMAGE_FROM_GALLERY){
                mPresenter!!.choosePhotoResult(data)
            }
        }
    }

    override fun onResponseReport(message: String?, IAvalue: Int?) {
        progressBarReport.visibility = View.GONE
        if(message == null && IAvalue != 0){
            onResponseSendPhoto(IAvalue!!)
            Handler().postDelayed({
                if(mReportFromDialog!!){
                    /*var i = Intent(this, MainActivity::class.java)
                    i.putExtra(AppConstants.PUBLIC_PLACE, mPublicPlace)
                    setResult(AppConstants.REQUEST_CODE_REPORT_FROM_DIALOG, i)*/
                    finish()
                }
            }, 2500)
        }else if (IAvalue == 0){
            displayMessage("No se han detectado personas, por favor intentalo nuevamente")
        }else{
            displayMessage(message!!)
        }
    }

    //TODO send capacity and form
    override fun onClickSendPlaceCapacity(capacity: Int) {
        progressBarReport.visibility = View.VISIBLE
        mPresenter!!.sendReport(mPublicPlace!!.placeId!!, true, capacity)
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