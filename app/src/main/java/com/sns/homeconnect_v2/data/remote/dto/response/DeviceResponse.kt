package com.sns.homeconnect_v2.data.remote.dto.response

data class DeviceResponseSpace(
    val device_id: String?,
    val serial_number: String,
    val template_id: String,
    val group_id: Int?,
    val space_id: Int,
    val account_id: String,
    val hub_id: String?,
    val firmware_id: String?,
    val name: String?,
    val power_status: Boolean,
    val attribute: Map<String, Any>?, // Sửa thành Map<String, Any>?
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
    val device_type_id: Int?, // Thêm trường từ JSON
    val device_type_name: String?, // Thêm trường từ JSON
    val device_template_name: String?, // Thêm trường từ JSON
    val device_template_status: String?, // Thêm trường từ JSON
    val device_base_capabilities: Map<String, Any>?, // Thêm trường, sử dụng Map cho object JSON
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