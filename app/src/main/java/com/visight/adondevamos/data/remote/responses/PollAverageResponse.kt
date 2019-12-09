package com.visight.adondevamos.data.remote.responses

import android.os.Parcel
import android.os.Parcelable

class PollAverageResponse(var IA: Int? = null, var encuesta: Int? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(IA)
        parcel.writeValue(encuesta)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PollAverageResponse> {
        override fun createFromParcel(parcel: Parcel): PollAverageResponse {
            return PollAverageResponse(parcel)
        }

        override fun newArray(size: Int): Array<PollAverageResponse?> {
            return arrayOfNulls(size)
        }
    }
}