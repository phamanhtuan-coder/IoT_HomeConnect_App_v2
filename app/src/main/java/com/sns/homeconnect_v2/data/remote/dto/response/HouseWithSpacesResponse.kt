package com.sns.homeconnect_v2.data.remote.dto.response

data class HouseWithSpacesResponse(
    val house_id: Int,
    val group_id: Int,
    val address: String,
    val icon_name: String,
    val icon_color: String,
    val created_at: String,
    val updated_at: String,
    val is_deleted: Boolean?,
    val house_name: String,
    val spaces: List<SpaceResponse>
)
