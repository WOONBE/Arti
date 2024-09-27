package com.hexa.arti.repository

import com.hexa.arti.data.model.artmuseum.ArtGalleryResponse
import com.hexa.arti.data.model.artmuseum.MyGalleryThemeItem
import com.hexa.arti.data.model.artmuseum.UpdateGalleryDto
import okhttp3.ResponseBody

interface ArtGalleryRepository {
    suspend fun getArtGallery(
        galleryId : Int
    ) : Result<ArtGalleryResponse>

    suspend fun getArtGalleryThemes(
        galleryId : Int
    )  : Result<List<MyGalleryThemeItem>>

    suspend fun updateArtGallery(
        galleryId : Int,
        updateGalleryDto: UpdateGalleryDto
    ) : Result<ResponseBody>
}