package com.example.app.model

import android.graphics.Bitmap
import java.time.LocalDateTime


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
    val visitDate: LocalDateTime?,
    val isSpecial: Boolean?,
    val name: String?,
    val userId: Int?,
)
