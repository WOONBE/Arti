package com.hexa.arti.network

import com.hexa.arti.data.model.response.PostSubscribeResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface MemberApi {

    @POST("members/{memberId}/subscribe/{galleryId}")
    suspend fun postSubscribe(
        @Path("memberId") memberId: Int,
        @Path("galleryId") galleryId: Int,
    ): Response<PostSubscribeResponse>

    @POST("members/{memberId}/subscribe/{galleryId}")
    suspend fun postUnsubscribe(
        @Path("memberId") memberId: Int,
        @Path("galleryId") galleryId: Int,
    ): Response<PostSubscribeResponse>

}