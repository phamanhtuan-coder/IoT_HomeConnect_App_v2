package com.sns.homeconnect_v2.domain.usecase.house

import com.sns.homeconnect_v2.data.remote.dto.response.house.CreateHouseRequest
import com.sns.homeconnect_v2.data.remote.dto.response.house.CreateHouseResponse
import com.sns.homeconnect_v2.domain.repository.HouseRepository
import javax.inject.Inject

class CreateHouseUseCase @Inject constructor(
    private val houseRepository: HouseRepository
) {
    suspend operator fun invoke(
        request: CreateHouseRequest
    ): Result<CreateHouseResponse> {
        return try {
            val response = houseRepository.createHouse(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}