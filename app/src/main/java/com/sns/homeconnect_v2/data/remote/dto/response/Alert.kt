package com.sns.homeconnect_v2.data.remote.dto.response

data class Alert(
    val id: Int,
    val deviceId: Int,
    val spaceId: Int,
    val typeId: Int,
    val message: String,
    val timestamp: String,
    val status: Boolean,
    val alertTypeName: String
)