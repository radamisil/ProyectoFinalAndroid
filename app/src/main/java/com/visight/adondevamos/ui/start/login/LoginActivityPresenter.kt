package com.visight.adondevamos.ui.start.login

import android.content.Context
import com.google.android.libraries.places.internal.it
import com.visight.adondevamos.R
import com.visight.adondevamos.data.entity.User
import com.visight.adondevamos.data.local.SharedPreferencesManager
import com.visight.adondevamos.data.remote.AppServices
import com.visight.adondevamos.data.remote.responses.UserResponse
import com.visight.adondevamos.utils.AppConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginActivityPresenter: LoginActivityContract.Presenter{
    var mView: LoginActivityContract.View? = null
    var mDisposable: Disposable? = null

    override fun startView(view: LoginActivityContract.View) {
        mView = view
    }

    override fun onDestroy() {
        mView = null
    }

    override fun login(email: String) {
        mDisposable = AppServices().getClient().login(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ onSuccess(it.data!![0]) }, { onRegisterError() })
                /*.subscribe { usersResponse: UserResponse ->
                    run {
                        //SharedPreferencesManager().setUser(mView!!.getContext(), user!!)
                        var user = usersResponse.data!!.get(0)

                        val prefs = mView!!.getContext().getSharedPreferences(AppConstants.PREFS_NAME, AppConstants.MODE)
                        with (prefs.edit()) {
                            putString(AppConstants.PREFS_USER_NAME, user.name)
                            putString(AppConstants.PREFS_USER_SURNAME, user.surname)
                            putString(AppConstants.PREFS_USER_EMAIL, user.email)
                            commit()
                        }
                        mView!!.onResponseLogin(user, null)
                    }
                }*/
    }

    private fun onSuccess(user: User) {
        val prefs = mView!!.getContext().getSharedPreferences(AppConstants.PREFS_NAME, AppConstants.MODE)
        with (prefs.edit()) {
            putString(AppConstants.PREFS_USER_NAME, user.name)
            putString(AppConstants.PREFS_USER_SURNAME, user.surname)
            putString(AppConstants.PREFS_USER_EMAIL, user.email)
            commit()
        }
        mView!!.onResponseLogin(user, null)
    }

    private fun onRegisterError() {
        mView!!.onResponseLogin(null, mView!!.getContext().getString(R.string.text_error_login))
    }
}