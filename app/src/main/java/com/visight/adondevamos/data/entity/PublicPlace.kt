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
    var photos: List<Photo>? = ArrayList()
    var rating: Double? = 0.0
    @SerializedName("place_id")
    var placeId: String? = ""
    @SerializedName("price_level")
    var priceLevel: Int? = 0
    var types: List<String> = ArrayList()
    var vicinity: String? = ""
    var isFull: Boolean = false
    var promotions: List<Promotion>? = listOf()

    //custom
    var latitude: String? = null
    var longitude: String? = null
    var photoReference: String? = null

    constructor(customPublicPlace: CustomPublicPlace) : this() {
        id = customPublicPlace.IdDatos_comercio.toString()
        name = customPublicPlace.placeName
        icon = customPublicPlace.placeIcon
        formattedAddress = customPublicPlace.placeFormatted_addres
        latitude = customPublicPlace.placeLatitude
        longitude = customPublicPlace.placeLongitude
        photoReference = customPublicPlace.placePhoto_reference
        rating = customPublicPlace.placeRating!!.toDouble()
        placeId = customPublicPlace.placeId
        vicinity = customPublicPlace.placeVicinity
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        icon = parcel.readString()
        formattedAddress = parcel.readString()
        geometry = parcel.readParcelable(Geometry::class.java.classLoader)
        openingHours = parcel.readParcelable(OpeningHours::class.java.classLoader)
        photos = parcel.createTypedArrayList(Photo)
        rating = parcel.readValue(Double::class.java.classLoader) as? Double
        placeId = parcel.readString()
        priceLevel = parcel.readValue(Int::class.java.classLoader) as? Int
        types = parcel.createStringArrayList()
        vicinity = parcel.readString()
        isFull = parcel.readByte() != 0.toByte()
        promotions = parcel.createTypedArrayList(Promotion)
    }

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
        parcel.writeParcelable(geometry, flags)
        parcel.writeParcelable(openingHours, flags)
        parcel.writeTypedList(photos)
        parcel.writeValue(rating)
        parcel.writeString(placeId)
        parcel.writeValue(priceLevel)
        parcel.writeStringList(types)
        parcel.writeString(vicinity)
        parcel.writeByte(if (isFull) 1 else 0)
        parcel.writeTypedList(promotions)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PublicPlace

        if (placeId != other.placeId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + (formattedAddress?.hashCode() ?: 0)
        result = 31 * result + (geometry?.hashCode() ?: 0)
        result = 31 * result + (openingHours?.hashCode() ?: 0)
        result = 31 * result + (photos?.hashCode() ?: 0)
        result = 31 * result + (rating?.hashCode() ?: 0)
        result = 31 * result + (placeId?.hashCode() ?: 0)
        result = 31 * result + (priceLevel ?: 0)
        result = 31 * result + types.hashCode()
        result = 31 * result + (vicinity?.hashCode() ?: 0)
        result = 31 * result + isFull.hashCode()
        result = 31 * result + (promotions?.hashCode() ?: 0)
        return result
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

class Geometry() : Parcelable {
    var location: Location? = null

    constructor(parcel: Parcel) : this() {

    }

    constructor(location: Location) : this() {
        this.location = location
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Geometry> {
        override fun createFromParcel(parcel: Parcel): Geometry {
            return Geometry(parcel)
        }

        override fun newArray(size: Int): Array<Geometry?> {
            return arrayOfNulls(size)
        }
    }
}

class Location() : Parcelable {
    var lat: Double = 0.0
    var lng: Double = 0.0

    constructor(parcel: Parcel) : this() {
        lat = parcel.readDouble()
        lng = parcel.readDouble()
    }

    constructor(latLng: LatLng) : this() {
        lat = latLng.latitude
        lng = latLng.longitude
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }
}

class OpeningHours() : Parcelable{
    @SerializedName("open_now")
    var openNow: Boolean = false

    constructor(parcel: Parcel) : this() {
        openNow = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (openNow) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OpeningHours> {
        override fun createFromParcel(parcel: Parcel): OpeningHours {
            return OpeningHours(parcel)
        }

        override fun newArray(size: Int): Array<OpeningHours?> {
            return arrayOfNulls(size)
        }
    }
}

class Photo() : Parcelable{
    @SerializedName("photo_reference")
    var photoReference: String = ""
    @SerializedName("html_attributions")
    var htmlAttributions: List<String> = ArrayList()

    constructor(parcel: Parcel) : this() {
        photoReference = parcel.readString()
        htmlAttributions = parcel.createStringArrayList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(photoReference)
        parcel.writeStringList(htmlAttributions)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }

}