package com.sns.homeconnect_v2.domain.usecase.house

import com.sns.homeconnect_v2.domain.repository.HouseRepository
import javax.inject.Inject

class DeleteHouseUseCase @Inject constructor(
private val houseRepository: HouseRepository
) {
    suspend operator fun invoke(houseId: Int): Result<Unit> {
        return try {
            houseRepository.deleteHouse(houseId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}