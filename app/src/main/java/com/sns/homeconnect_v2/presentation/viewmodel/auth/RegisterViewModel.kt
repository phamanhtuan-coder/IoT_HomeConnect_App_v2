package com.sns.homeconnect_v2.presentation.viewmodel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.RegisterRequest
import com.sns.homeconnect_v2.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data class Success(val message: String, val user: RegisterRequest) : RegisterState()
    data class Error(val error: String) : RegisterState()
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState = _registerState.asStateFlow()

    fun register(user: RegisterRequest) {
        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            registerUseCase(user).fold(
                onSuccess = { response ->
                    Log.d("RegisterViewModel", "Registration success: ${response.message}")
                    _registerState.value = RegisterState.Success(response.message, user)
                },
                onFailure = { e ->
                    Log.e("RegisterViewModel", "Registration error: ${e.message}")
                    _registerState.value = RegisterState.Error(e.message ?: "Đăng ký thất bại!")
                }
            )
        }
    }
}