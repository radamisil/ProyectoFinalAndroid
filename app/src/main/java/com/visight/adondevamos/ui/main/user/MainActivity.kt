package com.visight.adondevamos.ui.main.user

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import com.visight.adondevamos.R
import com.visight.adondevamos.ui.base.BaseActivity
import com.visight.adondevamos.ui.main.mapView.MapViewFragment
import com.visight.adondevamos.ui.start.StartActivity
import com.visight.adondevamos.utils.AppConstants
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_content_main.*
import kotlinx.android.synthetic.main.layout_sidebar_header.*
import kotlinx.android.synthetic.main.layout_sidebar_header.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var mCurrentLocation: LatLng
    private var mLocationDisposable: Disposable? = null
    private var isMapShowing: Boolean = false  //default, when not added yet or changed later
    private lateinit var mCurrentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent?.extras?.get(AppConstants.IS_LOGGED) == null ||
            intent?.extras?.get(AppConstants.IS_LOGGED) == false
        ) {
            llContainerNoSessionRegisteredOptions.visibility = View.VISIBLE
        } else {
            llContainerNoSessionRegisteredOptions.visibility = View.GONE
        }

        setSupportActionBar(toolbar)
        supportActionBar!!.title = null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)

        toolbar.setNavigationOnClickListener {
            dlDrawer.openDrawer(GravityCompat.START)
        }

        nvDrawer.setNavigationItemSelectedListener(this)
        getCurrentLocation()

        nvDrawer.getHeaderView(0).btnSeeProfile.isClickable =
            if (llContainerNoSessionRegisteredOptions.visibility != View.VISIBLE) true else false
        nvDrawer.getHeaderView(0).btnSeeProfile.isEnabled =
            if (llContainerNoSessionRegisteredOptions.visibility != View.VISIBLE) true else false

        nvDrawer.getHeaderView(0).btnSeeProfile.setOnClickListener {
            if(llContainerNoSessionRegisteredOptions.visibility != View.VISIBLE){
                //if(dlDrawer.isDrawerOpen(GravityCompat.START)) dlDrawer.closeDrawer(GravityCompat.START)
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
        }

        fabMyLocation.setOnClickListener {
            mCurrentFragment = supportFragmentManager.findFragmentById(R.id.flFragmentContainer)!!
            if(mCurrentFragment is MapViewFragment){
                (mCurrentFragment as MapViewFragment).setCameraToCurrentPosition(mCurrentLocation)
            }else{
                //TODO implement change of current position to List view
            }
        }

        btnRegisterLogin.setOnClickListener {
            val intent = Intent(this@MainActivity, StartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            //if(dlDrawer.isDrawerOpen(GravityCompat.START)) dlDrawer.closeDrawer(GravityCompat.START)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if(llContainerNoSessionRegisteredOptions.visibility != View.VISIBLE){
            when (menuItem.itemId) {
                R.id.menuMap -> {
                    Toast.makeText(this, "Android Store", Toast.LENGTH_SHORT).show()
                }
                R.id.menuFavourites -> {
                    Toast.makeText(this, "Newsletter", Toast.LENGTH_SHORT).show()
                }
                R.id.menuHistory -> {
                    Toast.makeText(this, "Join Community", Toast.LENGTH_SHORT).show()
                }
                R.id.menuInfo -> {
                    Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show()
                }
                R.id.menuLogout -> {
                    Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show()
                }
            }
            dlDrawer.closeDrawer(GravityCompat.START)
            return true
        }else{
            return false
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

        val bundle = Bundle()
        bundle.putParcelable(AppConstants.CURRENT_LOCATION_KEY, mCurrentLocation)
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

    override fun getContext(): Context {
        return this
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mLocationDisposable != null){
            mLocationDisposable!!.dispose()
        }
    }
}