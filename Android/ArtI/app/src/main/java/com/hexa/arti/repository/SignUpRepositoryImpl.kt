package com.hexa.arti.repository

import android.util.Log
import com.google.gson.Gson
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.data.model.response.ErrorResponse
import com.hexa.arti.data.model.signup.SignUpModel
import com.hexa.arti.network.SignUpApi
import okhttp3.ResponseBody
import javax.inject.Inject

private const val TAG = "SignUpRepositoryImpl"
class SignUpRepositoryImpl @Inject constructor(
   private val signUpApi: SignUpApi
): SignUpRepository {
    override suspend fun postSignUp(signUpModel: SignUpModel) : Result<ResponseBody> {
        val result = signUpApi.signUp(signUpModel)
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
}