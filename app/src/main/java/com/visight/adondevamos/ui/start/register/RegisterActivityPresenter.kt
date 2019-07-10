package com.visight.adondevamos.ui.start.register

import com.visight.adondevamos.data.entity.User
import com.visight.adondevamos.data.remote.AppServices
import com.visight.adondevamos.data.remote.requests.RegisterRequest
import com.visight.adondevamos.data.remote.responses.UserResponse
import com.visight.adondevamos.utils.AppConstants
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RegisterActivityPresenter: RegisterActivityContract.Presenter{
    var mView: RegisterActivityContract.View? = null
    var mDisposable: Disposable? = null

    override fun startView(view: RegisterActivityContract.View) {
       mView = view
    }

    override fun onDestroy() {
        mView = null
    }

    override fun register(nombre: String, apellido: String, email: String, password: String, tipo: Int) {
        var request = RegisterRequest(nombre, apellido, email, password, tipo)
        mDisposable = AppServices().getClient().register(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe { user: User ->
                    run {
                        //SharedPreferencesManager().setUser(mView!!.getContext(), user!!)

                        val prefs = mView!!.getContext().getSharedPreferences(AppConstants.PREFS_NAME, AppConstants.MODE)
                        with (prefs.edit()) {
                            putString(AppConstants.PREFS_USER_NAME, user.name)
                            putString(AppConstants.PREFS_USER_SURNAME, user.surname)
                            putString(AppConstants.PREFS_USER_EMAIL, user.email)
                            commit()
                        }
                        mView!!.onResponseRegister(user, null)
                    }
                }
    }
}