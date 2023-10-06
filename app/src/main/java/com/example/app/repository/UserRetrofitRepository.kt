package com.example.app.repository

import android.util.Log
import com.example.app.dao.ImageUploadRes
import com.example.app.dao.UserRetrofitDao
import com.example.app.model.AddLocationReq
import com.example.app.model.User
import okhttp3.MultipartBody
import retrofit2.Response

object UserRetrofitRepository {

    private const val TAG = "UserRetrofitRepository"
    private val userRetrofitDao = UserRetrofitDao.userRetrofitDao

    private fun logging(methodName: String, it: Any?) {
        Log.e(TAG, "$methodName : $it")
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

    suspend fun uploadNewLocation(
        images: List<MultipartBody.Part?>,
        req: AddLocationReq
    ) : Response<ImageUploadRes> =

        images
            .filterNotNull()
            .let { userRetrofitDao.uploadNewLocation(images = it, req = req) }
            .also { logging("addLocationReq", it.body()) }
}