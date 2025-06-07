package com.sns.homeconnect_v2.domain.usecase.house

import com.sns.homeconnect_v2.data.remote.dto.response.HouseWithSpacesResponse
import com.sns.homeconnect_v2.domain.repository.HouseRepository
import javax.inject.Inject

class GetHousesByGroupUseCase @Inject constructor(
    private val houseRepository: HouseRepository
) {
    suspend operator fun invoke(groupId: Int): Result<List<HouseWithSpacesResponse>> {
        return try {
            Result.success(houseRepository.getHousesByGroupId(groupId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
