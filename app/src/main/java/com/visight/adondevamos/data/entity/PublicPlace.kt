package com.visight.adondevamos.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.gson.annotations.SerializedName

class PublicPlace() : Parcelable {
    var id: String? = ""
    var name: String? = ""
    var icon: String? = ""
    @SerializedName("formatted_address")
    var formattedAddress: String? = ""
    var geometry: Geometry? = null
    @SerializedName("opening_hours")
    var openingHours: OpeningHours? = null
    var photos: List<Photo> = ArrayList()
    var rating: Double? = 0.0
    @SerializedName("place_id")
    var placeId: String? = ""
    @SerializedName("price_level")
    var priceLevel: Int? = 0
    var types: List<String> = ArrayList()
    var vicinity: String? = ""
    var isFull: Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        icon = parcel.readString()
        formattedAddress = parcel.readString()
        rating = parcel.readDouble()
        placeId = parcel.readString()
        priceLevel = parcel.readInt()
        types = parcel.createStringArrayList()
        vicinity = parcel.readString()
        isFull = parcel.readByte() != 0.toByte()
    }

    //TODO PREGUNTAR POR QUE NECESITA ESE THIS() / CONSTRUCTORES SECUNDARIOS
    constructor(place: Place) : this() {
        name = place.name!!
        formattedAddress = place.address
        rating = place.rating
        placeId = place.id!!
        priceLevel = place.priceLevel
        geometry = Geometry(Location(place.latLng!!))
        isFull = false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(icon)
        parcel.writeString(formattedAddress)
        rating?.let { parcel.writeDouble(it) }
        parcel.writeString(placeId)
        priceLevel?.let { parcel.writeInt(it) }
        parcel.writeStringList(types)
        parcel.writeString(vicinity)
        parcel.writeByte(if (isFull) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PublicPlace> {
        override fun createFromParcel(parcel: Parcel): PublicPlace {
            return PublicPlace(parcel)
        }

        override fun newArray(size: Int): Array<PublicPlace?> {
            return arrayOfNulls(size)
        }
    }

}

class Geometry {
    var location: Location? = null

    constructor(location: Location){
        this.location = location
    }
}

class Location {
    var lat: Double = 0.0
    var lng: Double = 0.0

    constructor(latLng: LatLng){
        lat = latLng.latitude
        lng = latLng.longitude
    }
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