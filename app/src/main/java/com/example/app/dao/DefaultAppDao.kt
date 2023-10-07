package com.example.app.dao

import retrofit2.Response
import retrofit2.http.GET

interface DefaultAppDao {
    @GET("default/thumbnail")
    suspend fun getAppThumbnail() : Response<String>

    @GET("default/preview")
    suspend fun getAppPreview() : Response<List<String>>
}