package com.example.app.dao

import com.example.app.model.AddLocationReq
import com.example.app.model.ImageUploadRes
import com.example.app.model.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface UserRetrofitDao {
    @GET("user/info")
    suspend fun getUserByKeyHash(@Query("keyHash") keyHash: String) : Response<User>

    @POST("user/save")
    suspend fun saveUser(@Body user: User) : Response<User>

    @Multipart
    @POST("user/upload/new")
    suspend fun uploadNewLocation(
        @Part images: List<MultipartBody.Part>,
        @Part("req") req: AddLocationReq
    ) : Response<ImageUploadRes>
}

