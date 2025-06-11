package com.sns.homeconnect_v2.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.domain.usecase.auth.CheckEmailResult
import com.sns.homeconnect_v2.domain.usecase.auth.CheckEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckEmailViewModel @Inject constructor(
    private val useCase: CheckEmailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CheckEmailUiState>(CheckEmailUiState.Idle)
    val uiState: StateFlow<CheckEmailUiState> = _uiState

    fun checkEmail(
        email: String,
        ok: (String) -> Unit,
        err: (String) -> Unit
    ) {
        viewModelScope.launch {
            useCase(email).fold(
                onSuccess = { result ->
                    when (result) {
                        is CheckEmailResult.Verified    -> ok("Email hợp lệ, chuyển OTP")
                        is CheckEmailResult.NotFound    -> err("Email không tồn tại")
                        is CheckEmailResult.NotVerified -> err("Email chưa được xác thực")
                        is CheckEmailResult.Failure     -> err(result.message) // 👈 bắt buộc có dòng này để tránh lỗi
                    }
                },
                onFailure = { e ->
                    err(e.message ?: "Có lỗi xảy ra khi kiểm tra email")
                }
            )
        }
    }
}

sealed class CheckEmailUiState {
    object Idle : CheckEmailUiState()
    object Loading : CheckEmailUiState()
    object Success : CheckEmailUiState()
    data class Error(val message: String) : CheckEmailUiState()
}
