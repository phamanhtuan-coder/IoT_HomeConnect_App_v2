package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.response.LedEffectsResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class GetLedEffectsUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(
        deviceId: String
    ): Result<LedEffectsResponse> = try {
        Result.success(deviceRepository.getLedEffects(deviceId))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
