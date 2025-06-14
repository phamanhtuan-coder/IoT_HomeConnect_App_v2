package com.sns.homeconnect_v2.data.remote.dto.response

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
    val attribute: String?,
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


//data class LinkDeviceResponse(
//    val message: String,
//    val device: LinkDevice
//)
//
//data class LinkDevice(
//    val DeviceID: Int,
//    val TypeID: Int,
//    val SpaceID: String, // Chuyển SpaceID thành String
//    val UserID: Int,
//    val Name: String,
//    val PowerStatus: Boolean,
//    val Attribute: String,
//    val WifiSSID: String,
//    val WifiPassword: String,
//    val IsDeleted: Boolean,
//    val createdAt: String,
//    val updatedAt: String
//)