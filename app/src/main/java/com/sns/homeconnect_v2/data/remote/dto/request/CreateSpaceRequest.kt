package com.sns.homeconnect_v2.data.remote.dto.request

data class CreateSpaceRequest(
    val houseId: Int,
    val space_name: String,
    val icon_name: String,
    val icon_color: String,
    val space_description: String? = null
)