package com.sns.homeconnect_v2.domain.usecase.auth

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val authManager: AuthManager
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        return try {
            val response = authRepository.login(email, password)
            authManager.saveJwtToken(response.token)
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}