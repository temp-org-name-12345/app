package com.example.app.repository

import android.util.Log
import com.example.app.dao.RetrofitClient
import retrofit2.Response

object DefaultAppRepository {
    private const val TAG = "DefaultAppRepository"
    private val defaultAppDao = RetrofitClient.defaultAppDao
    private fun logging(methodName: String, it: Any?) = Log.e(TAG, "$methodName : $it")

    suspend fun getAppThumbnail() : Response<List<String>> =
       defaultAppDao
            .getAppThumbnail()
            .also { logging("getAppThumbnail()", it) }
}