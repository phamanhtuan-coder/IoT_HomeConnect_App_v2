package com.sns.homeconnect_v2.presentation.viewmodel.iot_device.sharing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.domain.usecase.iot_device.sharing.RevokeRecipientPermissionUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.sharing.RevokeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RevokeRecipientViewModel @Inject constructor(
    private val revokeUseCase: RevokeRecipientPermissionUseCase
) : ViewModel() {

    fun reset() {
        _state.value = UiState.Idle
    }

    sealed interface UiState {
        object Idle : UiState
        object Loading : UiState
        object Success : UiState
        data class Error(val message: String) : UiState
    }

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state

    private val _snack = Channel<String>()
    val snack = _snack.receiveAsFlow()

    fun revoke(serial: String) = viewModelScope.launch {
        _state.value = UiState.Loading
        when (val res = revokeUseCase(serial)) {
            is RevokeResult.Success -> _state.value = UiState.Success
            is RevokeResult.Failure -> {
                val msg = res.message ?: "Lỗi ${res.httpCode}"
                _state.value = UiState.Error(msg)
                _snack.send(msg)
            }
        }
    }
}
