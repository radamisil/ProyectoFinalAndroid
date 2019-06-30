package com.visight.adondevamos.ui.main.user

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import com.visight.adondevamos.R
import com.visight.adondevamos.ui.base.BaseActivity
import kotlinx.android.synthetic.main.layout_toolbar.*

class ProfileActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setUpToolbar(toolbar, "Mi perfil", ivLogo)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getContext(): Context {
       return this
    }

}