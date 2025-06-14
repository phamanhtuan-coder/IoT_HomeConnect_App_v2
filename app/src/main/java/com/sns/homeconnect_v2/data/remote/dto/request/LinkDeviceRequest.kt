package com.sns.homeconnect_v2.data.remote.dto.request

//data class LinkDeviceRequest(
//    val deviceId: String,
//    val spaceId: String,
//    val deviceName: String
//)

data class LinkDeviceRequest(
    val serial_number: String,
    val groupId: Int,
    val spaceId: Int,
    val name: String
)