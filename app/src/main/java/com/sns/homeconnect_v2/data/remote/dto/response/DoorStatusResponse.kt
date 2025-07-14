package com.sns.homeconnect_v2.data.remote.dto.response

data class DoorStatusResponse(
    val success: Boolean,
    val door_status: DoorStatus,
    val timestamp: String
)

data class DoorStatus(
    val door_state: String,          // "closed" or "open"
    val door_type: String,           // "ROLLING", "SLIDING", ...
    val is_moving: Boolean,
    val online: Boolean,
    val last_seen: String,
    val current_rounds: Int,
    val config: DoorConfig
)

data class DoorConfig(
    val open_rounds: Int,
    val closed_rounds: Int,
    val motor_speed: Int
)
