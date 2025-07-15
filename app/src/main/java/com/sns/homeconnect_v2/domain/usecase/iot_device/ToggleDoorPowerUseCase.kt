package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.request.ToggleDoorRequest
import com.sns.homeconnect_v2.data.remote.dto.response.DoorToggleResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class ToggleDoorPowerUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(
        serialNumber: String,
        request: ToggleDoorRequest
    ): DoorToggleResponse {
        return repository.toggleDoorPower(serialNumber, request)
    }
}
