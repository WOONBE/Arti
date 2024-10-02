package com.hexa.arti.repository

import android.credentials.CredentialDescription
import com.hexa.arti.data.model.artmuseum.ArtGalleryResponse
import com.hexa.arti.data.model.artmuseum.CreateThemeDto
import com.hexa.arti.data.model.artmuseum.GalleryRequest
import com.hexa.arti.data.model.artmuseum.GalleryResponse
import com.hexa.arti.data.model.artmuseum.MyGalleryThemeItem
import com.hexa.arti.data.model.artmuseum.ThemeResponseItem
import com.hexa.arti.data.model.artmuseum.UpdateGalleryDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody

interface ArtGalleryRepository {
    suspend fun getArtGallery(
        galleryId : Int
    ) : Result<ArtGalleryResponse>

    suspend fun getArtGalleryThemes(
        galleryId : Int
    )  : Result<List<MyGalleryThemeItem>>

    suspend fun postGallery(
        image : MultipartBody.Part,
        galleryDto: GalleryRequest
    ) : Result<GalleryResponse>

    suspend fun postTheme(
        themeDto : CreateThemeDto
    ) : Result<ThemeResponseItem>

    suspend fun postArtworkTheme(
        themeId: Int,
        artworkId: Int,
        description: String
    ) : Result<ResponseBody>

    suspend fun updateArtGallery(
        galleryId : Int,
        updateGalleryDto: UpdateGalleryDto
    ) : Result<ResponseBody>

    suspend fun deleteTheme(
        galleryId: Int,
        themeId : Int
    ) : Result<ResponseBody>


    suspend fun deleteThemeArtWork(
        themeId : Int,
        artworkId : Int
    ) : Result<ResponseBody>

}