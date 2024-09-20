package com.hexa.arti.repository

import com.hexa.arti.data.model.signup.SignUpModel
import okhttp3.ResponseBody

interface SignUpRepository {
    suspend fun postSignUp(signUpModel: SignUpModel) : Result<ResponseBody>
}