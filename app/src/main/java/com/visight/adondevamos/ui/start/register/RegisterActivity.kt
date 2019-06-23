package com.visight.adondevamos.ui.start.register

import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputLayout
import com.visight.adondevamos.R
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ivLogo.visibility = View.GONE

        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.text_registrarse)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)

        applyTypeface(tilPassword)
        applyTypeface(tilRePassword)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun applyTypeface(til: TextInputLayout){
        val typeface = ResourcesCompat.getFont(this, R.font.opensans_regular)
        til.typeface = typeface
    }
}