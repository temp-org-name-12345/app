package com.example.app.model

import java.io.Serializable


data class User(
    var id: Int? = null,
    val nickname: String,
    val profileUrl: String,
    val email: String,
    val keyHash: String
) : Serializable
