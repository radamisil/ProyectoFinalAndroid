package com.visight.adondevamos.data.remote.responses

import com.google.gson.annotations.SerializedName

class AnalizedPhotoResponse {
    @SerializedName("cantidad", alternate = ["person"])
    var people: Int? = null
}