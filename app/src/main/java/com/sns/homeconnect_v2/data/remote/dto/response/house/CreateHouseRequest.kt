package com.sns.homeconnect_v2.data.remote.dto.response.house

data class CreateHouseRequest(
    val name: String,
    val address: String,
    val iconName: String,
    val iconColor: String
)