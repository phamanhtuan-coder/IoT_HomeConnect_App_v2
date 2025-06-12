package com.sns.homeconnect_v2.domain.usecase.space

import com.sns.homeconnect_v2.data.remote.dto.response.SpaceResponse
import com.sns.homeconnect_v2.domain.repository.SpaceRepository
import jakarta.inject.Inject

class GetListSpaceUseCase @Inject constructor(
private val spaceRepository: SpaceRepository
) {
    suspend operator fun invoke(
        houseId: Int
    ): Result<List<SpaceResponse>> {
        return try {
           var response= spaceRepository.getSpacesByHomeId(houseId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}