package com.visight.adondevamos.data.remote.responses

import com.visight.adondevamos.data.entity.PublicPlace

class GetPublicPlacesResponse {
    var results: List<PublicPlace> = ArrayList()
    var status: String = ""
}