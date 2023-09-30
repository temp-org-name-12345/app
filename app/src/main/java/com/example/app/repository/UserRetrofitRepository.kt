package com.example.app.repository

import android.util.Log
import com.example.app.dao.UserRetrofitDao
import com.example.app.model.AddLocationReq
import com.example.app.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    suspend fun addLocationReq(images: List<MultipartBody.Part?>, req: RequestBody) : Response<Unit> =
        userRetrofitDao
            .uploadLocation(images, req)
            .also { logging("addLocationReq", req) }

    /*
    suspend fun uploadTest(image: MultipartBody.Part) =
        userRetrofitDao
            .uploadLocation(image)
            .also { logging("uploadTest", image) }

     */
}