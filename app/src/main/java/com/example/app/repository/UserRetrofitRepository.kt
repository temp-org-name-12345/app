package com.example.app.repository

import android.util.Log
import com.example.app.dao.UserRetrofitDao
import com.example.app.model.AddLocationReq
import com.example.app.model.User
import okhttp3.MultipartBody
import retrofit2.Call
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

    suspend fun getAppThumbnail() : Response<List<String>> =
        userRetrofitDao
            .getAppThumbnail()
            .also { logging("getAppThumbnail()", it) }

    fun addLocationReq(
        images: List<MultipartBody.Part?>,
        req: AddLocationReq
    ) : Call<String> =

        images
            .filterNotNull()
            .let {
                Log.e("UserRetrofitRepository", "it = $it")

                userRetrofitDao.uploadLocation(
                    images = it,
                    req = req
                )
            }
            .also {
                logging("addLocationReq", it)
            }
}