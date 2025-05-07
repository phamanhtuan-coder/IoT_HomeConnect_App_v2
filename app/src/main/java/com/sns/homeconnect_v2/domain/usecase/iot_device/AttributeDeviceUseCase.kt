package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.response.AttributeResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class AttributeDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(
        deviceId: Int,
        brightness: Int,
        color: String
    ): Result<AttributeResponse> {
        return try {
            val response = deviceRepository.updateAttributeDevice(deviceId, brightness, color)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}