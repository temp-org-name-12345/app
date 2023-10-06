package com.example.app.model

import java.io.Serializable

data class Res<T> (
    val code: String?,
    val message: String?,
    val status: Int,
    val data: T,
    val isLoading: Boolean = false
)

data class User(
    var id: Int? = null,
    val nickname: String,
    val profileUrl: String,
    val email: String,
    val keyHash: String
) : Serializable

data class AddLocationReq(
    val lat: Double?,
    val lng: Double?,
    val year: Int,
    val month: Int,
    val day: Int,
    val isSpecial: Boolean?,
    val addressName: String?,
    val storeName: String?,
    val userId: Int?,
)
