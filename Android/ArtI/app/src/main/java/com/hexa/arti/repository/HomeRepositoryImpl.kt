package com.hexa.arti.repository

import android.util.Log
import com.google.gson.Gson
import com.hexa.arti.data.model.home.GetRecommendGalleriesResponse
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.data.model.response.ErrorResponse
import com.hexa.arti.network.HomeApi
import javax.inject.Inject


class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi
) : HomeRepository {
    override suspend fun getRecommendGalleries(userId: Int): Result<List<GetRecommendGalleriesResponse>> {
        try {
            val result = homeApi.getRecommendMuseum(userId)
            Log.d("확인","성공 ${result.body()}")
            if (result.isSuccessful) {
                result.body()?.let {
                    return Result.success(it)
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
        } catch(e: Exception){
            return Result.failure(
                ApiException(
                    code = 0,
                    message = "서버 닫힘"
                )
            )
        }
    }
}