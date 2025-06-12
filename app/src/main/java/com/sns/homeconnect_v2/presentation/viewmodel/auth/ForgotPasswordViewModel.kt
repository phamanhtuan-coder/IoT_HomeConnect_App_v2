package com.sns.homeconnect_v2.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.domain.usecase.auth.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class NewPasswordUiState {
    data object Idle : NewPasswordUiState()
    data object Loading : NewPasswordUiState()
    data class Success(val message: String) : NewPasswordUiState()
    data class Error(val message: String) : NewPasswordUiState()
}

@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val newPasswordUseCase: ForgotPasswordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<NewPasswordUiState>(NewPasswordUiState.Idle)
    val uiState: StateFlow<NewPasswordUiState> = _uiState

    fun newPassword(email: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = NewPasswordUiState.Loading

            val result: Result<String> = newPasswordUseCase(email, newPassword)

            result.fold(
                onSuccess = { message: String ->
                    _uiState.value = NewPasswordUiState.Success(message)
                },
                onFailure = { e: Throwable ->
                    _uiState.value = NewPasswordUiState.Error(e.message ?: "Có lỗi xảy ra")
                }
            )
        }
    }
}