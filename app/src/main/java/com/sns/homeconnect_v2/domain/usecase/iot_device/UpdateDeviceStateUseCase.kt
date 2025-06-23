package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.request.UpdateDeviceStateRequest
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateDeviceStateResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import jakarta.inject.Inject

class UpdateDeviceStateUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(
        serial_number: String,
        request: UpdateDeviceStateRequest
    ): Result<UpdateDeviceStateResponse> {
        return try {
            Result.success(repository.updateDeviceState(serial_number, request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}