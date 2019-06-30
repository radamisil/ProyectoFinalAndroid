package com.visight.adondevamos.ui.start.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputLayout
import com.visight.adondevamos.R
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.ui.main.user.MainActivity
import com.visight.adondevamos.ui.start.login.LoginActivity
import com.visight.adondevamos.utils.AppConstants
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setUpToolbar(toolbar, getString(R.string.text_registrarse), ivLogo)

        applyTypeface(tilPassword)
        applyTypeface(tilRePassword)

        btnRegister.setOnClickListener {
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            intent.putExtra(AppConstants.IS_LOGGED, true)
            startActivity(intent)
            finish()
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun applyTypeface(til: TextInputLayout) {
        val typeface = ResourcesCompat.getFont(this, R.font.opensans_regular)
        til.typeface = typeface
    }

    override fun getContext(): Context {
        return this
    }
}