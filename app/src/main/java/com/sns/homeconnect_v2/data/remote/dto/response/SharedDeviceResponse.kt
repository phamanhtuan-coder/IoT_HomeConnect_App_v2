package com.sns.homeconnect_v2.data.remote.dto.response

data class SharedDeviceWrapper(
    val data: List<SharedDeviceResponse>,
    val total_page: Int
)

data class SharedDeviceResponse(
    val permission_id: Int,
    val device_serial: String,
    val permission_type: String,
    val shared_with_user_id: String,
    val device_id: String?,
    val device_name: String?,
    val template_id: String?,
    val template_device_name: String?,
    val category_name: String?,
    val created_at: String,
    val updated_at: String,
    val is_deleted: Int
)
