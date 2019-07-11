package com.visight.adondevamos.ui.main.place

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tbruyelle.rxpermissions2.RxPermissions
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.ui.main.place.report.ReportFromPlaceActivity
import com.visight.adondevamos.utils.AppConstants
import com.visight.adondevamos.utils.GlideApp
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.activity_report_place_attendance.*
import kotlinx.android.synthetic.main.layout_place_detail_content.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.io.File

class PlaceDetailActivity : BaseActivity(), PlaceDetailActivityContract.View{
    var mPresenter: PlaceDetailActivityContract.Presenter? = null
    var mPublicPlace: PublicPlace? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)
        startPresenter()

        mPublicPlace = intent.getParcelableExtra(AppConstants.PUBLIC_PLACE)
        setUpToolbar(toolbar, mPublicPlace!!.name, ivLogo)
        setPlaceInformation(mPublicPlace)

        /*if(mPublicPlace!!.photos != null && mPublicPlace!!.photos.isNotEmpty()){
            mPresenter!!.getPlacePhoto(mPublicPlace!!)
        }else{
            GlideApp.with(this)
                    .load(R.drawable.ic_add)
                    .into(ivHeader)
        }*/

        btnGoToReport.setOnClickListener {
            val intent = Intent(this@PlaceDetailActivity, ReportFromPlaceActivity::class.java)
            intent.putExtra(AppConstants.PUBLIC_PLACE, mPublicPlace)
            startActivity(intent)
        }
    }

    override fun setPlacePhoto(photoUrl: String) {
            /*GlideApp.with(this)
                    .load(photoUrl)
                    .into(ivHeader)*/
    }

    private fun setPlaceInformation(mPublicPlace: PublicPlace?) {

    }

    override fun getContext(): Context {
        return this
    }

    override fun displayMessage(message: String) {

    }

    override fun startPresenter() {
        mPresenter = PlaceDetailActivityPresenter()
        mPresenter!!.startView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}