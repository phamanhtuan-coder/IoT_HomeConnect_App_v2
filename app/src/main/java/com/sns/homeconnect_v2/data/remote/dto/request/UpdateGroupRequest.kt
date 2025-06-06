package com.sns.homeconnect_v2.data.remote.dto.request

data class UpdateGroupRequest(
    val group_name: String,
    val icon_name: String?,
    val icon_color: String?,
    val group_description: String?
)
