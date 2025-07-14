package com.sns.homeconnect_v2.data.remote.dto.request

data class ToggleDoorRequest(
    val power_status: Boolean,
    val force: Boolean = false,
    val timeout: Int = 30000
)
