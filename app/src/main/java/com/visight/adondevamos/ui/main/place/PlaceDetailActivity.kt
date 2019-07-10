package com.visight.adondevamos.ui.main.place

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tbruyelle.rxpermissions2.RxPermissions
import com.visight.adondevamos.R
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.ui.main.place.report.ReportFromPlaceActivity
import com.visight.adondevamos.utils.AppConstants
import com.visight.adondevamos.utils.GlideApp
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.activity_report_place_attendance.*
import java.io.File

class PlaceDetailActivity : BaseActivity(), PlaceDetailActivityContract.View{
    private var mPresenter: PlaceDetailActivityContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)
        startPresenter()

        fabGoToReport.setOnClickListener {
            val intent = Intent(this@PlaceDetailActivity, ReportFromPlaceActivity::class.java)
            //intent.putExtra(AppConstants.PUBLIC_PLACE, place)
            startActivity(intent)
        }
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