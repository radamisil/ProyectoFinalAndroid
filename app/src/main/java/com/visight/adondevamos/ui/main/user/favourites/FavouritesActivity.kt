package com.visight.adondevamos.ui.main.user.favourites

import android.content.Context
import android.os.Bundle
import com.visight.adondevamos.R
import com.visight.adondevamos.ui.base.BaseActivity
import kotlinx.android.synthetic.main.layout_toolbar.*

class FavouritesActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        setUpToolbar(toolbar, getString(R.string.text_favoritos), ivLogo)
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun getContext(): Context {
        return this
    }
}