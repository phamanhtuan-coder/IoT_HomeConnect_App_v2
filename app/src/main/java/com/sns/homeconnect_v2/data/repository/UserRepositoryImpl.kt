package com.sns.homeconnect_v2.data.repository

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.*
import com.sns.homeconnect_v2.data.remote.dto.request.ChangePasswordRequest
import com.sns.homeconnect_v2.data.remote.dto.request.DeviceTokenRequest
import com.sns.homeconnect_v2.data.remote.dto.request.EmailRequest
import com.sns.homeconnect_v2.data.remote.dto.request.NewPasswordRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UserRequest
import com.sns.homeconnect_v2.data.remote.dto.response.ChangePasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceTokenResponse
import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse
import com.sns.homeconnect_v2.data.remote.dto.response.NewPasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.SharedWithResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UserResponse
import com.sns.homeconnect_v2.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager
) : UserRepository {
    override suspend fun sendToken(deviceToken: String): DeviceTokenResponse {
        val request = DeviceTokenRequest(deviceToken = deviceToken)
        val token = authManager.getJwtToken()
        return apiService.sendToken("Bearer $token", request)
    }

    override suspend fun putChangePassword(userId: Int, request: ChangePasswordRequest): ChangePasswordResponse {
        val token = authManager.getJwtToken()
        return apiService.putChangePassword(userId, request, "Bearer $token")
    }



    override suspend fun confirmEmail(email: String): EmailResponse {
        val request = EmailRequest(email = email)
        val token = authManager.getJwtToken()
        return apiService.confirmEmail(request, "Bearer $token")
    }

    override suspend fun getSharedWith(userId: Int): List<SharedWithResponse> {
        val token = authManager.getJwtToken()
        return apiService.sharedWith(userId, "Bearer $token")
    }


    override suspend fun putInfoProfile(userId: Int, user: UserRequest): UserResponse {
        val token = authManager.getJwtToken()
        return apiService.putInfoProfile(userId, user, "Bearer $token")
    }

}