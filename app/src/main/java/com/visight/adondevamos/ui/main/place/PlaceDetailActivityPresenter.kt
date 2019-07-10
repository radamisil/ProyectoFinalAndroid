package com.visight.adondevamos.ui.main.place

import androidx.annotation.NonNull

class PlaceDetailActivityPresenter : PlaceDetailActivityContract.Presenter{
    var mView: PlaceDetailActivityContract.View? = null

    override fun startView(@NonNull view: PlaceDetailActivityContract.View) {
        mView = view
    }

    override fun onDestroy() {
        mView = null
    }
}