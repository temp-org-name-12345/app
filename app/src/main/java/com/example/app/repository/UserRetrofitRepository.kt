package com.example.app.repository

import android.util.Log
import com.example.app.dao.UserRetrofitDao
import com.example.app.model.User
import retrofit2.Response

class UserRetrofitRepository {
    companion object {
        private const val TAG = "UserRetrofitRepository"
        private val userRetrofitDao = UserRetrofitDao.userRetrofitDao

        private fun logging(methodName: String, it: Any?) {
            Log.e(TAG, "$methodName : $it")
        }
    }

    suspend fun saveUser(user: User) : Response<User> =
        userRetrofitDao
            .saveUser(user)
            .also { logging("saveUser()", it) }


    suspend fun getUserByKeyHash(keyHash: String) : Response<User> =
        userRetrofitDao
            .getUserByKeyHash(keyHash)
            .also { logging("getUserByKeyHash()", it) }

}