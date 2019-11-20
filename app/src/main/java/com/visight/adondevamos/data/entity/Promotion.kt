package com.visight.adondevamos.data.entity

import android.os.Parcel
import android.os.Parcelable

class Promotion() : Parcelable {
    var id: Int? = null
    var description: String? = null
    var precio: Double? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        description = parcel.readString()
        precio = parcel.readValue(Double::class.java.classLoader) as? Double
    }

    constructor(description: String?, precio: Double?) : this() {
        this.description = description
        this.precio = precio
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(description)
        parcel.writeValue(precio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Promotion> {
        override fun createFromParcel(parcel: Parcel): Promotion {
            return Promotion(parcel)
        }

        override fun newArray(size: Int): Array<Promotion?> {
            return arrayOfNulls(size)
        }
    }
}