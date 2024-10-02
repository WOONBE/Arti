package com.hexa.arti.repository

import com.hexa.arti.data.model.home.GetRecommendGalleriesResponse

interface HomeRepository {

    suspend fun getRecommendGalleries(userId: Int): Result<List<GetRecommendGalleriesResponse>>
}