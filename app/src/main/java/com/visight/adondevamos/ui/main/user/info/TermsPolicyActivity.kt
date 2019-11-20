package com.visight.adondevamos.ui.main.user.info

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import com.visight.adondevamos.R
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.utils.AppConstants
import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class TermsPolicyActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_privacy_policy)

        var title: String
        if(intent.getStringExtra(AppConstants.TERMS_KEY) != null){
            title = AppConstants.TERMS_KEY
        }else{
            title = AppConstants.PRIVACY_POLICY_KEY
        }
        setUpToolbar(toolbar, title, ivLogo)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun getContext(): Context {
        return this
    }
}