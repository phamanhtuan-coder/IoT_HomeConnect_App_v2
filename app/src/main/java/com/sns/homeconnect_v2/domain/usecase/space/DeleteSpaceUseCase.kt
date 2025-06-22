package com.sns.homeconnect_v2.domain.usecase.space

import com.sns.homeconnect_v2.domain.repository.SpaceRepository
import javax.inject.Inject

class DeleteSpaceUseCase @Inject constructor(
private val spaceRepository: SpaceRepository
) {
    suspend operator fun invoke(spaceId: Int): Result<Unit> {
        return try {
            spaceRepository.deleteSpace(spaceId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}