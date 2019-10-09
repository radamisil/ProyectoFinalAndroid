package com.visight.adondevamos.ui.start.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.User
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.ui.main.user.MainActivity
import com.visight.adondevamos.ui.start.login.LoginActivity
import com.visight.adondevamos.utils.AppConstants
import com.visight.adondevamos.utils.DisplayMessage
import com.visight.adondevamos.utils.showMessage
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class RegisterActivity : BaseActivity(), RegisterActivityContract.View {
    var mPresenter: RegisterActivityContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setUpToolbar(toolbar, getString(R.string.text_registrarse), ivLogo)
        startPresenter()

        applyTypeface(tilPassword)
        applyTypeface(tilRePassword)

        btnRegister.setOnClickListener {
            if(validateFields(
                    tieName,
                    tieSurname,
                    tieEmail,
                    tiePassword)){
                progressBar.visibility = View.VISIBLE
                btnRegister.isEnabled = false
                mPresenter!!.register(
                    tieName.editableText.toString(),
                    tieSurname.editableText.toString(),
                    tieEmail.editableText.toString(),
                    tiePassword.editableText.toString(),
                    AppConstants.TYPE_USER)
            }
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateFields(tieName: TextInputEditText?, tieSurname: TextInputEditText?,
                               tieEmail: TextInputEditText?, tiePassword: TextInputEditText?): Boolean {

        var isValid = true

        if(!checkFields(tieName, AppConstants.FIELD_NAME) ||
            !checkFields(tieSurname, AppConstants.FIELD_SURNAME) ||
            !checkFields(tieEmail, AppConstants.FIELD_EMAIL) ||
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
                AppConstants.FIELD_NAME -> {
                    errorMessage = getString(R.string.text_por_favor_ingresa_un_nombre)
                    isValid = false
                }
                AppConstants.FIELD_SURNAME -> {
                    errorMessage = getString(R.string.text_por_favor_ingresa_un_apellido)
                    isValid = false
                }
                AppConstants.FIELD_EMAIL -> {
                    errorMessage = getString(R.string.text_por_favor_ingresa_un_correo_electronico)
                    isValid = false
                }
            }
        }else if(field == AppConstants.FIELD_EMAIL){
            if(!Patterns.EMAIL_ADDRESS.matcher(tie.editableText.toString()).matches()){
                errorMessage = getString(R.string.text_por_favor_ingresa_un_correo_electronico_valido)
                isValid = false
            }
        }else if(field == AppConstants.FIELD_PASSWORD){
            if(tie.editableText.toString() != tieRePassword.editableText.toString()){
                errorMessage = getString(R.string.text_las_contrasenias_no_coinciden)
                isValid = false
            }
        }

        if(!isValid) tie.error = errorMessage
        return isValid
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

    /*override fun displayMessage(message: String) {
        DisplayMessage().displayMessage(message, clMainContainer)
    }*/

    override fun onResponseRegister(user: User?, message: String?) {
        progressBar.visibility = View.GONE
        if(user != null){
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            intent.putExtra(AppConstants.IS_LOGGED, true)
            startActivity(intent)
            finish()
        }else{
            //displayMessage(message!!)
            showMessage(message!!, clMainContainer)
            btnRegister.isEnabled = true
        }
    }

    override fun startPresenter() {
        mPresenter = RegisterActivityPresenter()
        mPresenter!!.startView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}