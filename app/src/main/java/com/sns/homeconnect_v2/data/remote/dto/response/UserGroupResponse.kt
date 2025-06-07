package com.sns.homeconnect_v2.data.remote.dto.response

data class UserGroupResponse(
    val user_group_id: Int,
    val account_id: String,
    val group_id: Int,
    val role: String,
    val joined_at: String,
    val updated_at: String,
    val is_deleted: Boolean
)
