package com.sns.homeconnect_v2.data.remote.dto.response

data class DeviceResponse(
    val DeviceID: Int,
    val TypeID: Int,
    val SpaceID: Int,
    val Name: String,
    var PowerStatus: Boolean,
    val Attribute: String
)