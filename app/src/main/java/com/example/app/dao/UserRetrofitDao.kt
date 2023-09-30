package com.example.app.dao

import com.example.app.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserRetrofitDao {
    companion object {
        private const val host = "15.164.24.152"
        private const val port = "8080"
        private const val version = "api/v1"
        private const val prefix = "user"
        private const val domain = "http://$host:$port/$version/$prefix/"

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

    @GET("info")
    suspend fun getUserByKeyHash(@Query("keyHash") keyHash: String) : Response<User>

    @POST("save")
    suspend fun saveUser(@Body user: User) : Response<User>

    @POST("thumbnail")
    suspend fun getAppThumbnail() : Response<List<String>>

    @Multipart
    @POST("upload")
    suspend fun uploadLocation(
        @Part images: List<MultipartBody.Part?>,
        @Part("meta") metaData: RequestBody
    ) : Response<Unit>
}