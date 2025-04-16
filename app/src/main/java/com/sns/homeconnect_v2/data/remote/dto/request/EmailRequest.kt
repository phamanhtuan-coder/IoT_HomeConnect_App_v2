package com.sns.homeconnect_v2.data.remote.dto.request

data class EmailRequest(
    val email: String,
    val otp: String? = null
)