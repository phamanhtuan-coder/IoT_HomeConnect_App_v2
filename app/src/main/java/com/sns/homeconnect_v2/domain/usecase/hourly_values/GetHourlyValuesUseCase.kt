package com.sns.homeconnect_v2.domain.usecase.hourly_values

import com.sns.homeconnect_v2.domain.repository.SpaceRepository
import jakarta.inject.Inject

class GetHourlyValuesUseCase @Inject constructor(
    private val spaceRepository: SpaceRepository
) {
    suspend operator fun invoke(
        spaceId: Int,
        startTime: String,
        endTime: String,
        page: Int = 1,
        limit: Int = 10
    ) = spaceRepository.getHourlyValues(spaceId, startTime, endTime, page, limit)
}
