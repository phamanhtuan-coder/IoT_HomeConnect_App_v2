package com.sns.homeconnect_v2.data.remote.dto.response

data class GroupResponse(
    val group_id: Int,
    val group_name: String,
    val icon_name: String?,
    val icon_color: String?,
    val group_description: String?,
    val created_at: String,
    val updated_at: String,
    val is_deleted: Boolean
)

