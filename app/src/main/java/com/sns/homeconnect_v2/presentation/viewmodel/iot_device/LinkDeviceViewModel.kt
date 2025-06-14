package com.sns.homeconnect_v2.presentation.viewmodel.iot_device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.LinkDeviceRequest
import com.sns.homeconnect_v2.data.remote.dto.response.LinkDeviceResponse
import com.sns.homeconnect_v2.domain.usecase.iot_device.LinkDeviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LinkDeviceUiState {
    object Idle : LinkDeviceUiState()
    object Loading : LinkDeviceUiState()
    data class Success(val device: LinkDeviceResponse): LinkDeviceUiState()
    data class Error(val error: String): LinkDeviceUiState()
}

@HiltViewModel
class LinkDeviceViewModel @Inject constructor(
    private val linkDeviceUseCase: LinkDeviceUseCase
) : ViewModel() {

    private val _linkDeviceState = MutableStateFlow<LinkDeviceUiState>(LinkDeviceUiState.Idle)
    val linkDeviceState: StateFlow<LinkDeviceUiState> = _linkDeviceState

    fun linkDevice(request: LinkDeviceRequest) {
        _linkDeviceState.value = LinkDeviceUiState.Loading
        viewModelScope.launch {
            linkDeviceUseCase(request).fold(
                onSuccess = { device ->
                    _linkDeviceState.value = LinkDeviceUiState.Success(device)
                },
                onFailure = { e ->
                    _linkDeviceState.value = LinkDeviceUiState.Error(e.message ?: "Có lỗi xảy ra!")
                }
            )
        }
    }
}
