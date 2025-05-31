package com.sns.homeconnect_v2.presentation.viewmodel.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.domain.usecase.SendFcmTokenUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val sendFcmTokenUseCase: SendFcmTokenUseCase,
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        _loginState.value = LoginUiState.Loading

        viewModelScope.launch {
            loginUseCase(email, password).fold(
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

    suspend fun quickLogin(email: String, password: String): Result<String> {
        // 1. Gọi use-case đăng nhập
        val loginResult = loginUseCase(email, password)

        // 2. Nếu thành công → gửi FCM rồi trả tiếp Result
        return loginResult.mapCatching { token ->
            // gửi FCM, bỏ qua lỗi (không ảnh hưởng đăng nhập)
            runCatching {
                val fcm = FirebaseMessaging
                    .getInstance().token.await()
                sendFcmTokenUseCase(fcm)
            }
            token
        }
    }

    private fun sendFcmTokenToServer(onSuccess: () -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("LoginViewModel", "Fetching FCM token failed", task.exception)
                onSuccess() // Proceed even if FCM token fetch fails
                return@addOnCompleteListener
            }

            val fcmToken = task.result
            Log.d("LoginViewModel", "FCM Token: $fcmToken")

            viewModelScope.launch {
                sendFcmTokenUseCase(fcmToken).fold(
                    onSuccess = { message ->
                        Log.d("LoginViewModel", "FCM Token sent to server: $message")
                        onSuccess()
                    },
                    onFailure = { e ->
                        Log.e("LoginViewModel", "Failed to send FCM token: ${e.message}")
                        onSuccess() // Proceed even if FCM token send fails
                    }
                )
            }
        }
    }
}