package com.sns.homeconnect_v2.data.remote.dto.response.house

data class CreateHouseResponse(
    val message: String,
    val house: HouseDetail1,
    val space: DefaultSpace
)

data class HouseDetail1(
    val houseId: Int,
    val userId: Int,
    val name: String,
    val address: String,
    val iconName: String,
    val iconColor: String,
    val createdAt: String,
    val updatedAt: String
)


data class DefaultSpace(
    val spaceID: Int,
    val name: String,
    val isDeleted: Boolean,
    val createdAt: String,
    val updatedAt: String
)