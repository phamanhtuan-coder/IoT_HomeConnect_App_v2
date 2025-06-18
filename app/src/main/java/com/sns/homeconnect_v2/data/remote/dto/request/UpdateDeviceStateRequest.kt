package com.sns.homeconnect_v2.data.remote.dto.request

data class UpdateDeviceStateRequest(
    val serial_number: String,
    val power_status: Boolean? = null,
    val brightness: Int? = null,
    val color: String? = null
)