package com.visight.adondevamos.ui.start.login

import com.visight.adondevamos.data.entity.User
import com.visight.adondevamos.ui.base.BaseContract

interface LoginActivityContract {
    interface View: BaseContract.View {
        fun displayMessage(message: String)
        fun onResponseLogin(user: User?, message: String?)
    }

    interface Presenter: BaseContract.Presenter<LoginActivityContract.View> {
        fun login(email: String)
    }
}