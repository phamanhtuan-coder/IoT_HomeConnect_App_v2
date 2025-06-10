package com.sns.homeconnect_v2.data.remote.dto.response

data class CheckEmailResponse(
    val exists: Boolean,
    val isVerified: Boolean,
    val message: String
)
