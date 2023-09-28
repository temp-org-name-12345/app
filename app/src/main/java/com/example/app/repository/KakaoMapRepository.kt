package com.example.app.repository

import android.util.Log
import com.example.app.dao.KakaoMapDao
import com.example.app.model.Location
import retrofit2.Response

class KakaoMapRepository {
    companion object {
        private val kakaoMapDao = KakaoMapDao.kakaoMapDao

        private const val TAG = "KakaoMapRepository"
        private const val DEFAULT_URL_ENCODE = "UTF-8"

        private fun logging(methodName: String, it: Any?) {
            Log.e(TAG, "$methodName : $it")
        }
    }

    suspend fun searchLocation(key: String, query: String) : Response<Location> {
        val auth = "KakaoAK $key"

        return kakaoMapDao
            .searchLocation(auth, query)
            .also { logging("searchLocation", it.body()) }
            .also { logging("searchLocation", auth) }
    }
}