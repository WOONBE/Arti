package com.hexa.arti.network

import com.hexa.arti.data.model.artmuseum.ArtGalleryResponse
import com.hexa.arti.data.model.artmuseum.ThemeArtworksResponse
import com.hexa.arti.data.model.artmuseum.ThemeResponse
import com.hexa.arti.data.model.artmuseum.UpdateGalleryDto
import com.hexa.arti.data.model.response.GetRandomGalleriesResponse
import com.hexa.arti.data.model.response.GetRandomGenreArtWorkResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GalleryApi {

    @GET("galleries/random")
    suspend fun getRandomGalleries(): Response<List<GetRandomGalleriesResponse>>

    @GET("galleries/artworks/random")
    suspend fun getRandomGenreArtworks(@Query("genreLabel") genreLabel: String): Response<List<GetRandomGenreArtWorkResponse>>

    @GET("galleries/{galleryId}")
    suspend fun getGalley(@Path("galleryId") galleryId: Int): Response<ArtGalleryResponse>

    @GET("galleries/{galleryId}/themes")
    suspend fun getGalleryTheme(@Path("galleryId") galleryId: Int): Response<ThemeResponse>

    @GET("galleries/{themeId}/artworks")
    suspend fun getGalleryThemeArtwork(@Path("themeId") themeId: Int): Response<ThemeArtworksResponse>

    @POST("galleries/{galleryId}/themes")
    suspend fun postGalleryTheme(@Path("galleryId") galleryId: Int): Response<ResponseBody>

    @PUT("galleries/{galleryId}")
    suspend fun updateMyGallery(
        @Path("galleryId") galleryId: Int,
        @Body updateGalleryDto: UpdateGalleryDto
    ): Response<ResponseBody>

    @PUT("galleries/themes/{themeId")
    suspend fun updateMyGalleryTheme(@Path("themeId") themeId: Int): Response<ResponseBody>


    @DELETE("/galleries/themes/{themeId}/artworks/{artworkId}")
    suspend fun deleteThemeArtwork(
        @Path("themeId") themeId: Int,
        @Path("artworkId") artworkId: Int
    ): Response<ResponseBody>
}