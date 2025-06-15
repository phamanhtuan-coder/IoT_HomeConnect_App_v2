package com.sns.homeconnect_v2.data.remote.dto.response

data class DeviceCapabilitiesResponse(
    val success: Boolean,
    val capabilities: CapabilitiesWrapper,
    val timestamp: String
)

data class CapabilitiesWrapper(
    val base: BaseCapabilities?,
    val runtime: String?, // nếu backend trả về kiểu chuỗi
    val firmware_version: String?,
    val firmware_id: String?,
    val merged_capabilities: MergedCapabilities
)

data class BaseCapabilities(
    val category: String?,
    val capabilities: List<String>
)

data class MergedCapabilities(
    val capabilities: List<String>,
    val category: String?,
    val controls: Map<String, String> // ví dụ: "brightness" -> "slider"
)
