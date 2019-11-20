package com.visight.adondevamos.ui.main.user.info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.visight.adondevamos.R
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.utils.AppConstants
import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class InformationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        setUpToolbar(toolbar, getString(R.string.text_informacion), ivLogo)

        tvTerms.setOnClickListener {
            val intent = Intent(this@InformationActivity, TermsPolicyActivity::class.java)
            intent.putExtra(AppConstants.TERMS_KEY, AppConstants.TERMS_KEY)
            startActivity(intent)
        }

        tvPrivacyPolicy.setOnClickListener {
            val intent = Intent(this@InformationActivity, TermsPolicyActivity::class.java)
            intent.putExtra(AppConstants.PRIVACY_POLICY_KEY, AppConstants.PRIVACY_POLICY_KEY)
            startActivity(intent)
        }
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