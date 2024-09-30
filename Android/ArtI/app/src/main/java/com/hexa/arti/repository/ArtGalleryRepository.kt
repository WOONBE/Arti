package com.hexa.arti.repository

import com.hexa.arti.data.model.artmuseum.ArtGalleryResponse
import com.hexa.arti.data.model.artmuseum.MyGalleryThemeItem
import com.hexa.arti.data.model.artmuseum.UpdateGalleryDto
import com.hexa.arti.data.model.artwork.Artwork
import okhttp3.ResponseBody

interface ArtGalleryRepository {

    suspend fun getRandomGenreArtworks(
        genreLabel: String,
    ): Result<List<Artwork>>

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

    suspend fun deleteThemeArtWork(
        themeId : Int,
        artworkId : Int
    ) : Result<ResponseBody>

}