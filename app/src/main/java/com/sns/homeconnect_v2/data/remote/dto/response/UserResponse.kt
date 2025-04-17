package com.sns.homeconnect_v2.data.remote.dto.response

data class UserResponse(
    val message: String,
    val user: User,
    val error: String
)