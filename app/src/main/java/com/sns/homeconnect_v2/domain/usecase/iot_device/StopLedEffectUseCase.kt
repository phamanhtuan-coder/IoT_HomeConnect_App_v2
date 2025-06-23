package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.request.StopLedEffectRequest
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class StopLedEffectUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(
        serial_number: String,
        request: StopLedEffectRequest
    ): Result<DeviceResponse> = try {
        Result.success(deviceRepository.stopLedEffect(serial_number, request))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
