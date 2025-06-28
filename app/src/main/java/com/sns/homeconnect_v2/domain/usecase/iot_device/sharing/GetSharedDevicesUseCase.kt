package com.sns.homeconnect_v2.domain.usecase.iot_device.sharing

import com.sns.homeconnect_v2.data.remote.dto.response.SharedDeviceResponse
import com.sns.homeconnect_v2.domain.repository.SharedRepository
import jakarta.inject.Inject

class GetSharedDevicesUseCase @Inject constructor(
    private val repository: SharedRepository
) {
    suspend operator fun invoke(): Result<List<SharedDeviceResponse>> {
        return try {
            val result = repository.getSharedDevicesForUser()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
