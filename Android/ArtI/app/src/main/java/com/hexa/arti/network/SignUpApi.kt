package com.hexa.arti.network

import com.hexa.arti.data.model.signup.SignUpModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {
    @POST("auth/register")
    suspend fun signUp(@Body signUpModel: SignUpModel) : Response<ResponseBody>
}