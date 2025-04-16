package com.sns.homeconnect_v2.domain.usecase.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.EmailRequest
import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse
import com.sns.homeconnect_v2.domain.repository.OTPRepository
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
    private val otpRepository: OTPRepository
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
            try {
                val response = otpRepository.sendOTP(email)
                _sendOtpState.value = OTPState.Success(response.success, response.message)
            } catch (e: Exception) {
                _sendOtpState.value = OTPState.Error(e.message ?: "Failed to send OTP")
            }
        }
    }

    fun verifyOTP(email: String, otp: String) {
        _verifyOtpState.value = OTPState.Loading
        viewModelScope.launch {
            try {
                val response = otpRepository.verifyOTP(email, otp)
                _verifyOtpState.value = OTPState.Success(response.success, response.message)
            } catch (e: Exception) {
                _verifyOtpState.value = OTPState.Error(e.message ?: "OTP verification failed")
            }
        }
    }

    fun confirmEmail(email: String) {
        _verifyEmailState.value = VerifyEmailState.Loading
        viewModelScope.launch {
            try {
                val request = EmailRequest(email = email)
                val response = otpRepository.verifyOTP(email, "") // Placeholder; replace with actual confirmEmail call if separate endpoint exists
                _verifyEmailState.value = VerifyEmailState.Success(response)
            } catch (e: Exception) {
                _verifyEmailState.value = VerifyEmailState.Error(e.message ?: "Email verification failed")
            }
        }
    }
}