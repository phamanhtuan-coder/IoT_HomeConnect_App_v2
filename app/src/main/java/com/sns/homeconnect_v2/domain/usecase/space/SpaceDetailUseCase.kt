package com.sns.homeconnect_v2.domain.usecase.space

import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponseSpace
import com.sns.homeconnect_v2.domain.repository.SpaceRepository
import jakarta.inject.Inject

class GetSpaceDetailUseCase @Inject constructor(
private val spaceRepository: SpaceRepository
) {
    suspend operator fun invoke(
        spaceId: Int
    ): Result<List<DeviceResponseSpace>> {
        return try {
            val response = spaceRepository.getDevicesBySpaceId(spaceId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}