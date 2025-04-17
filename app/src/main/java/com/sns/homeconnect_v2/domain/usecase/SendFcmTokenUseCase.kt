package com.sns.homeconnect_v2.domain.usecase

import com.sns.homeconnect_v2.data.remote.dto.response.DeviceTokenResponse
import com.sns.homeconnect_v2.domain.repository.UserRepository
import javax.inject.Inject

class SendFcmTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(token: String): Result<DeviceTokenResponse> {
        return try {
            val response = userRepository.sendToken(token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}