package com.sns.homeconnect_v2.data.repository

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.request.AttributeRequest
import com.sns.homeconnect_v2.data.remote.dto.request.LinkDeviceRequest
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.response.AttributeResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.LinkDeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.OwnedDeviceResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UnlinkResponse
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
}