package com.sns.homeconnect_v2.data.repository

import android.content.Context
import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.request.LoginRequest
import com.sns.homeconnect_v2.data.remote.dto.request.NewPasswordRequest
import com.sns.homeconnect_v2.data.remote.dto.request.RegisterRequest
import com.sns.homeconnect_v2.data.remote.dto.response.LoginResponse
import com.sns.homeconnect_v2.data.remote.dto.response.NewPasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.RegisterResponse
import com.sns.homeconnect_v2.data.remote.dto.response.User
import com.sns.homeconnect_v2.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager,
    private val context: Context
) : AuthRepository {

    override suspend fun login(email: String, password: String): LoginResponse {
        val request = LoginRequest(
            Email = email,
            PasswordHash = password
        )
        return apiService.login(request)
    }

    override suspend fun logout() {
        val token = authManager.getJwtToken()
//   Todo:     apiService.logout("Bearer $token") // Assuming ApiService has a logout endpoint
        authManager.clearJwtToken()
    }

    override suspend fun register(user: RegisterRequest): RegisterResponse {
        return apiService.register(user)
    }

    override suspend fun getInfoProfile(): User {
        val token = authManager.getJwtToken()
        return apiService.getInfoProfile(token = "Bearer $token")
    }

    override suspend fun newPassword(email: String, password: String): NewPasswordResponse {
        // Tạo request
        val request = NewPasswordRequest(
            email = email,
            newPassword = password
        )
        // Gọi API
        return apiService.newPassword(request)
    }
}