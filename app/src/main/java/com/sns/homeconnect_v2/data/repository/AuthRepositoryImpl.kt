package com.sns.homeconnect_v2.data.repository

import android.content.Context
import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.request.CheckEmailRequest
import com.sns.homeconnect_v2.data.remote.dto.request.LoginRequest
import com.sns.homeconnect_v2.data.remote.dto.request.NewPasswordRequest
import com.sns.homeconnect_v2.data.remote.dto.request.RecoveryPasswordRequest
import com.sns.homeconnect_v2.data.remote.dto.request.RegisterRequest
import com.sns.homeconnect_v2.data.remote.dto.response.CheckEmailResponse
import com.sns.homeconnect_v2.data.remote.dto.response.LoginResponse
import com.sns.homeconnect_v2.data.remote.dto.response.NewPasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.RecoveryPasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.RegisterResponse
import com.sns.homeconnect_v2.data.remote.dto.response.User
import com.sns.homeconnect_v2.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager,
    private val context: Context
) : AuthRepository {

    override suspend fun login(username: String, password: String): LoginResponse {
        val deviceId = android.provider.Settings.Secure.getString(
            context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID
        )
        val deviceUuid = authManager.getDeviceUuid()

        val request = LoginRequest(
            username = username,
            password = password,
            rememberMe = true,
            deviceName = "Android_${android.os.Build.MODEL}",
            deviceId = deviceId,
            deviceUuid = deviceUuid
        )

        val response = apiService.login(request)

        // Lưu token và deviceUuid
        authManager.saveJwtToken(response.accessToken)
        authManager.saveRefreshToken(response.refreshToken)
        authManager.saveDeviceUuid(response.deviceUuid)

        return response
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

    override suspend fun checkEmail(email: String): CheckEmailResponse {
        val request = CheckEmailRequest(email)
        return apiService.checkEmail(request)
    }

    override suspend fun recoveryPassword(email: String, newPassword: String): RecoveryPasswordResponse {
        val req = RecoveryPasswordRequest(email, newPassword)
        return apiService.recoveryPassword(req)
    }
}