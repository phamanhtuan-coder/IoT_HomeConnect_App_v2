package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class ToggleDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: Int, toggle: ToggleRequest): Result<ToggleResponse> {
        return try {
            val response = deviceRepository.toggleDevice(deviceId, toggle)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}