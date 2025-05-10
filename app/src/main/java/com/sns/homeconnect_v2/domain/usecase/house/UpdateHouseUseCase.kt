package com.sns.homeconnect_v2.domain.usecase.house

import com.sns.homeconnect_v2.domain.repository.HouseRepository
import com.sns.homeconnect_v2.presentation.viewmodel.house.UpdateHouseState
import javax.inject.Inject

class CreateHouseUseCase @Inject constructor(
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