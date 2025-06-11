package com.sns.homeconnect_v2.data.remote.dto.response

data class EmailResponse(
    val status: String?, // có thể null
    val code: String? = null,
    val message: String
)
