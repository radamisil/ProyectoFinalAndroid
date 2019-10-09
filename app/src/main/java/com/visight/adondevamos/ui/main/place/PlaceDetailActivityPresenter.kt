package com.visight.adondevamos.ui.main.place

import androidx.annotation.NonNull
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.data.remote.GooglePlacesService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PlaceDetailActivityPresenter : PlaceDetailActivityContract.Presenter{
    var mView: PlaceDetailActivityContract.View? = null
    var mPhotoDisposable: Disposable? = null

    override fun startView(@NonNull view: PlaceDetailActivityContract.View) {
        mView = view
    }

    override fun onDestroy() {
        mView = null
    }
}