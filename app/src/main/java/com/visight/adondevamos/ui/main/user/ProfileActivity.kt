package com.visight.adondevamos.ui.main.user

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.User
import com.visight.adondevamos.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class ProfileActivity : BaseActivity(){
    var isEditOptionVisible: Boolean? = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setUpToolbar(toolbar, "Mi perfil", ivLogo)

        var user =  User()
        user.name = "Adam"
        user.surname = "Levine"
        user.email = "adam.levine@hotmail.com"

        tieName.setText(user.name)
        tieSurname.setText(user.surname)
        tieEmail.setText(user.email)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.menu_profile, menu)

        if(!isEditOptionVisible!!){
            menu!!.findItem(R.id.optionEdit).setVisible(false)
            menu!!.findItem(R.id.optionConfirm).setVisible(true)
        }else{
            menu!!.findItem(R.id.optionEdit).setVisible(true)
            menu!!.findItem(R.id.optionConfirm).setVisible(false)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.optionEdit -> {
                isEditOptionVisible = false
                toolbar.menu.findItem(R.id.optionEdit).isVisible = false
                toolbar.menu.findItem(R.id.optionConfirm).isVisible = true
                tieName.isEnabled = true
                tieSurname.isEnabled = true
                tieEmail.isEnabled = true
            }
            R.id.optionConfirm -> {
                isEditOptionVisible = true
                toolbar.menu.findItem(R.id.optionEdit).isVisible = true
                toolbar.menu.findItem(R.id.optionConfirm).isVisible = false
                tieName.isEnabled = false
                tieSurname.isEnabled = false
                tieEmail.isEnabled = false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getContext(): Context {
       return this
    }

}