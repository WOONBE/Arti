package com.hexa.arti.repository

import com.hexa.arti.data.UserListModel

interface TestRepository {

    suspend fun getUser(page: String): Result<UserListModel?>
}