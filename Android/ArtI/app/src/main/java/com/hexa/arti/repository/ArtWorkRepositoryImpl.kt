package com.hexa.arti.repository

import androidx.paging.PagingSource
import com.google.gson.Gson
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.data.model.response.ErrorResponse
import com.hexa.arti.network.ArtWorkApi
import com.hexa.arti.util.asArtwork
import javax.inject.Inject

class ArtWorkRepositoryImpl @Inject constructor(
    private val artWorkApi: ArtWorkApi
) : ArtWorkRepository {
    override suspend fun getArtWorkById(artworkId: Int): Result<Artwork> {
        val result = artWorkApi.getArtWorkById(artworkId)
        if (result.isSuccessful) {
            result.body()?.let {
                return Result.success(it.asArtwork())
            }
            return Result.failure(Exception())
        } else {
            val errorResponse =
                Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
            return Result.failure(
                ApiException(
                    code = errorResponse.code,
                    message = errorResponse.message
                )
            )
        }
    }

    override suspend fun getArtWorksByString(keyword: String): Result<List<Artwork>> {
        val result = artWorkApi.getArtWorksByString(keyword)
        if (result.isSuccessful) {
            result.body()?.let {
                return Result.success(it.map { artworkResponse ->  artworkResponse.asArtwork() })
            }
            return Result.failure(Exception())
        } else {
            val errorResponse =
                Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
            return Result.failure(
                ApiException(
                    code = errorResponse.code,
                    message = errorResponse.message
                )
            )
        }
    }

    override suspend fun getArtWorksByStringWithPaging(keyword: String): PagingSource<Int, Artwork> {
        TODO("Not yet implemented")
    }
}