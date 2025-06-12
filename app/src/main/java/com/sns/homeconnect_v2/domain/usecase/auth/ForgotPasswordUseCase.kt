package com.sns.homeconnect_v2.domain.usecase.auth

import com.sns.homeconnect_v2.domain.repository.AuthRepository
import jakarta.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, newPassword: String): Result<String> {
        return try {
            val res = repository.forgotPassword(email, newPassword)
            if (res.success) {
                Result.success(res.message)
            } else {
                Result.failure(Exception(res.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Có lỗi xảy ra"))
        }
    }
}
