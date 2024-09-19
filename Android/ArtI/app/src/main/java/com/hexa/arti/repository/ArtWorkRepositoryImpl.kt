package com.hexa.arti.repository

import com.google.gson.Gson
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.data.model.response.ErrorResponse
import com.hexa.arti.data.model.response.GetArtWorkResponse
import com.hexa.arti.network.ArtWorkApi
import javax.inject.Inject

class ArtWorkRepositoryImpl @Inject constructor(
    private val artWorkApi: ArtWorkApi
) : ArtWorkRepository {
    override suspend fun getArtWork(artworkId: Int): Result<GetArtWorkResponse> {
        val result = artWorkApi.getArtWork(artworkId)
        if (result.isSuccessful) {
            result.body()?.let {
                return Result.success(it)
            }
            return Result.failure(Exception())
        } else {
            val errorResponse =
                Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
            // return Result.failure(exception = Exception())
            return Result.failure(
                ApiException(
                    code = errorResponse.code,
                    message = errorResponse.message
                )
            )
        }
    }
}