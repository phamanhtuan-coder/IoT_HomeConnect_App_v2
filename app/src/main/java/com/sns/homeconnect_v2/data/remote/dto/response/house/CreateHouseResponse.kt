package com.sns.homeconnect_v2.data.remote.dto.response.house

data class CreateHouseResponse(
    val message: String,
    val house: House,
    val space: DefaultSpace
)

data class House(
    val house_id: Int,
    val group_id: Int,
    val house_name: String,
    val address: String,
    val icon_name: String,
    val icon_color: String,
    val created_at: String,
    val updated_at: String,
    val is_deleted: Boolean?
)

data class HouseDetail1(
    val houseID: Int,
    val groupID: Int,
    val houseName: String,
    val address: String,
    val iconName: String,
    val iconColor: String,
    val createdAt: String,
    val updatedAt: String,
    val isDeleted: Boolean?
)

data class DefaultSpace(
    val spaceID: Int,
    val name: String,
    val isDeleted: Boolean,
    val createdAt: String,
    val updatedAt: String
)