package com.sns.homeconnect_v2.domain.usecase.house

import com.sns.homeconnect_v2.data.remote.dto.response.house.UpdateHouseRequest
import com.sns.homeconnect_v2.data.remote.dto.response.house.UpdateHouseResponse
import com.sns.homeconnect_v2.domain.repository.HouseRepository
import javax.inject.Inject

class UpdateHouseUseCase @Inject constructor(
    private val houseRepository: HouseRepository
) {
    suspend operator fun invoke(
        houseId: Int,
        request: UpdateHouseRequest
    ): Result<UpdateHouseResponse> {
        return  try {
            // Gọi hàm cập nhật từ Repository
            val response = houseRepository.updateHouse(houseId, request)
           Result.success(response)
        } catch (e: Exception) {
          Result.failure(e)
        }
    }
}