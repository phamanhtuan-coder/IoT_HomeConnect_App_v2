package com.sns.homeconnect_v2.data.remote.dto.response

data class MemberResponse(
    val user_group_id: Int,
    val account_id: String,
    val group_id: Int,
    val role: String,
    val joined_at: String,
    val username: String,
    val email: String,
    val full_name: String,
    val avatar: String?
)
