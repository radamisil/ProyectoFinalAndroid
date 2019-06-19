package com.visight.adondevamos.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PublicPlace() : Parcelable {
    var id: String = ""
    var name: String = ""
    var icon: String = ""
    @SerializedName("formatted_address")
    var formattedAddress: String = ""
    var geometry: Geometry? = null
    @SerializedName("opening_hours")
    var openingHours: OpeningHours? = null
    var photos: List<Photo> = ArrayList()
    var rating: Float = 0f
    @SerializedName("place_id")
    var placeId: String = ""
    @SerializedName("price_level")
    var priceLevel: Int = 0
    var types: List<String> = ArrayList()
    var vicinity: String = ""
    var isFull: Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        icon = parcel.readString()
        formattedAddress = parcel.readString()
        rating = parcel.readFloat()
        placeId = parcel.readString()
        priceLevel = parcel.readInt()
        types = parcel.createStringArrayList()
        vicinity = parcel.readString()
        isFull = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(icon)
        parcel.writeString(formattedAddress)
        parcel.writeFloat(rating)
        parcel.writeString(placeId)
        parcel.writeInt(priceLevel)
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