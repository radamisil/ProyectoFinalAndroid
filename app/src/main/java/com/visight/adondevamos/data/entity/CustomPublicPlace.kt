package com.visight.adondevamos.data.entity

import android.os.Parcel
import android.os.Parcelable

class CustomPublicPlace(var IdDatos_comercio: Int? = null,
                        var direccion: String? = null,
                        var telefono: String? = null,
                        var capacidad: Int? = null,
                        var nombre: String? = null,
                        var idTipo_comercio: Int? = null,
                        var placeId: String? = null,
                        var placeName: String? = null,
                        var placeIcon: String? = null,
                        var placeFormatted_addres: String? = null,
                        var placeLatitude: String? = null,
                        var placeLongitude: String? = null,
                        var placePhoto_reference: String? = null,
                        var placePlace_id: String? = null,
                        var placeTypes: String? = null,
                        var placeVicinity: String? = null,
                        var placeRating: String? = null,
                        var idUsuario: Int? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(IdDatos_comercio)
        parcel.writeString(direccion)
        parcel.writeString(telefono)
        parcel.writeValue(capacidad)
        parcel.writeString(nombre)
        parcel.writeValue(idTipo_comercio)
        parcel.writeString(placeId)
        parcel.writeString(placeName)
        parcel.writeString(placeIcon)
        parcel.writeString(placeFormatted_addres)
        parcel.writeString(placeLatitude)
        parcel.writeString(placeLongitude)
        parcel.writeString(placePhoto_reference)
        parcel.writeString(placePlace_id)
        parcel.writeString(placeTypes)
        parcel.writeString(placeVicinity)
        parcel.writeString(placeRating)
        parcel.writeValue(idUsuario)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomPublicPlace> {
        override fun createFromParcel(parcel: Parcel): CustomPublicPlace {
            return CustomPublicPlace(parcel)
        }

        override fun newArray(size: Int): Array<CustomPublicPlace?> {
            return arrayOfNulls(size)
        }
    }

}