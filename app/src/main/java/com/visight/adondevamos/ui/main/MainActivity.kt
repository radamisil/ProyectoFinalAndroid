package com.visight.adondevamos.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.button.MaterialButton
import com.visight.adondevamos.R
import com.visight.adondevamos.ui.main.mapView.MapViewFragment
import com.visight.adondevamos.ui.start.StartActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)

        toolbar.setNavigationOnClickListener{
            dlDrawer.openDrawer(GravityCompat.START)
        }

        val mapFragment = MapViewFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.flFragmentContainer, mapFragment)
        transaction.commit()

        btnRegisterLogin.setOnClickListener {
            val intent = Intent(this@MainActivity, StartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }
}