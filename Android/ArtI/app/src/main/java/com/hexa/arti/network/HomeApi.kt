package com.hexa.arti.network

import com.hexa.arti.data.model.home.GetRecommendGalleriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApi {

    @GET("fastapi/home/{user_id}")
    suspend fun getRecommendMuseum(@Path("user_id") userId:Int): Response<List<GetRecommendGalleriesResponse>>
}