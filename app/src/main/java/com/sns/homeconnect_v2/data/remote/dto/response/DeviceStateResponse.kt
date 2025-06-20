package com.sns.homeconnect_v2.data.remote.dto.response

data class DeviceStateResponse(
    val success: Boolean,
    val state: DeviceState,
    val timestamp: String
)

data class DeviceState(
    val power_status: Boolean,
    val brightness:   Int?   = null,
    val color:        String? = null
)
