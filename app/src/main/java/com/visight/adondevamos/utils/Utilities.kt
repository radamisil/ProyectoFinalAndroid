package com.visight.adondevamos.utils

open class Utilities {

    fun loadPlaceImageFromGoogle(photoReference: String) : String {
        return "https://maps.googleapis.com/maps/api/place/" +
                "photo?maxwidth=500&photoreference=" +
                photoReference +
                "&key=AIzaSyBMfHFvJTPHMgD5zBbRbuJdOjIOJ_HdL4o"
    }
}