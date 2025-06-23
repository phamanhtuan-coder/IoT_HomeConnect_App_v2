package com.sns.homeconnect_v2.data.remote.dto.request

data class LedPresetRequest(
    val serial_number: String,
    val preset: String,
    val duration: Int? = null
)