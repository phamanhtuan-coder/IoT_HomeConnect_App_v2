package com.sns.homeconnect_v2.domain.usecase.profile

import com.sns.homeconnect_v2.data.remote.dto.response.User
import com.sns.homeconnect_v2.domain.repository.AuthRepository
import javax.inject.Inject

class GetInfoProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<User> {
        return try {
            val response = authRepository.getInfoProfile()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}