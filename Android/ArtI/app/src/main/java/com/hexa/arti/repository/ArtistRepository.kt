package com.hexa.arti.repository

import com.hexa.arti.data.model.search.Artist

interface ArtistRepository {
    suspend fun getArtist(keyword: String): Result<List<Artist>>
}