package com.sns.homeconnect_v2.data.remote.dto.response

data class ToggleResponse(
    val message: String,
    var device: DeviceResponse,
    val error: String
)