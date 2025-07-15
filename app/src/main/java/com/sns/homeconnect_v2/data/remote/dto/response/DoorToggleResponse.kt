package com.sns.homeconnect_v2.data.remote.dto.response

data class DoorToggleResponse(
    val success: Boolean,
    val door: DoorStatus,
    val door_type: String,
    val action: String,
    val message: String,
    val timestamp: String
)

