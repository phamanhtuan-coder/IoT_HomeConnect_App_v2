package com.sns.homeconnect_v2.domain.usecase.auth

import com.sns.homeconnect_v2.data.remote.dto.response.NewPasswordResponse
import com.sns.homeconnect_v2.domain.repository.AuthRepository
import javax.inject.Inject

class NewPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<NewPasswordResponse> {
        return try {
            val response = authRepository.newPassword(email, password)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}