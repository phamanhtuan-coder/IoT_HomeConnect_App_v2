package com.sns.homeconnect_v2.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.CheckEmailResponse
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
            when (val res = useCase(email)) {           // UseCase đã tính NotFound, NotVerified, Verified
                CheckEmailResult.Verified    -> ok("Email hợp lệ, chuyển OTP")
                CheckEmailResult.NotFound    -> err("Email không tồn tại")
                CheckEmailResult.NotVerified -> err("Email chưa được xác thực")
                is CheckEmailResult.Failure  -> err(res.message)
            }
        }
    }
}

sealed class CheckEmailUiState {
    object Idle : CheckEmailUiState()
    object Loading : CheckEmailUiState()
    object Success : CheckEmailUiState()
    data class Error(val message: String) : CheckEmailUiState()
}
