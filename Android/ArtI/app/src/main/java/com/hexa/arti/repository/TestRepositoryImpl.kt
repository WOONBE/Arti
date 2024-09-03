package com.hexa.arti.repository

import com.hexa.arti.data.UserListModel
import com.hexa.arti.network.TestApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRepositoryImpl @Inject constructor(
    private val testApi: TestApi
) : TestRepository {

    override suspend fun getUser(page: String): Result<UserListModel?> {
        val response = testApi.getUser(page)

        println("확인 여까지 오나 ?${response}")
        return if(response.isSuccessful){
            Result.success(response.body())
        } else {
            Result.failure(Exception())
        }
    }
}