package com.visight.adondevamos.data.entity

import android.os.Parcel
import android.os.Parcelable

class PieChartItem(var idPlace: String? = null,
                   var fecha: String? = null,
                   var altaporcent: Int? = null,
                   var mediaporcent: Int? = null,
                   var bajaporcent: Int? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idPlace)
        parcel.writeString(fecha)
        parcel.writeValue(altaporcent)
        parcel.writeValue(mediaporcent)
        parcel.writeValue(bajaporcent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PieChartItem> {
        override fun createFromParcel(parcel: Parcel): PieChartItem {
            return PieChartItem(parcel)
        }

        override fun newArray(size: Int): Array<PieChartItem?> {
            return arrayOfNulls(size)
        }
    }

}