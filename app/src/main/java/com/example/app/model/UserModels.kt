package com.example.app.model

import java.io.Serializable


data class User(
    var id: Int? = null,
    val nickname: String,
    val profileUrl: String,
    val email: String,
    val keyHash: String
) : Serializable

/* 장소 업로드 요청에 대한 모델 */
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

/* 이미지 업로드 처리에 따른 서버 응답 */
data class ImageUploadRes(
    val imageUrls: List<String>,
    val data: AddLocationReq
)
