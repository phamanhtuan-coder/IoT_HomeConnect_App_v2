package com.example.ungdungquanlyvadieukhieniot_homeconnect.data.remote.dto.base

interface IDeviceBase {
    val deviceId: Int
    val name: String
    val typeId: Int
    val spaceId: Int
    val powerStatus: Boolean
    val attribute: String
}