package com.hexa.arti.data.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val expiresIn: Int,
    @SerializedName("token")
    val token: String
)