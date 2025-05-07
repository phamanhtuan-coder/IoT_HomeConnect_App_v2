package com.sns.homeconnect_v2.data.remote.dto.request

data class LinkDeviceRequest(
    val deviceId: String,
    val spaceId: String,
    val deviceName: String
)