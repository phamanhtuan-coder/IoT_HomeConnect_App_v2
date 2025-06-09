package com.sns.homeconnect_v2.data.remote.dto.request

data class CreateHouseRequest(
    val groupId: Int,
    val house_name: String,
    val address: String,
    val icon_name: String,
    val icon_color: String
)