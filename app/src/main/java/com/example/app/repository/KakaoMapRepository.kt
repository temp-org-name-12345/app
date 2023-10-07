package com.example.app.repository

import android.util.Log
import com.example.app.dao.RetrofitClient

object KakaoMapRepository {
    private const val TAG = "KakaoMapRepository"
    private val kakaoMapDao = RetrofitClient.kakaoMapDao
    private fun logging(methodName: String, it: Any?) = Log.e(TAG, "$methodName : $it")

}