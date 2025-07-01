package com.sns.homeconnect_v2.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class DevicePermissionListResponse(
    @SerializedName("data") val data: List<DevicePermissionResponse>,
    @SerializedName("total_page") val totalPage: Int
)

data class DevicePermissionResponse(
    @SerializedName("permission_id") val permissionId: Int,
    @SerializedName("device_serial") val deviceSerial: String,
    @SerializedName("permission_type") val permissionType: String,
    @SerializedName("serial_number") val serialNumber: String,
    @SerializedName("shared_with_user_id") val sharedWithUserId: String,
    @SerializedName("device_name") val deviceName: String,
    @SerializedName("template_device_name") val templateDeviceName: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("is_deleted") val isDeleted: Int
)

//data class OwnedDeviceResponse(
//    @SerializedName("device_id") val device_id: String,
//    @SerializedName("device_name") val name: String?,
//    @SerializedName("serial_number") val serial_number: String,
//    @SerializedName("template_id") val template_id: String,
//    @SerializedName("category_name") val category_name: String?,
//    @SerializedName("created_at") val created_at: String?,
//    @SerializedName("updated_at") val updated_at: String?,
//    @SerializedName("is_deleted") val is_deleted: Int,
//    val isRevealed: Boolean = false // Để hỗ trợ trạng thái UI
//)
//
//
//data class OwnedDeviceListResponse(
//    @SerializedName("data") val data: List<OwnedDeviceResponse>,
//    @SerializedName("total_page") val totalPage: Int
//)
