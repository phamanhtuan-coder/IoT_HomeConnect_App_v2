package com.sns.homeconnect_v2.data.remote.dto.response

data class AlertDetail(
    val alertId: Int,
    val deviceId: Int,
    val spaceId: Int,
    val typeId: Int,
    val message: String,
    val timestamp: String,
    val status: Boolean,
    val alertTypeId: Int,
    val alertType: AlertType
)