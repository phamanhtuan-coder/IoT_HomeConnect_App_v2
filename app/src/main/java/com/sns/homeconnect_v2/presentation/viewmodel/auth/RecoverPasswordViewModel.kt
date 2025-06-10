package com.sns.homeconnect_v2.presentation.viewmodel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse
import com.sns.homeconnect_v2.domain.usecase.auth.CheckEmailUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.SendOtpUseCase
import com.sns.homeconnect_v2.presentation.model.RecoverPasswordUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RecoverPasswordState {
    data object Idle : RecoverPasswordState()
    data object Loading : RecoverPasswordState()
    data class Success(val message: String) : RecoverPasswordState()
    data class Error(val message: String) : RecoverPasswordState()
}

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val checkEmailUseCase: CheckEmailUseCase,
    private val sendOtpUseCase: SendOtpUseCase,
) : ViewModel() {

    private val _checkEmailState = MutableStateFlow<RecoverPasswordState>(RecoverPasswordState.Idle)
    val checkEmailState = _checkEmailState.asStateFlow()

    private val _uiModel = MutableStateFlow(RecoverPasswordUiModel())
    val uiModel = _uiModel.asStateFlow()

    fun updateUiModel(newModel: RecoverPasswordUiModel) {
        _uiModel.value = newModel
    }

    fun checkEmail() {
        _checkEmailState.value = RecoverPasswordState.Loading
        viewModelScope.launch {
            val uiModel = _uiModel.value
            checkEmailUseCase(uiModel.email).fold(
                onSuccess = { response ->
                    Log.d("RecoverPasswordViewModel", "Success: ${response.message}")
                    _checkEmailState.value = RecoverPasswordState.Success(response.message)
                },
                onFailure = { e ->
                    Log.e("RecoverPasswordViewModel", "Error: ${e.message}")
                    _checkEmailState.value = RecoverPasswordState.Error(e.message ?: "Xác thực email thất bại!")
                }
            )
        }
    }

    suspend fun sendOtp(email: String): Result<EmailResponse> {
        _checkEmailState.value = RecoverPasswordState.Loading
        return sendOtpUseCase(email).also { result ->
            result.fold(
                onSuccess = { response ->
                    _checkEmailState.value = RecoverPasswordState.Success(response.message)
                },
                onFailure = { e ->
                    _checkEmailState.value = RecoverPasswordState.Error(e.message ?: "Failed to send OTP")
                }
            )
        }
    }
}