package com.sns.homeconnect_v2.presentation.viewmodel.otp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse
import com.sns.homeconnect_v2.domain.usecase.auth.CheckEmailUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.ConfirmEmailUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.SendOtpUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.VerifyOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class OTPState {
    object Idle : OTPState()
    object Loading : OTPState()
    data class Success(val success: Boolean, val message: String) : OTPState()
    data class Error(val message: String) : OTPState()
}

sealed class VerifyEmailState {
    data object Idle : VerifyEmailState()
    data object Loading : VerifyEmailState()
    data class Success(val emailResponse: EmailResponse) : VerifyEmailState()
    data class Error(val message: String) : VerifyEmailState()
}

@HiltViewModel
class OTPViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val confirmEmailUseCase: ConfirmEmailUseCase
) : ViewModel() {

    private val _sendOtpState = MutableStateFlow<OTPState>(OTPState.Idle)
    val sendOtpState = _sendOtpState.asStateFlow()

    private val _verifyOtpState = MutableStateFlow<OTPState>(OTPState.Idle)
    val verifyOtpState = _verifyOtpState.asStateFlow()

    private val _verifyEmailState = MutableStateFlow<VerifyEmailState>(VerifyEmailState.Idle)
    val verifyEmailState = _verifyEmailState.asStateFlow()

    fun sendOTP(email: String) {
        _sendOtpState.value = OTPState.Loading
        viewModelScope.launch {
            sendOtpUseCase(email).fold(
                onSuccess = { response ->
                    _sendOtpState.value = OTPState.Success(response.success, response.message)
                },
                onFailure = { e ->
                    _sendOtpState.value = OTPState.Error(e.message ?: "Failed to send OTP")
                }
            )
        }
    }

    fun verifyOTP(email: String, otp: String) {
        _verifyOtpState.value = OTPState.Loading
        viewModelScope.launch {
            verifyOtpUseCase(email, otp).fold(
                onSuccess = { response ->
                    _verifyOtpState.value = OTPState.Success(response.success, response.message)
                },
                onFailure = { e ->
                    _verifyOtpState.value = OTPState.Error(e.message ?: "OTP verification failed")
                }
            )
        }
    }


    fun confirmEmail(email: String) {
        // Reset state
        _verifyEmailState.value = VerifyEmailState.Loading
        viewModelScope.launch {
           confirmEmailUseCase(email).fold(
                onSuccess = { response ->
                    Log.d("OTPViewModel", "Success: ${response.message}")
                    _verifyEmailState.value = VerifyEmailState.Success(response)
                },
                onFailure = { e ->
                    Log.e("OTPViewModel", "Error: ${e.message}")
                    _verifyEmailState.value = VerifyEmailState.Error(e.message ?: "Email confirmation failed")
                }
            )
        }
    }
}