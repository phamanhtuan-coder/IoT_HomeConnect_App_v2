package com.sns.homeconnect_v2.data.remote.dto.response

import com.sns.homeconnect_v2.data.remote.dto.request.RegisterRequest


data class RegisterResponse(
    val message: String,
    val user: RegisterRequest,
    val error: String
)