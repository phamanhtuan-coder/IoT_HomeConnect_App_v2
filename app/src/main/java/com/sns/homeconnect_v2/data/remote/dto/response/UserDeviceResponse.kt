package com.sns.homeconnect_v2.data.remote.dto.response

data class UserActivityResponse(
    val user_device_id: Int,
    val user_id: String,
    val device_name: String,
    val device_id: String?,
    val device_uuid: String?,
    val device_token: String?,
    val last_login: String?,
    val last_out: String?,
    val created_at: String?,
    val updated_at: String?,
    val is_deleted: Boolean
)
