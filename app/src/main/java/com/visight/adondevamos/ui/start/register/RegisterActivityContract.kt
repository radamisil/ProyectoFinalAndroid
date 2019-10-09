package com.visight.adondevamos.ui.start.register

import com.visight.adondevamos.data.entity.User
import com.visight.adondevamos.ui.base.BaseContract

interface RegisterActivityContract {
    interface View: BaseContract.View {
        //fun displayMessage(message: String)
        fun onResponseRegister(user: User?, message: String?)
    }

    interface Presenter: BaseContract.Presenter<RegisterActivityContract.View> {
        fun register(nombre: String, apellido: String, email: String, password: String, tipo: Int)
    }
}