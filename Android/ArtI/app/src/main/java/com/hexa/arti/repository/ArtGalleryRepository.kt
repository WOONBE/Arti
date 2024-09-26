package com.hexa.arti.repository

import com.hexa.arti.data.model.artmuseum.ArtGalleryResponse
import com.hexa.arti.data.model.artmuseum.MyGalleryThemeItem

interface ArtGalleryRepository {
    suspend fun getArtGallery(
        galleryId : Int
    ) : Result<ArtGalleryResponse>

    suspend fun getArtGalleryThemes(
        galleryId : Int
    )  : Result<List<MyGalleryThemeItem>>

}