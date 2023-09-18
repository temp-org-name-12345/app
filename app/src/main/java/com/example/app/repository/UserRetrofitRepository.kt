package com.example.app.repository

import com.example.app.User
import com.example.app.util.RetrofitClient
import retrofit2.Response
import java.net.URLEncoder

class UserRetrofitRepository {
    companion object {
        private const val DEFAULT_ENCODE_TYPE = "UTF-8"
        private val userRetrofitDao = RetrofitClient.userRetrofitDao
    }

    suspend fun checkUserByEmail(email: String) : Response<Boolean> {
        val encodedEmail = URLEncoder.encode(email, DEFAULT_ENCODE_TYPE)
        return userRetrofitDao.checkUserByEmail(encodedEmail)
    }

    suspend fun saveUser(user: User) : Response<User> {
        return userRetrofitDao.saveUser(user)
    }

    suspend fun getAllUser() : Response<List<User>> {
        return userRetrofitDao.getAllUser()
    }
}