package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.OwnedDeviceResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import jakarta.inject.Inject

class ListOfUserOwnedDevicesUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
){
    suspend operator fun invoke(): Result<List<OwnedDeviceResponse>> {
        return try {
            val response = deviceRepository.getListOfUserOwnedDevices()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}