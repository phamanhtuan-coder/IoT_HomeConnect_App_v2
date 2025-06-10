package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse


interface OTPRepository {
    suspend fun sendOTP(email: String): EmailResponse
    suspend fun verifyOTP(email: String, otp: String): EmailResponse
    suspend fun checkEmail(email: String): EmailResponse
    suspend fun sendOtp(email: String): EmailResponse
}