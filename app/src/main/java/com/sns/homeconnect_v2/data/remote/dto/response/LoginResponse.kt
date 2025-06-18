package com.sns.homeconnect_v2.data.remote.dto.response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val deviceUuid: String,
    val userId: String,
)
