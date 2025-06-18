package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.response.DeviceStateResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import jakarta.inject.Inject

class GetDeviceStateUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: String, serialNumber: String): Result<DeviceStateResponse> {
        return try {
            Result.success(repository.getDeviceState(deviceId, serialNumber))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
