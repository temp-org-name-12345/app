package com.example.app.dao

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    private const val defaultDomain = "http://15.164.24.152:8080/api/v1/"
    private const val kakaoApiDomain = "https://dapi.kakao.com/v2/local/search/"

    private val retrofit : Retrofit by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(defaultDomain)
            .addConverterFactory(ScalarsConverterFactory.create())  // String 응답처리 Converter
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val retrofitForKakao : Retrofit by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(kakaoApiDomain)
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
        retrofitForKakao.create(KakaoMapDao::class.java)
    }
}