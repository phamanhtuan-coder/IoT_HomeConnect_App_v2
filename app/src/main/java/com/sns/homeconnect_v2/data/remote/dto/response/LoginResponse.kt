package com.sns.homeconnect_v2.data.remote.dto.response

data class LoginResponse(
    val token: String,
    val user: User
)