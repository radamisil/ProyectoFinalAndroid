package com.visight.adondevamos.ui.main.user.favourites

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.visight.adondevamos.R
import com.visight.adondevamos.adapters.PlacesListAdapter
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.data.local.SharedPreferencesManager
import com.visight.adondevamos.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class FavouritesActivity : BaseActivity() {
    //var mFavouritePlacesAdapter: FavouritePlacesAdapter? = null
    var mFavouritePlacesAdapter: PlacesListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        setUpToolbar(toolbar, getString(R.string.text_favoritos), ivLogo)

        if (SharedPreferencesManager().getFavourites(this).isNullOrEmpty()) {
            rvPlaces.visibility = View.GONE
            tvEmptyList.visibility = View.VISIBLE
        } else {
            tvEmptyList.visibility = View.GONE
            rvPlaces.visibility = View.VISIBLE
            mFavouritePlacesAdapter = PlacesListAdapter(SharedPreferencesManager().getFavourites(this)!! as List<PublicPlace>)
            rvPlaces.adapter = mFavouritePlacesAdapter
            rvPlaces.layoutManager = LinearLayoutManager(
                this@FavouritesActivity,
                RecyclerView.VERTICAL, false
            )
        }

        /*val db = Room.databaseBuilder(
            applicationContext,
            FavouritePlacesDatabase::class.java, AppConstants.DATABASE_NAME
        ).build()

        GlobalScope.launch {
            var data = db.favouritePlaceDao().getAllFavourites()

            if(data.isEmpty()){
                Log.d("DB", "empty")
                rvPlaces.visibility = View.GONE
                tvEmptyList.visibility = View.VISIBLE
            }else{
                tvEmptyList.visibility = View.GONE
                rvPlaces.visibility = View.VISIBLE
                mFavouritePlacesAdapter = FavouritePlacesAdapter(data)
                rvPlaces.adapter = mFavouritePlacesAdapter
                rvPlaces.layoutManager = LinearLayoutManager(this@FavouritesActivity,
                    RecyclerView.VERTICAL, false)

                *//*data.forEach {
                    println(it)
                }*//*
            }
        }*/
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
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