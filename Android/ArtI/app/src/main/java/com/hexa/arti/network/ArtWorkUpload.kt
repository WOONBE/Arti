package com.hexa.arti.network

import com.hexa.arti.data.model.artworkupload.ArtWorkUploadDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ArtWorkUpload {
    @Multipart
    @POST("artwork/ai")
    suspend fun makeAIImage(
        @Part contentImage: MultipartBody.Part,
        @Part("style_image") styleImage: RequestBody
    ): Response<ArtWorkUploadDto>


    @GET("artwork/ai/show")
    suspend fun getImage(
        @Query("image_path") imagePath: String
    ): Response<ResponseBody>
}