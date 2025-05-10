package com.sns.homeconnect_v2.domain.usecase.house


import com.sns.homeconnect_v2.data.remote.dto.response.house.HousesListResponse
import com.sns.homeconnect_v2.domain.repository.HouseRepository
import javax.inject.Inject

class FetchHousesUseCase @Inject constructor(
    private val houseRepository: HouseRepository
) {
    suspend operator fun invoke(

    ): Result<List<HousesListResponse>> {
        return try {
            val response = houseRepository.getHouses()
            Result.success(response)
        } catch (e: Exception) {
           Result.failure(e)
        }
    }
}