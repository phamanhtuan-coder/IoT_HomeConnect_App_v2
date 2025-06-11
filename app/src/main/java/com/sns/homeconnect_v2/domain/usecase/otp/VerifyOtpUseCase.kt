package com.sns.homeconnect_v2.domain.usecase.otp

import com.sns.homeconnect_v2.domain.repository.OTPRepository
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val otpRepository: OTPRepository
) {
    suspend operator fun invoke(email: String, otp: String): Result<String> {
        return try {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return Result.failure(IllegalArgumentException("Email không hợp lệ"))
            }

            if (!otp.matches(Regex("^\\d{6}$"))) {
                return Result.failure(IllegalArgumentException("OTP phải gồm 6 chữ số"))
            }

            val response = otpRepository.verifyOTP(email, otp)

            if (response.status == "error") {
                Result.failure(IllegalStateException(response.message))
            } else if (response.status==null) {
                Result.success(response.message)
            } else {
                Result.failure(IllegalStateException("Email không tồn tại"))
            }

        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Đã xảy ra lỗi khi xác thực OTP"))
        }
    }
}

