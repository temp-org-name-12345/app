package com.example.app.dao

import com.example.app.model.Location
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoMapDao {
    @GET("address")
    suspend fun searchLocation(
        @Header("Authorization") auth: String,
        @Query("query") query: String
    ) : Response<Location>
}