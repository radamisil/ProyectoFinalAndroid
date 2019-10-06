package com.visight.adondevamos.data.remote.requests

import com.google.gson.annotations.SerializedName


class RegisterRequest(@SerializedName("Nombre") private val nombre :  String,
                      @SerializedName("Apellido") private val apellido: String,
                      @SerializedName("Email") private val email: String,
                      @SerializedName("Password") private val password: String,
                      @SerializedName("Tipo") private val tipo: Int)
