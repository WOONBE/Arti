package com.hexa.arti.network

import com.hexa.arti.data.UserListModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TestApi {
    @GET("api/users")
    suspend fun getUser(@Query("page") page: String): Response<UserListModel>
}