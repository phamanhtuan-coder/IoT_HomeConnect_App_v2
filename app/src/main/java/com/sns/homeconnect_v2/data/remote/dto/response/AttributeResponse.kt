package com.sns.homeconnect_v2.data.remote.dto.response

data class AttributeResponse(
    val message: String,
    val device: DeviceResponse,
    val error: String
)