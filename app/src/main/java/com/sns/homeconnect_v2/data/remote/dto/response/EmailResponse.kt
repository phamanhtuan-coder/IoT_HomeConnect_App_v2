package com.sns.homeconnect_v2.data.remote.dto.response

data class EmailResponse(
    val success: Boolean,
    val exists: Boolean?,
    val message: String
)