package com.sns.homeconnect_v2.domain.usecase.space

import com.sns.homeconnect_v2.data.remote.dto.response.SpaceResponse
import com.sns.homeconnect_v2.domain.repository.SpaceRepository
import javax.inject.Inject

class UpdateSpaceUseCase @Inject constructor(
    private val spaceRepository: SpaceRepository
) {
    suspend operator fun invoke(
        spaceId: Int,
        name: String,
        iconName: String? = null,
        iconColor: String? = null,
        description: String? = null
    ): Result<SpaceResponse> {
        return spaceRepository.updateSpace(spaceId, name, iconName, iconColor, description)
    }
}