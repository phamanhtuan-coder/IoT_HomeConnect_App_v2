package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.response.DoorStatusResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import jakarta.inject.Inject

class GetDoorStatusUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(serialNumber: String): DoorStatusResponse {
        return deviceRepository.getDoorStatus(serialNumber)
    }
}
