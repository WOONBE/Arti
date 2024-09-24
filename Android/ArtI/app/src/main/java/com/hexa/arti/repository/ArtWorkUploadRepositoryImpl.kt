package com.hexa.arti.repository

import android.util.Log
import com.google.gson.Gson
import com.hexa.arti.data.model.artworkupload.ArtWorkAIUploadDto
import com.hexa.arti.data.model.artworkupload.ArtWorkUploadDto
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.data.model.response.ErrorResponse
import com.hexa.arti.network.ArtWorkUpload
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.Part
import javax.inject.Inject

private const val TAG = "ArtWorkUploadRepository"
class ArtWorkUploadRepositoryImpl @Inject constructor(
    private val artWorkUpload: ArtWorkUpload
) : ArtWorkUploadRepository
{
    override suspend fun postMakeAIImage(
       contentImage: MultipartBody.Part,
       styleImage: RequestBody
    ): Result<ArtWorkUploadDto> {
        val result = artWorkUpload.makeAIImage(contentImage,styleImage)

        Log.d(TAG, "postSignUp: ${result}")
        // 성공적인 응답 처리
        if (result.isSuccessful) {
            Log.d(TAG, "postSignUp: ${result.body()}")
            return result.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Response body is null"))
        }

        // 오류 응답 처리
        val errorBody = result.errorBody()?.string()
        val errorResponse = if (errorBody != null) {
            Gson().fromJson(errorBody, ErrorResponse::class.java)
        } else {
            ErrorResponse(code = result.code(), message = "Unknown error")
        }

        return Result.failure(
            ApiException(
                code = errorResponse.code,
                message = errorResponse.message
            )
        )
    }

    override suspend fun getImage(imagePath: String): Result<ResponseBody> {
        val result = artWorkUpload.getImage(imagePath)

        if (result.isSuccessful) {
            Log.d(TAG, "getImage: ${result.body()}")
            return result.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Response body is null"))
        }

        // 오류 응답 처리
        val errorBody = result.errorBody()?.string()
        val errorResponse = if (errorBody != null) {
            Gson().fromJson(errorBody, ErrorResponse::class.java)
        } else {
            ErrorResponse(code = result.code(), message = "Unknown error")
        }

        return Result.failure(
            ApiException(
                code = errorResponse.code,
                message = errorResponse.message
            )
        )
    }
}