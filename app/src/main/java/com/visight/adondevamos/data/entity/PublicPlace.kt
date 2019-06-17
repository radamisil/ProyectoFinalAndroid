package com.visight.adondevamos.data.entity

import com.google.gson.annotations.SerializedName

class PublicPlace {
    var name: String = ""
    @SerializedName("formatted_address")
    var formattedAddress: String = ""
    var geometry: Geometry?= null
    @SerializedName("opening_hours")
    var openingHours: OpeningHours? = null
    var photos: List<Photo> = ArrayList()
    var rating: Float = 0f
    @SerializedName("place_id")
    var placeId: String = ""
    @SerializedName("price_level")
    var priceLevel: Int = 0
    var types: List<String> = ArrayList()
    @SerializedName("permanently_closed")
    var permanentlyClosed: Boolean = false
    var isFull: Boolean = false
}

class Geometry {
    var location: Location? = null
}

class Location {
    var lat: Double = 0.0
    var lng: Double = 0.0
}

class OpeningHours {
    @SerializedName("open_now")
    var openNow: Boolean = false
}

class Photo {
    @SerializedName("photo_reference")
    var photoReference: String = ""
    @SerializedName("html_attributions")
    var htmlAttributions: List<String> = ArrayList()

}