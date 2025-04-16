package com.sns.homeconnect_v2.domain.usecase.otp

import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse
import com.sns.homeconnect_v2.domain.repository.OTPRepository
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val otpRepository: OTPRepository
) {
    suspend operator fun invoke(email: String): Result<EmailResponse> {
        return try {
            val response = otpRepository.sendOTP(email)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}