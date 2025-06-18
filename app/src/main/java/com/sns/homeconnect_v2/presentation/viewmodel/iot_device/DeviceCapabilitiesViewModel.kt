package com.sns.homeconnect_v2.presentation.viewmodel.iot_device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceCapabilitiesResponse
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDeviceCapabilitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DeviceCapabilitiesUiState {
    object Idle : DeviceCapabilitiesUiState()
    object Loading : DeviceCapabilitiesUiState()
    data class Success(val data: DeviceCapabilitiesResponse) : DeviceCapabilitiesUiState()
    data class Error(val error: String) : DeviceCapabilitiesUiState()
}

@HiltViewModel
class DeviceCapabilitiesViewModel @Inject constructor(
    private val getDeviceCapabilitiesUseCase: GetDeviceCapabilitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DeviceCapabilitiesUiState>(DeviceCapabilitiesUiState.Idle)
    val uiState: StateFlow<DeviceCapabilitiesUiState> = _uiState

    // Chỉ lấy controls từ merged_capabilities để UI dùng
    val controls: StateFlow<Map<String, String>> = _uiState
        .filterIsInstance<DeviceCapabilitiesUiState.Success>()
        .map { it.data.capabilities.merged_capabilities.controls }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun loadCapabilities(deviceId: String, serialNumber: String) {
        _uiState.value = DeviceCapabilitiesUiState.Loading
        viewModelScope.launch {
            getDeviceCapabilitiesUseCase(deviceId, serialNumber).fold(
                onSuccess = { response ->
                    _uiState.value = DeviceCapabilitiesUiState.Success(response)
                },
                onFailure = { error ->
                    _uiState.value = DeviceCapabilitiesUiState.Error(error.message ?: "Lỗi không xác định")
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = DeviceCapabilitiesUiState.Idle
    }
}
