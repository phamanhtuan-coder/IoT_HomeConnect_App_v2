package com.sns.homeconnect_v2.data.remote.dto.request

data class RecoveryPasswordRequest(
    val email: String,
    val newPassword: String
)