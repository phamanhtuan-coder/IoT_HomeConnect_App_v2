package com.sns.homeconnect_v2.domain.usecase.auth

import com.sns.homeconnect_v2.domain.repository.AuthRepository
import jakarta.inject.Inject

sealed class RecoveryPasswordResult {
    data class Success(val message: String) : RecoveryPasswordResult()
    data class Failure(val message: String) : RecoveryPasswordResult()
}

class RecoveryPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, newPassword: String): RecoveryPasswordResult {
        return try {
            val res = repository.recoveryPassword(email, newPassword)
            if (res.success) {
                RecoveryPasswordResult.Success(res.message)
            } else {
                RecoveryPasswordResult.Failure(res.message)
            }
        } catch (e: Exception) {
            RecoveryPasswordResult.Failure(e.message ?: "Có lỗi xảy ra")
        }
    }
}
