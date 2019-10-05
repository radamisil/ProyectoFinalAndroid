package com.visight.adondevamos.ui.main.listView

import com.visight.adondevamos.data.entity.PublicPlace

class ListViewFragmentPresenter : ListViewFragmentContract.Presenter{
    private var mView: ListViewFragmentContract.View? = null
    private var allPublicPlacesList: MutableList<PublicPlace>? = mutableListOf()

    override fun startView(view: ListViewFragmentContract.View) {
       mView = view
    }

    override fun onDestroy() {
        mView = null
    }

    override fun setPublicPlacesList(list: List<PublicPlace>) {
        allPublicPlacesList!!.addAll(list)
    }

    override fun getStoredPlacesList(): List<PublicPlace> {
        return allPublicPlacesList!!
    }

}