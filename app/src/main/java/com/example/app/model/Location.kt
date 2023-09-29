package com.example.app.model

import com.google.gson.annotations.SerializedName

data class Location(
    val meta: Meta,
    val documents: List<Document>
)

data class Meta(
    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("pageable_count")
    val pageableCount: Int,

    @SerializedName("is_end")
    val isEnd: Boolean
)

data class Document(
    @SerializedName("address_name")
    val addressName: String,

    @SerializedName("address_type")
    val addressType: String,

    @SerializedName("x")
    val lat: String,

    @SerializedName("y")
    val lng: String,

    val address: Address?,

    @SerializedName("road_address")
    val roadAddress: RoadAddress?
)

/* 지번 주소 상세 정보 */
data class Address(
    @SerializedName("address_name")
    val addressName: String,

    @SerializedName("x")
    val lat: String,

    @SerializedName("y")
    val lng: String
)

/* 도로명 주소 상세 정보 */
data class RoadAddress(
    @SerializedName("address_name")
    val addressName: String,

    @SerializedName("building_name")
    val buildingName: String,

    @SerializedName("main_building_no")
    val mainBuildingNo: String,

    @SerializedName("x")
    val lat: String,

    @SerializedName("y")
    val lng: String,
) {
    val fullAddress: String
        get() = "$addressName $buildingName"
}