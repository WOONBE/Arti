package com.hexa.arti.repository

import com.hexa.arti.data.model.response.GetArtistByStringResponse

interface ArtistRepository {
    suspend fun getArtist(keyword: String): Result<List<GetArtistByStringResponse>>
}