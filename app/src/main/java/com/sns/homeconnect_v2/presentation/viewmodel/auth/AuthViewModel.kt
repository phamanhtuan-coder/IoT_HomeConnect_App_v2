package com.sns.homeconnect_v2.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.domain.usecase.auth.RecoveryPasswordResult
import com.sns.homeconnect_v2.domain.usecase.auth.RecoveryPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RecoveryPasswordUiState {
    data object Idle : RecoveryPasswordUiState()
    data object Loading : RecoveryPasswordUiState()
    data class Success(val message: String) : RecoveryPasswordUiState()
    data class Error(val message: String) : RecoveryPasswordUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val recoveryPasswordUseCase: RecoveryPasswordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<RecoveryPasswordUiState>(RecoveryPasswordUiState.Idle)
    val uiState: StateFlow<RecoveryPasswordUiState> = _uiState

    fun recoveryPassword(email: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = RecoveryPasswordUiState.Loading
            when (val result = recoveryPasswordUseCase(email, newPassword)) {
                is RecoveryPasswordResult.Success ->
                    _uiState.value = RecoveryPasswordUiState.Success(result.message)
                is RecoveryPasswordResult.Failure ->
                    _uiState.value = RecoveryPasswordUiState.Error(result.message)
            }
        }
    }
}