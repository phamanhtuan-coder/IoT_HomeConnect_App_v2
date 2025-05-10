package com.sns.homeconnect_v2.data.remote.dto.response.house

data class UpdateHouseRequest(
    val name: String,
    val address: String,
    val iconName: String,
    val iconColor: String
)