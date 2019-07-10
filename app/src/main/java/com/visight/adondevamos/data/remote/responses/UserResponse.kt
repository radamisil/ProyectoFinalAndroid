package com.visight.adondevamos.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.visight.adondevamos.data.entity.User

class UserResponse {
    @SerializedName("Data")
    var data: List<User>? = null
}