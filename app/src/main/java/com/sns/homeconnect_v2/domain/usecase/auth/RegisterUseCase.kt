package com.sns.homeconnect_v2.domain.usecase.auth


import com.sns.homeconnect_v2.data.remote.dto.request.RegisterRequest
import com.sns.homeconnect_v2.data.remote.dto.response.RegisterResponse
import com.sns.homeconnect_v2.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(user: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = authRepository.register(user)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}