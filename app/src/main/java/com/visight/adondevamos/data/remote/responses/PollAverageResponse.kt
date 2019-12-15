package com.visight.adondevamos.data.remote.responses

import android.os.Parcel
import android.os.Parcelable

class PollAverageResponseData(var Data: List<PollAverageResponse>? = null)

class PollAverageResponse(var PromedioIA: String? = null, var PromedioEncuesta: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(PromedioIA)
        parcel.writeString(PromedioEncuesta)
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