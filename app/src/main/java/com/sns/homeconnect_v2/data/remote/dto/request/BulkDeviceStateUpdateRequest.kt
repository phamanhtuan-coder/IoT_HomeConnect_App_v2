package com.sns.homeconnect_v2.data.remote.dto.request

data class BulkDeviceStateUpdateRequest(
    val serial_number: String,
    val updates: List<Map<String, Any>>
)
