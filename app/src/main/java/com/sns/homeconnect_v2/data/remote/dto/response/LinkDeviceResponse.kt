package com.sns.homeconnect_v2.data.remote.dto.response

data class EffectParams(
    val count: Int,
    val speed: Int,
    val color1: String,
    val color2: String,
    val effect: String,
    val duration: Int
)

data class DeviceAttribute(
    val color: String,
    val effect: String,
    val status: String,
    val door_type: String,
    val last_seen: String,
    val brightness: Int,
    val color_mode: String,
    val deviceType: String,
    val effect_count: Int,
    val effect_speed: Int,
    val power_status: Boolean,
    val effect_active: Boolean,
    val effect_color1: String,
    val effect_color2: String,
    val effect_params: EffectParams,
    val effect_preset: String,
    val offline_reason: String?,
    val connection_type: String,
    val effect_duration: Int
)

data class LinkDeviceResponse(
    val device_id: String,
    val serial_number: String,
    val template_id: String,
    val group_id: Int,
    val space_id: Int,
    val account_id: String,
    val hub_id: String?,
    val firmware_id: String?,
    val name: String,
    val power_status: Boolean,
    val attribute: DeviceAttribute?,
    val wifi_ssid: String?,
    val wifi_password: String?,
    val current_value: String?,
    val link_status: String,
    val last_reset_at: String?,
    val lock_status: String,
    val locked_at: String?,
    val created_at: String,
    val updated_at: String,
    val is_deleted: Boolean
)
