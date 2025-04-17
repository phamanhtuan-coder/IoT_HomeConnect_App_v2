package com.sns.homeconnect_v2.data.remote.dto.request

data class NewPasswordRequest(
    val email: String,
    val newPassword: String
)