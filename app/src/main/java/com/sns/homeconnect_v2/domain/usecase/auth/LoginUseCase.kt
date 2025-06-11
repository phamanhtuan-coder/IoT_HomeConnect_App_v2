package com.sns.homeconnect_v2.domain.usecase.auth

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val authManager: AuthManager
) {
    suspend operator fun invoke(username: String, password: String): Result<String> {
        return try {
            val response = authRepository.login(username, password)

            if (response.accessToken.isNotEmpty()) {
                authManager.saveJwtToken(response.accessToken)
                authManager.saveRefreshToken(response.refreshToken)
                authManager.saveDeviceUuid(response.deviceUuid)
            }

            Result.success(response.accessToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
