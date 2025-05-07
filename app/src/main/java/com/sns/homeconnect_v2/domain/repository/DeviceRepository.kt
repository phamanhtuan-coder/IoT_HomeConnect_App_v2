package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.response.AttributeResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.LinkDeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UnlinkResponse

interface DeviceRepository {
    suspend fun getInfoDevice(deviceId: Int): DeviceResponse
    suspend fun toggleDevice(deviceId: Int, toggleRequest: ToggleRequest): ToggleResponse
    suspend fun updateAttributeDevice(
        deviceId: Int,
        brightness: Int,
        color: String
    ): AttributeResponse

    suspend fun unlinkDevice(deviceId: Int): UnlinkResponse
    suspend fun linkDevice(
        deviceId: String,
        spaceId: String,
        deviceName: String
    ): LinkDeviceResponse
}