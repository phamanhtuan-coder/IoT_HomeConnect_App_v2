package com.sns.homeconnect_v2.domain.usecase.auth

import com.sns.homeconnect_v2.domain.repository.AuthRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }

    suspend fun logoutAllDevices(): Result<Unit> {
        return try {
            authRepository.logoutAllDevices()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}