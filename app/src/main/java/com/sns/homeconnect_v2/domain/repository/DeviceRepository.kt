package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.request.BulkDeviceStateUpdateRequest
import com.sns.homeconnect_v2.data.remote.dto.request.LedEffectRequest
import com.sns.homeconnect_v2.data.remote.dto.request.LedPresetRequest
import com.sns.homeconnect_v2.data.remote.dto.request.LinkDeviceRequest
import com.sns.homeconnect_v2.data.remote.dto.request.StopLedEffectRequest
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateDeviceStateRequest
import com.sns.homeconnect_v2.data.remote.dto.response.AttributeResponse
import com.sns.homeconnect_v2.data.remote.dto.response.BulkDeviceStateUpdateResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceCapabilitiesResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceStateResponse
import com.sns.homeconnect_v2.data.remote.dto.response.LedEffectsResponse
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
    suspend fun getDeviceState(serialNumber: String): DeviceStateResponse
    suspend fun updateDeviceState(deviceId: String, request: UpdateDeviceStateRequest): UpdateDeviceStateResponse
    suspend fun updateDeviceStateBulk(serial_number: String, request: BulkDeviceStateUpdateRequest): BulkDeviceStateUpdateResponse
    suspend fun applyLedEffect(serial_number: String, request: LedEffectRequest): DeviceResponse
    suspend fun stopLedEffect(serial_number: String, request: StopLedEffectRequest): DeviceResponse
    suspend fun applyLedPreset(serial_number: String, request: LedPresetRequest): DeviceResponse
    suspend fun getLedEffects(deviceId: String): LedEffectsResponse
}