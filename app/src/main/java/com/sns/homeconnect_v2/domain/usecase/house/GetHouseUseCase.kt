package com.sns.homeconnect_v2.domain.usecase.house

import com.sns.homeconnect_v2.data.remote.dto.response.house.House
import com.sns.homeconnect_v2.domain.repository.HouseRepository
import jakarta.inject.Inject

class GetHouseUseCase @Inject constructor(
    private val houseRepository: HouseRepository
) {
    suspend operator fun invoke(houseId: Int): Result<House> {
        return try {
            val response = houseRepository.getHouseById(houseId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}