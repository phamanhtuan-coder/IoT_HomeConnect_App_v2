package com.sns.homeconnect_v2.presentation.model

data class DeviceSession(
    val id: Int,
    val deviceName: String,
    val browser: String,
    val ip: String,
    val lastAccess: String
)
