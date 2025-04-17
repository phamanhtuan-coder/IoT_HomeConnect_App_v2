package com.sns.homeconnect_v2.domain.usecase.auth

import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse
import com.sns.homeconnect_v2.domain.repository.OTPRepository
import javax.inject.Inject

class CheckEmailUseCase @Inject constructor(
    private val otpRepository: OTPRepository
) {
    suspend operator fun invoke(email: String): Result<EmailResponse> {
        return try {
            if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))) {
                throw IllegalArgumentException("Invalid email")
            }
            val response = otpRepository.sendOTP(email)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}