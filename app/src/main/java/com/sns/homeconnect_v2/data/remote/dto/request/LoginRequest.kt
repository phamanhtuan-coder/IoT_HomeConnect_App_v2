package com.sns.homeconnect_v2.data.remote.dto.request

data class LoginRequest(
    val username: String,
    val password: String,
    val rememberMe: Boolean,
    val deviceName: String,
    val deviceId: String,
    val deviceUuid: String?
)
