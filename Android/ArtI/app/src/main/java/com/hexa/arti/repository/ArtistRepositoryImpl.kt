package com.hexa.arti.repository

import android.util.Log
import com.google.gson.Gson
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.data.model.response.ErrorResponse
import com.hexa.arti.data.model.search.Artist
import com.hexa.arti.network.ArtistApi
import com.hexa.arti.util.asArtist
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val artistApi: ArtistApi
) : ArtistRepository {
    override suspend fun getArtist(keyword: String): Result<List<Artist>> {
        val result = artistApi.getArtistByString(keyword)
        if (result.isSuccessful) {
            result.body()?.let {
                return Result.success(it.map { it.asArtist() })
            }

            return Result.failure(Exception())
        } else {
            val errorResponse =
                Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
            Log.d("확인", "${result.errorBody()} ${result.code()}")
            return Result.failure(
                ApiException(
                    code = errorResponse.code,
                    message = errorResponse.message
                )
            )
        }
    }
}