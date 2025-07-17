package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.request.RegisterRequest
import com.sns.homeconnect_v2.data.remote.dto.response.CheckEmailResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ForgotPasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.LoginResponse
import com.sns.homeconnect_v2.data.remote.dto.response.NewPasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.RegisterResponse
import com.sns.homeconnect_v2.data.remote.dto.response.User

interface AuthRepository {
    suspend fun login(username: String, password: String): LoginResponse
    suspend fun logout(): Result<Unit>
    suspend fun register(user: RegisterRequest): RegisterResponse
    suspend fun getInfoProfile(): User
    suspend fun newPassword(email: String, password: String): NewPasswordResponse
    suspend fun checkEmail(email: String): CheckEmailResponse
    suspend fun forgotPassword(email: String, newPassword: String): ForgotPasswordResponse
    suspend fun logoutAllDevices(): Result<Unit>
}
