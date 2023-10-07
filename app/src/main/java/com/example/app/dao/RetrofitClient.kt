package com.example.app.dao

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    private const val host = "15.164.24.152"
    private const val port = "8080"
    private const val version = "api/v1"
    private const val prefix = "user"
    private const val domain = "http://$host:$port/$version/$prefix/"
    private const val domainMap = "https://dapi.kakao.com/v2/local/search/"

    private val retrofit : Retrofit by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(domain)
            .addConverterFactory(ScalarsConverterFactory.create())  // String 응답처리 Converter
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val retrofitMap : Retrofit by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(domainMap)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val userRetrofitDao : UserRetrofitDao by lazy {
        retrofit.create(UserRetrofitDao::class.java)
    }

    val defaultAppDao : DefaultAppDao by lazy {
        retrofit.create(DefaultAppDao::class.java)
    }

    val kakaoMapDao : KakaoMapDao by lazy {
        retrofitMap.create(KakaoMapDao::class.java)
    }
}