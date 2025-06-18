package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.request.BulkDeviceStateUpdateRequest
import com.sns.homeconnect_v2.data.remote.dto.request.LinkDeviceRequest
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateDeviceStateRequest
import com.sns.homeconnect_v2.data.remote.dto.response.AttributeResponse
import com.sns.homeconnect_v2.data.remote.dto.response.BulkDeviceStateUpdateResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceCapabilitiesResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceStateResponse
import com.sns.homeconnect_v2.data.remote.dto.response.LinkDeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.OwnedDeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UnlinkResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateDeviceStateResponse

interface DeviceRepository {
    suspend fun getInfoDevice(deviceId: Int): DeviceResponse
    suspend fun toggleDevice(deviceId: Int, toggleRequest: ToggleRequest): ToggleResponse
    suspend fun updateAttributeDevice(deviceId: Int, brightness: Int, color: String): AttributeResponse
    suspend fun unlinkDevice(deviceId: Int): UnlinkResponse
    suspend fun linkDevice(request: LinkDeviceRequest): LinkDeviceResponse
    suspend fun getListOfUserOwnedDevices(): List<OwnedDeviceResponse>
    suspend fun getDeviceCapabilities(deviceId: String, serialNumber: String): DeviceCapabilitiesResponse
    suspend fun getDeviceState(deviceId: String, serialNumber: String): DeviceStateResponse
    suspend fun updateDeviceState(deviceId: String, request: UpdateDeviceStateRequest): UpdateDeviceStateResponse
    suspend fun updateDeviceStateBulk(deviceId: String, request: BulkDeviceStateUpdateRequest): BulkDeviceStateUpdateResponse
}