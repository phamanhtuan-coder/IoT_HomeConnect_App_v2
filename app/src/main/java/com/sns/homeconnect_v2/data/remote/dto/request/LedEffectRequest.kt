package com.sns.homeconnect_v2.data.remote.dto.request

data class LedEffectRequest(
    val serial_number: String,
    val effect: String,
    val speed: Int? = null,
    val count: Int? = null,
    val duration: Int? = null,
    val color1: String? = null,
    val color2: String? = null
)