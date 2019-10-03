package com.visight.adondevamos.ui.start.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.User
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.ui.main.user.MainActivity
import com.visight.adondevamos.ui.start.register.RegisterActivity
import com.visight.adondevamos.utils.AppConstants
import com.visight.adondevamos.utils.DisplayMessage
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class LoginActivity : BaseActivity(), LoginActivityContract.View {
    var mPresenter: LoginActivityContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUpToolbar(toolbar, getString(R.string.text_iniciar_sesion), ivLogo)
        startPresenter()

        val typeface = ResourcesCompat.getFont(this, R.font.opensans_regular)
        tilPassword.typeface = typeface

        btnLogin.setOnClickListener {
            if(validateFields(tieEmail, tiePassword)){
                progressBar.visibility = View.VISIBLE
                btnLogin.isEnabled = false
                mPresenter!!.login(tieEmail.editableText.toString())
            }
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
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

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun validateFields(tieEmail: TextInputEditText?, tiePassword: TextInputEditText?): Boolean {

        var isValid = true

        if(!checkFields(tieEmail, AppConstants.FIELD_EMAIL) ||
            !checkFields(tiePassword, AppConstants.FIELD_PASSWORD)){
            isValid = false
        }

        return isValid
    }

    private fun checkFields(tie: TextInputEditText?, field: String): Boolean {
        var errorMessage = ""
        var isValid = true

        if(tie!!.editableText.toString().isEmpty()){
            when(field){
                AppConstants.FIELD_EMAIL -> {
                    errorMessage = getString(R.string.text_por_favor_ingresa_el_correo_electronico)
                    isValid = false
                }
                AppConstants.FIELD_PASSWORD -> {
                    errorMessage = getString(R.string.text_por_favor_ingresa_la_contrasenia)
                    isValid = false
                }
            }
        }
        tie.error = errorMessage
        return isValid
    }

    override fun displayMessage(message: String) {
        DisplayMessage().displayMessage(message, clMainContainer)
    }

    override fun onResponseLogin(user: User?, message: String?) {
        progressBar.visibility = View.VISIBLE
        if(message == null){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            intent.putExtra(AppConstants.IS_LOGGED, true)
            startActivity(intent)
            finish()
        }else{
            displayMessage(message)
            btnLogin.isEnabled = true
        }

    }

    override fun startPresenter() {
        mPresenter = LoginActivityPresenter()
        mPresenter!!.startView(this)
    }
}