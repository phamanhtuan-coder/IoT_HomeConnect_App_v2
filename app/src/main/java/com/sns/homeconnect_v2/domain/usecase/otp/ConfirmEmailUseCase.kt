package com.sns.homeconnect_v2.domain.usecase.otp

import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse
import com.sns.homeconnect_v2.domain.repository.UserRepository
import javax.inject.Inject

class ConfirmEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String): Result<EmailResponse> {
        return try {
            val response = userRepository.confirmEmail(email)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}