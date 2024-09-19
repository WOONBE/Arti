package com.hexa.arti.repository

import com.hexa.arti.data.model.response.GetArtWorkResponse
import kotlinx.coroutines.flow.Flow

interface ArtWorkRepository {
    suspend fun getArtWork(artworkId: Int) : Result<GetArtWorkResponse>
}