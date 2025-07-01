package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.response.OwnedDeviceResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class ListOfUserOwnedDevicesUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(searchQuery: String): Result<List<OwnedDeviceResponse>> {
        return try {
            val response = deviceRepository.getOwnedDevices(searchQuery)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}