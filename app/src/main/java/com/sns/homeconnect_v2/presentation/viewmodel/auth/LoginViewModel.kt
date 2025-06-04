package com.sns.homeconnect_v2.presentation.viewmodel.auth

import android.app.Application
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.request.LoginRequest
import com.sns.homeconnect_v2.data.remote.dto.response.LoginResponse
import com.sns.homeconnect_v2.domain.usecase.SendFcmTokenUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val loginUseCase: LoginUseCase,
    private val sendFcmTokenUseCase: SendFcmTokenUseCase,
    private val apiService: ApiService,
    private val authManager: AuthManager
) : AndroidViewModel(application) {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(username: String, password: String) {
        _loginState.value = LoginUiState.Loading

        viewModelScope.launch {
            loginUseCase(username, password).fold(
                onSuccess = { token ->
                    sendFcmTokenToServer {
                        _loginState.value = LoginUiState.Success(token)
                    }
                },
                onFailure = { e ->
                    Log.e("LoginViewModel", "Login error: ${e.message}")
                    _loginState.value = LoginUiState.Error(e.message ?: "Đăng nhập thất bại!")
                }
            )
        }
    }

    suspend fun quickLogin(username: String, password: String): Result<LoginResponse> {
        return runCatching {
            val context = getApplication<Application>()
            val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            val deviceUuid = authManager.getDeviceUuid()

            val request = LoginRequest(
                username = username,
                password = password,
                rememberMe = true,
                deviceName = "Android_${Build.MODEL}",
                deviceId = deviceId,
                deviceUuid = deviceUuid
            )

            val response = apiService.login(request)

            authManager.saveJwtToken(response.accessToken)
            authManager.saveRefreshToken(response.refreshToken)
            authManager.saveDeviceUuid(response.deviceUuid)

            response
        }
    }

    private fun sendFcmTokenToServer(onSuccess: () -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("LoginViewModel", "Fetching FCM token failed", task.exception)
                onSuccess()
                return@addOnCompleteListener
            }

            val fcmToken = task.result
            Log.d("LoginViewModel", "FCM Token: $fcmToken")

            viewModelScope.launch {
                sendFcmTokenUseCase(fcmToken).fold(
                    onSuccess = {
                        Log.d("LoginViewModel", "FCM Token sent to server")
                        onSuccess()
                    },
                    onFailure = {
                        Log.e("LoginViewModel", "Failed to send FCM token: ${it.message}")
                        onSuccess()
                    }
                )
            }
        }
    }
}
