package com.sns.homeconnect_v2.data.remote.dto.response

data class DeviceResponseSpace(
    val device_id: String?,
    val serial_number: String,
    val template_id: String,
    val group_id: Int,
    val space_id: Int,
    val account_id: String,
    val hub_id: String?,
    val firmware_id: String?,
    val name: String?,
    val power_status: Boolean,
    val attribute: String?,
    val wifi_ssid: String?,
    val wifi_password: String?,
    val current_value: String?,
    val link_status: String?,
    val last_reset_at: String?,
    val lock_status: String?,
    val locked_at: String?,
    val created_at: String?,
    val updated_at: String?,
    val is_deleted: Boolean,
    val isRevealed: Boolean = false
)

data class DeviceResponse(
    val DeviceID: Int,
    val TypeID: Int,
    val SpaceID: Int,
    val Name: String,
    var PowerStatus: Boolean,
    val Attribute: String
)