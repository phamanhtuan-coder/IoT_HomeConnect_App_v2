package com.sns.homeconnect_v2.presentation.viewmodel.iot_device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.BulkDeviceStateUpdateRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateDeviceStateRequest
import com.sns.homeconnect_v2.data.remote.dto.response.CategoryData
import com.sns.homeconnect_v2.data.remote.dto.response.ProductData
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceState
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDeviceDisplayInfoUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDeviceStateUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.UpdateDeviceStateBulkUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.UpdateDeviceStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DeviceDisplayInfoState {
    object Idle : DeviceDisplayInfoState()
    object Loading : DeviceDisplayInfoState()
    data class Success(val product: ProductData, val category: CategoryData) : DeviceDisplayInfoState()
    data class Error(val error: String) : DeviceDisplayInfoState()
}

sealed class DeviceStateUiState {
    object Idle : DeviceStateUiState()
    object Loading : DeviceStateUiState()
    data class Success(val state: DeviceState, val timestamp: String) : DeviceStateUiState()
    data class Error(val message: String) : DeviceStateUiState()
}

sealed class UpdateDeviceStateUiState {
    object Idle : UpdateDeviceStateUiState()
    object Loading : UpdateDeviceStateUiState()
    data class Success(val message: String) : UpdateDeviceStateUiState()
    data class Error(val error: String) : UpdateDeviceStateUiState()
}

sealed class UpdateDeviceStateBulkUiState {
    object Idle : UpdateDeviceStateBulkUiState()
    object Loading : UpdateDeviceStateBulkUiState()
    data class Success(val message: String) : UpdateDeviceStateBulkUiState()
    data class Error(val error: String) : UpdateDeviceStateBulkUiState()
}

@HiltViewModel
class DeviceDisplayViewModel @Inject constructor(
    private val getDeviceDisplayInfoUseCase: GetDeviceDisplayInfoUseCase,
    private val getDeviceStateUseCase: GetDeviceStateUseCase,
    private val updateDeviceStateUseCase: UpdateDeviceStateUseCase,
    private val updateDeviceStateBulkUseCase: UpdateDeviceStateBulkUseCase
) : ViewModel() {

    // ---------- 1. DISPLAY INFO ----------
    private val _displayState =
        MutableStateFlow<DeviceDisplayInfoState>(DeviceDisplayInfoState.Idle)
    val displayState: StateFlow<DeviceDisplayInfoState> = _displayState

    fun fetchDisplayInfo(templateId: String) {
        _displayState.value = DeviceDisplayInfoState.Loading
        viewModelScope.launch {
            getDeviceDisplayInfoUseCase(templateId)
                .onSuccess {
                    _displayState.value =
                        DeviceDisplayInfoState.Success(it.product, it.category)
                }
                .onFailure {
                    _displayState.value =
                        DeviceDisplayInfoState.Error(it.message ?: "Unknown error")
                }
        }
    }

    // ---------- 2. DEVICE STATE ----------
    private val _deviceState =
        MutableStateFlow<DeviceStateUiState>(DeviceStateUiState.Idle)
    val deviceState: StateFlow<DeviceStateUiState> = _deviceState

    fun fetchDeviceState(deviceId: String, serial: String) {
        _deviceState.value = DeviceStateUiState.Loading
        viewModelScope.launch {
            getDeviceStateUseCase(deviceId, serial)
                .fold(
                    onSuccess = {
                        _deviceState.value =
                            DeviceStateUiState.Success(it.state, it.timestamp)
                    },
                    onFailure = {
                        _deviceState.value =
                            DeviceStateUiState.Error(it.message ?: "Không thể lấy trạng thái thiết bị")
                    }
                )
        }
    }

    // ---------- 3. UPDATE STATE (single) ----------
    private val _updateDeviceState =
        MutableStateFlow<UpdateDeviceStateUiState>(UpdateDeviceStateUiState.Idle)
    val updateDeviceState: StateFlow<UpdateDeviceStateUiState> = _updateDeviceState

    fun updateDeviceState(
        deviceId: String,
        serial: String,
        power: Boolean? = null,
        brightness: Int? = null,
        color: String? = null
    ) {
        _updateDeviceState.value = UpdateDeviceStateUiState.Loading
        val req = UpdateDeviceStateRequest(serial, power, brightness, color)
        viewModelScope.launch {
            updateDeviceStateUseCase(deviceId, req)
                .fold(
                    onSuccess = {
                        _updateDeviceState.value =
                            UpdateDeviceStateUiState.Success(it.message)
                    },
                    onFailure = {
                        _updateDeviceState.value =
                            UpdateDeviceStateUiState.Error(it.message ?: "Lỗi không xác định")
                    }
                )
        }
    }

    // ---------- 4. UPDATE STATE (bulk) ----------
    private val _updateBulk =
        MutableStateFlow<UpdateDeviceStateBulkUiState>(UpdateDeviceStateBulkUiState.Idle)
    val updateBulk: StateFlow<UpdateDeviceStateBulkUiState> = _updateBulk

    fun updateDeviceStateBulk(
        deviceId: String,
        serial: String,
        updates: List<Map<String, Any>>
    ) {
        _updateBulk.value = UpdateDeviceStateBulkUiState.Loading
        val req = BulkDeviceStateUpdateRequest(serial, updates)
        viewModelScope.launch {
            updateDeviceStateBulkUseCase(deviceId, req)
                .fold(
                    onSuccess = {
                        _updateBulk.value =
                            UpdateDeviceStateBulkUiState.Success(it.message ?: "Thành công")
                    },
                    onFailure = {
                        _updateBulk.value =
                            UpdateDeviceStateBulkUiState.Error(it.message ?: "Lỗi không xác định")
                    }
                )
        }
    }
}