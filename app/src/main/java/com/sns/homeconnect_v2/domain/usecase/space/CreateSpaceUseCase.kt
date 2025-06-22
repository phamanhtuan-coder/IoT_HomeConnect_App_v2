package com.sns.homeconnect_v2.domain.usecase.space

import com.sns.homeconnect_v2.data.remote.dto.request.CreateSpaceRequest
import com.sns.homeconnect_v2.data.remote.dto.response.SpaceResponse
import com.sns.homeconnect_v2.domain.repository.SpaceRepository
import javax.inject.Inject

class CreateSpaceUseCase @Inject constructor(
    private val spaceRepository: SpaceRepository
) {
    suspend operator fun invoke(
        request: CreateSpaceRequest
    ): Result<SpaceResponse> {
        return try {
            val response = spaceRepository.createSpace(request) // Lưu SpaceResponse
            Result.success(response) // Trả về Result<SpaceResponse>
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}