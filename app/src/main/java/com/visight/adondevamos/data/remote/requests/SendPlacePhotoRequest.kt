package com.visight.adondevamos.data.remote.requests

import com.google.gson.annotations.SerializedName

class SendPlacePhotoRequest() {
    @SerializedName("place_id")
    var placeId: String? = null
    var imagenbase64: String? = null
    var encuesta: String? = null
    var capacity: Int? = null

    constructor(placeId: String, imagenbase64: String, encuesta: String?, capacity: Int?): this(){
        this.placeId = placeId
        this.imagenbase64 = imagenbase64
        this.encuesta = encuesta
        this.capacity = capacity
    }
}