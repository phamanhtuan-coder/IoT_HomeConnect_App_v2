package com.sns.homeconnect_v2.domain.usecase.home

import com.sns.homeconnect_v2.data.remote.dto.response.house.HouseResponse
import com.sns.homeconnect_v2.domain.repository.HouseRepository
import javax.inject.Inject

class GetListHouseUseCase @Inject constructor(
    private val houseRepository: HouseRepository
) {
    suspend operator fun invoke(): Result<List<HouseResponse>> {
        return try {
            val response = houseRepository.getListHome()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}