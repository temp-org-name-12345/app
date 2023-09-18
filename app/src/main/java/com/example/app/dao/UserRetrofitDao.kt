package com.example.app.dao

import com.example.app.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserRetrofitDao {
    @GET("/check")
    suspend fun checkUserByEmail(@Query("email") encodedEmail: String) : Response<Boolean>

    @POST("/")
    suspend fun saveUser(@Body user: User) : Response<User>

    @GET("/")
    suspend fun getAllUser() : Response<List<User>>
}