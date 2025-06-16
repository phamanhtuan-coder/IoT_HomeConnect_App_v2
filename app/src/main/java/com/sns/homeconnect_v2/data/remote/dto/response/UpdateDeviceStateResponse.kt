package com.sns.homeconnect_v2.data.remote.dto.response

data class UpdateDeviceStateResponse(
    val success: Boolean,
    val device: DeviceResponse,
    val message: String
)