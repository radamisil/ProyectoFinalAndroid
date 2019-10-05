package com.visight.adondevamos.ui.main.user

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
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
import kotlinx.android.synthetic.main.layout_sidebar_header.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.tabs.TabLayout
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.data.entity.User
import com.visight.adondevamos.data.local.SharedPreferencesManager
import com.visight.adondevamos.ui.main.listView.ListViewFragment
import com.visight.adondevamos.ui.main.mapView.mapUtils.MapItem
import com.visight.adondevamos.ui.main.place.PlaceDetailActivity
import com.visight.adondevamos.ui.main.user.favourites.FavouritesActivity
import kotlinx.android.synthetic.main.layout_tabs_categories.*
import kotlinx.android.synthetic.main.layout_tabs_categories.view.*
import java.util.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var mCurrentLocation: LatLng
    private var mLocationDisposable: Disposable? = null
    private var isMapShowing: Boolean = false  //default, when not added yet or changed later
    private lateinit var mCurrentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*if (intent?.extras?.get(AppConstants.IS_LOGGED) == null ||
                intent?.extras?.get(AppConstants.IS_LOGGED) == false
        ) {
            llContainerNoSessionRegisteredOptions.visibility = View.VISIBLE
        } else {
            llContainerNoSessionRegisteredOptions.visibility = View.GONE
        }*/
        val prefs = this.getSharedPreferences(AppConstants.PREFS_NAME, AppConstants.MODE)
        if (prefs.getString(AppConstants.PREFS_USER_NAME, "") != null
                && prefs.getString(AppConstants.PREFS_USER_NAME, "").isNotEmpty()) {
            llContainerNoSessionRegisteredOptions.visibility = View.GONE
        } else {
            llContainerNoSessionRegisteredOptions.visibility = View.VISIBLE
        }

        setSupportActionBar(toolbar)
        supportActionBar!!.title = null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)

        /*if(SharedPreferencesManager().getUser(this) != null){
            setSidebarUserInfo(SharedPreferencesManager().getUser(this))
        }*/

        //val prefs = this.getSharedPreferences(AppConstants.PREFS_NAME, AppConstants.MODE)
        if (prefs.getString(AppConstants.PREFS_USER_NAME, "") != null
                && prefs.getString(AppConstants.PREFS_USER_NAME, "").isNotEmpty()) {
            setSidebarUserInfo()
        }

        toolbar.setNavigationOnClickListener {
            dlDrawer.openDrawer(GravityCompat.START)
        }

        nvDrawer.setNavigationItemSelectedListener(this)
        getCurrentLocation()
        setCategories()

        nvDrawer.getHeaderView(0).btnSeeProfile.isClickable =
                if (llContainerNoSessionRegisteredOptions.visibility != View.VISIBLE) true else false
        nvDrawer.getHeaderView(0).btnSeeProfile.isEnabled =
                if (llContainerNoSessionRegisteredOptions.visibility != View.VISIBLE) true else false

        nvDrawer.getHeaderView(0).btnSeeProfile.setOnClickListener {
            if (llContainerNoSessionRegisteredOptions.visibility != View.VISIBLE) {
                //if(dlDrawer.isDrawerOpen(GravityCompat.START)) dlDrawer.closeDrawer(GravityCompat.START)
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
        }

        // Initialize Places.
        Places.initialize(getApplicationContext(), getResources().getString(R.string.maps_debug_api_key))

        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (supportFragmentManager.findFragmentById(R.id.flFragmentContainer) is MapViewFragment) {
                    (supportFragmentManager.findFragmentById(R.id.flFragmentContainer) as MapViewFragment)
                            .callMethodGetPublicPlaces(p0!!.text.toString())
                }
            }

        })

        fabMyLocation.setOnClickListener {
            mCurrentFragment = supportFragmentManager.findFragmentById(R.id.flFragmentContainer)!!
            if (mCurrentFragment is MapViewFragment) {
                (mCurrentFragment as MapViewFragment).setCameraToCurrentPosition(mCurrentLocation)
            } else {
                //TODO implement change of current position to List view
            }
        }

        fabListMode.setOnClickListener {
            mCurrentFragment = supportFragmentManager.findFragmentById(R.id.flFragmentContainer)!!
            if (!isMapShowing) {
                isMapShowing = true
                displayMapView()
            } else {
                isMapShowing = false
                displayListView((mCurrentFragment as MapViewFragment).getPlacesList())
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

    private fun setSidebarUserInfo() {
        val prefs = getSharedPreferences(AppConstants.PREFS_NAME, AppConstants.MODE)
        var name = prefs.getString(AppConstants.PREFS_USER_NAME, "")
        nvDrawer.getHeaderView(0).tvSidebarUser.text = "¡Hola, " + name + "!"
    }

    private fun setCategories() {
        val placeTypes = resources.getStringArray(R.array.placeTypes)
        for (s in placeTypes) {
            mTabLayout.addTab(mTabLayout.newTab().setText(s))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.optionSearch) {
            var fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
            var intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .build(this)
            startActivityForResult(intent, AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if (llContainerNoSessionRegisteredOptions.visibility != View.VISIBLE) {
            when (menuItem.itemId) {
                R.id.menuMap -> {
                    if (supportFragmentManager.findFragmentById(R.id.flFragmentContainer) is MapViewFragment) {
                        dlDrawer.closeDrawer(GravityCompat.START)
                    }else{
                        displayMapView()
                    }
                    //Toast.makeText(this, "Android Store", Toast.LENGTH_SHORT).show()
                }
                R.id.menuFavourites -> {
                    val intent = Intent(this@MainActivity, FavouritesActivity::class.java)
                    startActivity(intent)
                    //Toast.makeText(this, "Newsletter", Toast.LENGTH_SHORT).show()
                }
                /*R.id.menuHistory -> {
                    //Toast.makeText(this, "Join Community", Toast.LENGTH_SHORT).show()
                }*/
                R.id.menuInfo -> {
                    //Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show()
                }
                R.id.menuLogout -> {
                    logout()
                    //Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show()
                }
            }
            dlDrawer.closeDrawer(GravityCompat.START)
            return true
        } else {
            return false
        }
    }

    private fun logout() {
        val prefs = this.getSharedPreferences(AppConstants.PREFS_NAME, AppConstants.MODE)
        with (prefs.edit()) {
            putString(AppConstants.PREFS_USER_NAME, "")
            putString(AppConstants.PREFS_USER_SURNAME,"")
            putString(AppConstants.PREFS_USER_EMAIL, "")
            commit()
        }
        this.finish()
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
                            if (!isMapShowing) {
                                isMapShowing = true
                                displayMapView()
                            } else {
                                isMapShowing = false
                                displayListView((mCurrentFragment as MapViewFragment).getPlacesList())
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
        transaction.replace(R.id.flFragmentContainer, mapFragment)
        transaction.commit()
    }

    private fun displayListView(placesList: MutableList<PublicPlace>?) {
        val listFragment = ListViewFragment()

        val bundle = Bundle()
        bundle.putParcelable(AppConstants.CURRENT_LOCATION_KEY, mCurrentLocation)
        bundle.putParcelableArrayList(AppConstants.PLACES_LIST_KEY, placesList as ArrayList<out Parcelable>)
        listFragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragmentContainer, listFragment)
        transaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode != AutocompleteActivity.RESULT_ERROR) {
                    var place = Autocomplete.getPlaceFromIntent(data!!)
                    //TODO send place to map fragment

                    if (supportFragmentManager.findFragmentById(R.id.flFragmentContainer) is MapViewFragment) {
                        var mapFragment = supportFragmentManager.findFragmentById(R.id.flFragmentContainer) as MapViewFragment
                        val transaction = supportFragmentManager.beginTransaction()

                        transaction
                                .detach(mapFragment)
                                .attach(mapFragment)

                        val bundle = Bundle()
                        var publicPlace = PublicPlace(place)  //place to display in the marker
                        bundle.putParcelable(AppConstants.CURRENT_PLACE_KEY, publicPlace)
                        mapFragment.arguments = bundle

                        transaction.commit()
                    }
                } else {
                    var status = Autocomplete.getStatusFromIntent(data!!)
                    Log.d("AUTOCOMPLETE ERROR", status.statusMessage)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun redirectToPlaceDetailActivity(place: PublicPlace) {
        val intent = Intent(this@MainActivity, PlaceDetailActivity::class.java)
        intent.putExtra(AppConstants.PUBLIC_PLACE, place)
        startActivity(intent)
    }

    override fun getContext(): Context {
        return this
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mLocationDisposable != null) {
            mLocationDisposable!!.dispose()
        }
    }
}