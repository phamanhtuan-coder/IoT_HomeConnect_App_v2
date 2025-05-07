package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class GetInfoDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: Int): Result<DeviceResponse> {
        return try {
            val response = deviceRepository.getInfoDevice(deviceId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}