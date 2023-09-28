package com.example.app.dao

import com.example.app.model.Location
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoMapDao {
    companion object {
        private const val domain = "https://dapi.kakao.com/v2/local/search/"

        private val retrofit : Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(domain)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val kakaoMapDao : KakaoMapDao by lazy {
            retrofit.create(KakaoMapDao::class.java)
        }
    }

    @GET("address")
    suspend fun searchLocation(
        @Header("Authorization") auth: String,
        @Query("query") query: String
    ) : Response<Location>
}