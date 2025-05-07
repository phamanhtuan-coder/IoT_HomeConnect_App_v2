package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.domain.repository.SpaceRepository
import javax.inject.Inject
import com.sns.homeconnect_v2.data.remote.dto.response.SpaceResponse


class GetSpacesByHomeIdUseCase @Inject constructor(
    private val spaceRepository: SpaceRepository
) {
    suspend operator fun invoke(homeId: Int): Result<List<SpaceResponse>> {
        return try {
            val response = spaceRepository.getSpacesByHomeId(homeId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}