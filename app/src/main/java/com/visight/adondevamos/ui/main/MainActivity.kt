package com.visight.adondevamos.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.button.MaterialButton
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import com.visight.adondevamos.R
import com.visight.adondevamos.ui.main.mapView.MapViewFragment
import com.visight.adondevamos.ui.start.StartActivity
import com.visight.adondevamos.utils.AppConstants
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_content_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : AppCompatActivity() {
    private lateinit var mCurrentLocation: LatLng
    private lateinit var mLocationDisposable: Disposable
    private var isMapShowing: Boolean = false  //default, when not added yet or changed later

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

        getCurrentLocation()

        fabMyLocation.setOnClickListener {
            
        }

        btnRegisterLogin.setOnClickListener {
            val intent = Intent(this@MainActivity, StartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        var rxPermissions = RxPermissions(this)
        var rxLocation = RxLocation(this)
        mLocationDisposable = rxPermissions
            .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe { granted ->
                if (granted) { // Always true pre-M
                    //TODO PREGUNTAR COMO HACER PARA QUE ME RECONOZCA EL PERMISO ANTERIOR SIN USAR EL WARNING
                    rxLocation.location().lastLocation().subscribe {
                        mCurrentLocation = LatLng(it.latitude, it.longitude)
                        if(!isMapShowing){
                            isMapShowing = true
                            displayMapView()
                        }else{
                            isMapShowing = false
                            displayListView()
                        }
                    }
                } else {
                    rxPermissions.request(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
            }
    }

    private fun displayMapView() {
        val mapFragment = MapViewFragment()

        val bundle = Bundle().putParcelable(AppConstants.CURRENT_LOCATION_KEY, mCurrentLocation) as Bundle
        mapFragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.flFragmentContainer, mapFragment)
        transaction.commit()
    }

    private fun displayListView() {
        /*val mapFragment = MapViewFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.flFragmentContainer, mapFragment)
        transaction.commit()*/
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mLocationDisposable != null){
            mLocationDisposable.dispose()
        }
    }
}