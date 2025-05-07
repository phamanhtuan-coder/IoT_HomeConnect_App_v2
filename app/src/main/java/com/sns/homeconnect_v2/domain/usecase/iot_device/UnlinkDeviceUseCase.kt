package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.response.UnlinkResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class UnlinkDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: Int): Result<UnlinkResponse> {
        return try {
            val response = deviceRepository.unlinkDevice(deviceId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}