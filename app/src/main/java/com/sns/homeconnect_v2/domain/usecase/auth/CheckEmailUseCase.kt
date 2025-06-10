package com.sns.homeconnect_v2.domain.usecase.auth

import com.sns.homeconnect_v2.domain.repository.AuthRepository
import javax.inject.Inject

sealed class CheckEmailResult {
    object NotFound : CheckEmailResult()
    object NotVerified : CheckEmailResult()
    object Verified : CheckEmailResult()
    data class Failure(val message: String) : CheckEmailResult()
}

class CheckEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): CheckEmailResult {
        return try {
            val resp = authRepository.checkEmail(email)
            when {
                !resp.exists     -> CheckEmailResult.NotFound
                !resp.isVerified -> CheckEmailResult.NotVerified
                else             -> CheckEmailResult.Verified
            }
        } catch (e: Exception) {
            CheckEmailResult.Failure(e.message ?: "Lỗi khi kiểm tra email")
        }
    }
}
