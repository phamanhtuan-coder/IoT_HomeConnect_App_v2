package com.sns.homeconnect_v2.data.remote.dto.response.house

data class HouseDetail(
    val houseID: Int,
    val userID: Int,
    val name: String,
    val address: String,
    val iconName: String,
    val iconColor: String,
    val isDeleted: Int,
    val createdAt: String,
    val updatedAt: String
)