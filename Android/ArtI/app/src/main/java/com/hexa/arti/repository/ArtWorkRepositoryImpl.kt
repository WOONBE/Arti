package com.hexa.arti.repository

import android.util.Log
import com.google.gson.Gson
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.data.model.response.ErrorResponse
import com.hexa.arti.data.model.response.GetArtWorkResponse
import com.hexa.arti.network.ArtWorkApi
import javax.inject.Inject

class ArtWorkRepositoryIml @Inject constructor(private val api: ArtWorkApi) : ArtWorkRepository {
    override suspend fun getArtWork(artworkId: Int): Result<GetArtWorkResponse> {
        val result = api.getArtWork(artworkId)
        if (result.isSuccessful) {
            result.body()?.let {
                return Result.success(it)
            }
            return Result.failure(Exception())
        } else {
            val errorCode = result.code()
            val errorMessage = result.message()
            val errorBody = result.errorBody()
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