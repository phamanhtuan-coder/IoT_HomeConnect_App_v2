package com.sns.homeconnect_v2.data.remote.dto.response

data class UpdateGroupResponse(
    val group_id: Int,
    val group_name: String,
    val created_at: String,
    val updated_at: String,
    val is_deleted: Boolean
)
