package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.response.DeviceCapabilitiesResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import jakarta.inject.Inject

class GetDeviceCapabilitiesUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: String, serialNumber: String): Result<DeviceCapabilitiesResponse> {
        return try {
            val response = repository.getDeviceCapabilities(deviceId, serialNumber)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
