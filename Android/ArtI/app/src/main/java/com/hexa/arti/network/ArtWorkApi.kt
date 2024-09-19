package com.hexa.arti.network

import com.hexa.arti.data.model.login.UserListModel
import com.hexa.arti.data.model.response.GetArtWorkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtWorkApi {
    @GET("artworks/{artworkId}")
    suspend fun getArtWork(@Path("artworkId") artworkId: Int): Response<GetArtWorkResponse>
}