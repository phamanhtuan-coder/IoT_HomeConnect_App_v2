package com.sns.homeconnect_v2.domain.usecase.iot_device.sharing

import com.sns.homeconnect_v2.data.remote.dto.response.SharedUser
import com.sns.homeconnect_v2.domain.repository.SharedRepository
import javax.inject.Inject

class GetSharedUsersUseCase @Inject constructor(
    private val shareRepository: SharedRepository
) {
    suspend operator fun invoke(
        deviceId: Int,
    ): Result<List<SharedUser>> {
        return try {
            val response = shareRepository.getSharedUsers(deviceId)
          Result.success(response)
        } catch (e: Exception) {
           Result.failure(e)
        }
    }
}