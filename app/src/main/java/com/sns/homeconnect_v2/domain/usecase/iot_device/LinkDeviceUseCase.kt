package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.data.remote.dto.request.LinkDeviceRequest
import com.sns.homeconnect_v2.data.remote.dto.response.LinkDeviceResponse
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import javax.inject.Inject

class LinkDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(request: LinkDeviceRequest): Result<LinkDeviceResponse> {
        return try {
            val res = deviceRepository.linkDevice(request)
            Result.success(res)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}