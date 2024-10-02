package com.hexa.arti.network

import com.hexa.arti.data.model.artmuseum.ArtGalleryResponse
import com.hexa.arti.data.model.artmuseum.CreateThemeDto
import com.hexa.arti.data.model.artmuseum.GalleryRequest
import com.hexa.arti.data.model.artmuseum.GalleryResponse
import com.hexa.arti.data.model.artmuseum.ThemeArtworksResponse
import com.hexa.arti.data.model.artmuseum.ThemeResponse
import com.hexa.arti.data.model.artmuseum.ThemeResponseItem
import com.hexa.arti.data.model.artmuseum.UpdateGalleryDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface GalleryApi  {
    @GET("galleries/{galleryId}")
    suspend fun getGalley(@Path("galleryId") galleryId : Int ) : Response<ArtGalleryResponse>

    @GET("galleries/{galleryId}/themes")
    suspend fun getGalleryTheme(@Path("galleryId") galleryId: Int) : Response<ThemeResponse>

    @GET("galleries/{themeId}/artworks")
    suspend fun getGalleryThemeArtwork(@Path("themeId") themeId: Int) : Response<ThemeArtworksResponse>

    @Multipart
    @POST("galleries")
    suspend fun postGallery(@Part galleryRequest: MultipartBody.Part, // JSON 형식의 DTO
                            @Part image: MultipartBody.Part ) : Response<GalleryResponse>

    @POST("galleries/themes")
    suspend fun postGalleryTheme(@Body themeDto: CreateThemeDto) : Response<ThemeResponseItem>

    @POST("galleries/themes/{themeId}/artworks/{artworkId}")
    suspend fun postArtworkTheme(@Path("themeId") themeId: Int, @Path("artworkId") artworkId: Int, @Query("description") description: String) : Response<ResponseBody>

    @PUT("galleries/{galleryId}")
    suspend fun updateMyGallery(@Path("galleryId") galleryId: Int, @Body updateGalleryDto : UpdateGalleryDto) : Response<ResponseBody>

    @PUT("galleries/themes/{themeId")
    suspend fun updateMyGalleryTheme(@Path("themeId") themeId: Int): Response<ResponseBody>


    @DELETE("galleries/{galleryId}/themes/{themeId}")
    suspend fun deleteTheme(@Path("galleryId") galleryId: Int, @Path("themeId") themeId: Int) : Response<ResponseBody>

    @DELETE("/galleries/themes/{themeId}/artworks/{artworkId}")
    suspend fun deleteThemeArtwork(@Path("themeId") themeId: Int, @Path("artworkId") artworkId : Int) : Response<ResponseBody>
 }