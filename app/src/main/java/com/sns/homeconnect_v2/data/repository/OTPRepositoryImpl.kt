package com.sns.homeconnect_v2.data.repository

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
        val request = EmailRequest(email = email, otp = otp)
        return apiService.verifyOTP(request)
    }

    override suspend fun checkEmail(email: String): EmailResponse {
        val request = EmailRequest(email = email)
        return apiService.checkEmail(request)
    }


}