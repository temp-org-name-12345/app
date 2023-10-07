package com.example.app.dao

import retrofit2.Response
import retrofit2.http.POST

interface DefaultAppDao {
    @POST("thumbnail")
    suspend fun getAppThumbnail() : Response<List<String>>
}