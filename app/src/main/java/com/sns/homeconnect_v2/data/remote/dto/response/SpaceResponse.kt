package com.sns.homeconnect_v2.data.remote.dto.response

data class SpaceResponse(
    val space_id: Int,
    val house_id: Int,
    val created_at: String,
    val updated_at: String,
    val is_deleted: Boolean?,
    val space_name: String?,
    val icon_color: String?,
    val icon_name: String?,
    val space_description: String?,
    var isRevealed: Boolean = false
)