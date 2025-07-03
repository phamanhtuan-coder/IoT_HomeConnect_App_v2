package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.response.UnlinkResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class UnlinkDeviceUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(serialNumber: String): Result<Unit> {
        return try {
            repository.unlinkDevice(serialNumber)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
