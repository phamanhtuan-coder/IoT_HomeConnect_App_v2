package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.request.LedEffectRequest
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class ApplyLedEffectUseCase @Inject constructor(
    private val repo: DeviceRepository
) {
    suspend operator fun invoke(
        serial_number: String,
        request: LedEffectRequest
    ): Result<DeviceResponse> {
        return try {
            val response = repo.applyLedEffect(serial_number, request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
