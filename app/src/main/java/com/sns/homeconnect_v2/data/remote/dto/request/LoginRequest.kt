package com.sns.homeconnect_v2.data.remote.dto.request

data class LoginRequest(
    val Email: String,
    val PasswordHash: String
)