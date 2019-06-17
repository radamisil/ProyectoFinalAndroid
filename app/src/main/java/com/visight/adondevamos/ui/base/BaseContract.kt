package com.visight.adondevamos.ui.base

import android.content.Context

interface BaseContract {
    interface View {
        fun getContext(): Context
        fun startPresenter()
        fun onDestroy()
    }

    interface Presenter<VIEW: BaseContract.View> {
        fun startView(view: VIEW)
        fun onDestroy()
    }
}