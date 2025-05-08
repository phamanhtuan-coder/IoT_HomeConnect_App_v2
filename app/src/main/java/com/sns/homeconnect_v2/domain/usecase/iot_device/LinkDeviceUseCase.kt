package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.response.LinkDeviceResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class LinkDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: String, spaceId: String, deviceName: String): Result<LinkDeviceResponse> {
        return try {
            val response = deviceRepository.linkDevice(deviceId, spaceId, deviceName)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}