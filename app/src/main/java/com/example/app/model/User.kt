package com.example.app.model



data class User(
    var id: Int? = null,
    val nickname: String,
    val profileUrl: String,
    val email: String,
    val keyHash: String
)

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
