package com.sns.homeconnect_v2.data.repository

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.request.AttributeRequest
import com.sns.homeconnect_v2.data.remote.dto.request.BulkDeviceStateUpdateRequest
import com.sns.homeconnect_v2.data.remote.dto.request.DeviceCapabilitiesRequest
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
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UnlinkResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateDeviceStateResponse
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager
) : DeviceRepository {

    override suspend fun getInfoDevice(
        deviceId: Int
    ): DeviceResponse {
        val token = authManager.getJwtToken()
        return apiService.getInfoDevice(deviceId, token = "Bearer $token")
    }

    override suspend fun toggleDevice(
        deviceId: Int,
        toggleRequest: ToggleRequest
    ): ToggleResponse {
        val token = authManager.getJwtToken()
        return apiService.toggleDevice(deviceId, toggleRequest, token = "Bearer $token")
    }

    override suspend fun updateAttributeDevice(
        deviceId: Int,
        brightness: Int,
        color: String
    ): AttributeResponse {
        val token = authManager.getJwtToken()
        val attributeRequest = AttributeRequest(brightness = brightness, color = color)
        return apiService.updateAttributes(deviceId, attributeRequest, token = "Bearer $token")
    }

    override suspend fun unlinkDevice(
        deviceId: Int
    ): UnlinkResponse {
        val token = authManager.getJwtToken()
        return apiService.unlinkDevice(deviceId, token = "Bearer $token")
    }

    override suspend fun linkDevice(request: LinkDeviceRequest): LinkDeviceResponse {
        val token = authManager.getJwtToken()
        return apiService.linkDevice(request, "Bearer $token")
    }

    override suspend fun getListOfUserOwnedDevices(): List<OwnedDeviceResponse> {
        val token = authManager.getJwtToken()
        return apiService.getUserOwnedDevices(token = "Bearer $token")
    }

    override suspend fun getDeviceCapabilities(deviceId: String, serialNumber: String): DeviceCapabilitiesResponse {
        val token = authManager.getJwtToken()
        return apiService.getDeviceCapabilities(
            deviceId = deviceId,
            request = DeviceCapabilitiesRequest(serial_number = serialNumber),
            token = "Bearer $token"
        )
    }
    override suspend fun getDeviceState(serialNumber: String): DeviceStateResponse {
        val token = authManager.getJwtToken()
        return apiService.getDeviceState(serialNumber, serialNumber, "Bearer $token")
    }

    override suspend fun updateDeviceState(
        serial_number: String,
        request: UpdateDeviceStateRequest
    ): UpdateDeviceStateResponse {
        val token = authManager.getJwtToken()
        return apiService.updateDeviceState(serial_number, request, "Bearer $token")
    }

    override suspend fun updateDeviceStateBulk(serial_number: String, request: BulkDeviceStateUpdateRequest): BulkDeviceStateUpdateResponse {
        val token = authManager.getJwtToken()
        return apiService.updateDeviceStateBulk(serial_number, request, "Bearer $token")
    }

    // ---------- LED / State ---------------------------------------------------
    override suspend fun applyLedEffect(
        serial_number: String,
        request: LedEffectRequest
    ): DeviceResponse {
        val token = authManager.getJwtToken()
        return apiService.applyLedEffect(serial_number, request, "Bearer $token")
    }

    override suspend fun stopLedEffect(
        serial_number: String,
        request: StopLedEffectRequest
    ): DeviceResponse {
        val token = authManager.getJwtToken()
        return apiService.stopLedEffect(
            serial_number,
            request,
            "Bearer $token"
        )
    }
    override suspend fun applyLedPreset(
        deviceId: String,
        request: LedPresetRequest
    ): DeviceResponse {
        val token = authManager.getJwtToken()
        return apiService.applyLedPreset(deviceId, request, "Bearer $token")
    }

    override suspend fun getLedEffects(deviceId: String): LedEffectsResponse {
        val token = authManager.getJwtToken()
        return apiService.getLedEffects(deviceId, "Bearer $token")
    }


}