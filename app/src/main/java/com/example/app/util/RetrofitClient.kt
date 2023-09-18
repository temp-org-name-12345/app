package com.example.app.util

import com.example.app.dao.UserRetrofitDao
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val host = "15.164.24.152"
    private const val port = "8080"
    private const val prefix = "api"
    private const val version = "v1"
    private const val domain = "http://$host:$port"

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(domain)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userRetrofitDao : UserRetrofitDao by lazy {
        retrofit.create(UserRetrofitDao::class.java)
    }
}