package com.visight.adondevamos.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.visight.adondevamos.data.entity.User

open class SharedPreferencesManager {
    private val PREFS_NAME = "ADondeVamosPreferences"
    private val MODE = Context.MODE_PRIVATE

    private val USER_ADONDEVAMOS = "USER_ADONDEVAMOS"

    open fun getUser(ctx: Context): User {
        val prefs = ctx.getSharedPreferences(PREFS_NAME, MODE)
        val json = prefs.getString(USER_ADONDEVAMOS, "")

        val gson = GsonBuilder().create()
        val type = object : TypeToken<User>() {}.type
        return gson.fromJson(json, type)
    }

    open fun setUser(context: Context, user: User) {
        val prefs = context.getSharedPreferences(PREFS_NAME, MODE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString(USER_ADONDEVAMOS, json)
        editor.apply()
    }

    open fun removeUser(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, MODE)
        val editor = prefs.edit()
        editor.remove(USER_ADONDEVAMOS)
                .apply()
    }
}