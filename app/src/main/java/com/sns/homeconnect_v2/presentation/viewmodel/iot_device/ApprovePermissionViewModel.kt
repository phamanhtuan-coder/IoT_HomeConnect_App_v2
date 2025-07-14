package com.sns.homeconnect_v2.presentation.viewmodel.iot_device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.domain.usecase.iot_device.ApproveSharePermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ApprovePermissionViewModel @Inject constructor(
    private val approveSharePermissionUseCase: ApproveSharePermissionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<PermissionUiState>(PermissionUiState.Idle)
    val state = _state.asStateFlow()

    fun approve(ticketId: String) {
        _state.value = PermissionUiState.Loading
        viewModelScope.launch {
            approveSharePermissionUseCase(ticketId).fold(
                onSuccess = {
                    _state.value = PermissionUiState.Success
                },
                onFailure = {
                    _state.value = PermissionUiState.Error(it.message ?: "Có lỗi xảy ra")
                }
            )
        }
    }
}

sealed class PermissionUiState {
    object Idle : PermissionUiState()
    object Loading : PermissionUiState()
    object Success : PermissionUiState()
    data class Error(val message: String) : PermissionUiState()
}
