package com.hexa.arti.repository

import android.util.Log
import com.google.gson.Gson
import com.hexa.arti.data.model.profile.ChangePass
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.data.model.response.ErrorResponse
import com.hexa.arti.network.UserAPI
import okhttp3.ResponseBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userAPI: UserAPI
) : UserRepository{
    override suspend fun postChangeNickname(nickName: String): Result<ResponseBody> {
        val result = userAPI.changeNickname(Pair("nickName",nickName))

        Log.d("확인", "postChangePass: $result")
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

    }

    override suspend fun postChangePass(
        currentPassword: String,
        newPassword: String,
        confirmationPassword: String
    ): Result<ResponseBody> {
        val result = userAPI.changePass(
            ChangePass(
            currentPassword,newPassword,confirmationPassword
        )
        )
        Log.d("확인", "postChangePass: $result")
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
    }
}