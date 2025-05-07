package com.sns.homeconnect_v2.data.remote.dto.response

data class LinkDeviceResponse(
    val message: String,
    val device: LinkDevice
)

data class LinkDevice(
    val DeviceID: Int,
    val TypeID: Int,
    val SpaceID: String, // Chuyển SpaceID thành String
    val UserID: Int,
    val Name: String,
    val PowerStatus: Boolean,
    val Attribute: String,
    val WifiSSID: String,
    val WifiPassword: String,
    val IsDeleted: Boolean,
    val createdAt: String,
    val updatedAt: String
)