package com.sns.homeconnect_v2.data.remote.dto.response

import com.google.gson.JsonElement

data class OwnedDeviceResponse(
    val device_id: String,
    val serial_number: String,
    val template_id: String?,
    val space_id: Int?,
    val account_id: String?,
    val hub_id: String?,
    val firmware_id: String?,
    val name: String?,
    val power_status: Boolean,
    val attribute: JsonElement?,         // hoặc Map<String, Any>?
    val wifi_ssid: String?,
    val wifi_password: String?,
    val current_value: JsonElement?,     // hoặc Map<String, Any>?
    val link_status: String?,
    val last_reset_at: String?,          // hoặc LocalDateTime?
    val lock_status: String?,
    val locked_at: String?,              // hoặc LocalDateTime?
    val created_at: String?,
    val updated_at: String?,
    val is_deleted: Boolean,
    val isRevealed: Boolean = false
)
