package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.request.LedPresetRequest
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class ApplyLedPresetUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(
        deviceId: String,
        request: LedPresetRequest
    ): Result<DeviceResponse> = try {
        Result.success(deviceRepository.applyLedPreset(deviceId, request))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
