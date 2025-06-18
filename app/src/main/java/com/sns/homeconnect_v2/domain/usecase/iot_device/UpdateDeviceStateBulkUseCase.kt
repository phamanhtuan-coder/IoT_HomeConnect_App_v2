package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.request.BulkDeviceStateUpdateRequest
import com.sns.homeconnect_v2.data.remote.dto.response.BulkDeviceStateUpdateResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import jakarta.inject.Inject

class UpdateDeviceStateBulkUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(deviceId: String, request: BulkDeviceStateUpdateRequest): Result<BulkDeviceStateUpdateResponse> {
        return try {
            Result.success(repository.updateDeviceStateBulk(deviceId, request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
