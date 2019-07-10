package com.visight.adondevamos.data.remote.requests

import com.google.gson.annotations.SerializedName

class RegisterRequest(_nombre: String, _apellido: String, _email: String,
                      _password: String, _tipo: Int) {
    @SerializedName("Nombre")
    var nombre: String? = null
    @SerializedName("Apellido")
    var apellido: String? = null
    @SerializedName("Email")
    var email: String? = null
    @SerializedName("Password")
    var password: String? = null
    @SerializedName("Tipo")
    var tipo: Integer? = null
}