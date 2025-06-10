package com.sns.homeconnect_v2.data.repository

import android.util.Log
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.request.EmailRequest
import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse

import com.sns.homeconnect_v2.domain.repository.OTPRepository
import javax.inject.Inject

class OTPRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : OTPRepository {

    override suspend fun sendOTP(email: String): EmailResponse {
        val request = EmailRequest(email = email)
        return apiService.sendOTP(request)
    }

    override suspend fun verifyOTP(email: String, otp: String): EmailResponse {
        try {
            Log.d("OTPRepository", "Creating request - Email: $email, OTP: $otp")
            val request = EmailRequest(email = email, otp = otp)
            Log.d("OTPRepository", "Sending request to API: $request")

            // Sử dụng endpoint mới
            val response = apiService.verifyOTPNew(request)
            Log.d("OTPRepository", "API Response received: Success=${response.status}, Message=${response.message}")

            return response
        } catch (e: Exception) {
            Log.e("OTPRepository", "Error in verifyOTP", e)
            throw e
        }
    }

    override suspend fun checkEmail(email: String): EmailResponse {
        val request = EmailRequest(email = email)
        return apiService.checkEmail(request)
    }

    override suspend fun sendOtp(email: String): EmailResponse {
        val request = EmailRequest(email = email)
        return apiService.sendOtp(request)
    }


}