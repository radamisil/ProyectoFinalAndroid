package com.visight.adondevamos.data.local

import android.content.Context
import com.facebook.internal.Mutable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.visight.adondevamos.data.entity.PublicPlace
import com.visight.adondevamos.data.entity.User
import android.preference.PreferenceManager



open class SharedPreferencesManager {
    private val PREFS_NAME = "ADondeVamosPreferences"
    private val MODE = Context.MODE_PRIVATE

    private val USER_ADONDEVAMOS = "USER_ADONDEVAMOS"
    private val FAVS_ADONDEVAMOS = "FAVS_ADONDEVAMOS"

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

    open fun getFavourites(ctx: Context): MutableList<PublicPlace>? {
        try {
            val prefs = ctx.getSharedPreferences(PREFS_NAME, MODE)
            val json = prefs.getString(FAVS_ADONDEVAMOS, null)
            val type = object : TypeToken<List<PublicPlace>>() {
            }.type
            return Gson().fromJson(json, type)
        } catch (e: Exception) {
            return null
        }
    }

    open fun setFavourites(context: Context, favourites: MutableList<PublicPlace>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, MODE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(favourites)
        editor.putString(FAVS_ADONDEVAMOS, json)
        editor.apply()
    }
}