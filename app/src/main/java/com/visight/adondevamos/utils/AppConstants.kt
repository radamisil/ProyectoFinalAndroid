package com.visight.adondevamos.utils

import android.content.Context

object AppConstants {
    @JvmField val mapZoomIn: Float = 10f
    @JvmField val mapZoomInSpecificPlace: Float = 100f
    @JvmField val searchRadius: Int = 1000
    @JvmField val IS_LOGGED = "isLogged"
    @JvmField val CURRENT_LOCATION_KEY = "currentLocation"
    @JvmField val CURRENT_PLACE_KEY = "currentPlace"
    @JvmField val PUBLIC_PLACE = "publicPlace"
    @JvmField val PLACE_AUTOCOMPLETE_REQUEST_CODE = 600
    @JvmField val REQUEST_IMAGE_CAPTURE = 183
    @JvmField val PREFS_NAME = "ADondeVamosPreferences"
    @JvmField val MODE = Context.MODE_PRIVATE
    @JvmField val PREFS_USER_NAME ="username"
    @JvmField val PREFS_USER_SURNAME = "surname"
    @JvmField val PREFS_USER_EMAIL = "email"

    @JvmField val PLACE_PREVIEW_DIALOG = "placePreviewDialog"
}