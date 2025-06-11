package com.sns.homeconnect_v2.domain.usecase.auth

import com.sns.homeconnect_v2.domain.repository.AuthRepository
import javax.inject.Inject

sealed class CheckEmailResult {
    object Verified : CheckEmailResult()
    object NotFound : CheckEmailResult()
    object NotVerified : CheckEmailResult()
    data class Failure(val message: String) : CheckEmailResult()
}

class CheckEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<CheckEmailResult> {
        return try {
            val response = authRepository.checkEmail(email)
            // xử lý logic ở đây
            val result = when {
                !response.exists -> CheckEmailResult.NotFound
                !response.isVerified -> CheckEmailResult.NotVerified
                else -> CheckEmailResult.Verified
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

