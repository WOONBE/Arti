package com.hexa.arti.repository

import com.hexa.arti.data.model.artwork.Artwork

interface ArtWorkRepository {
    suspend fun getArtWorkById(artworkId: Int): Result<Artwork>

    suspend fun getArtWorksByString(keyword: String): Result<List<Artwork>>
}