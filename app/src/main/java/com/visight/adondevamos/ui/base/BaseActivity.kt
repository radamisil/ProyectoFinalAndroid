package com.visight.adondevamos.ui.base

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.visight.adondevamos.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_toolbar.*

open abstract class BaseActivity: AppCompatActivity() {
    abstract  fun getContext(): Context

    fun setUpToolbar(toolbar: Toolbar, title: String?, ivLogo: ImageView){
        setSupportActionBar(toolbar)
        if(title != null){
            ivLogo.visibility = View.GONE

            supportActionBar!!.title = title
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        }else{
            supportActionBar!!.title = null
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }
}