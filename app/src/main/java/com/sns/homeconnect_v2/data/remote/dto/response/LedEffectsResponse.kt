package com.sns.homeconnect_v2.data.remote.dto.response

data class LedEffectInfo(
    val name: String,
    val description: String,
    val params: List<String>
)

data class LedEffectsResponse(
    val success: Boolean,
    val effects: List<LedEffectInfo>,
    val available_effects: List<String>
)