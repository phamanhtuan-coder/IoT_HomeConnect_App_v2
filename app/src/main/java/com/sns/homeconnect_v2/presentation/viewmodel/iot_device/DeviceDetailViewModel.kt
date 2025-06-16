package com.sns.homeconnect_v2.presentation.viewmodel.iot_device

import android.util.Log
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DeviceDisplayInfoState {
    data object Idle : DeviceDisplayInfoState()
    data object Loading : DeviceDisplayInfoState()
    data class Success(
        val product: ProductData,
        val category: CategoryData
    ) : DeviceDisplayInfoState()
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

    private val _deviceDisplayInfoState = MutableStateFlow<DeviceDisplayInfoState>(DeviceDisplayInfoState.Idle)
    val deviceDisplayInfoState = _deviceDisplayInfoState.asStateFlow()

    private var lastFetchedTemplateId: Int? = null

    fun getDeviceDisplayInfo(templateId: Int) {
        // Ngăn gọi lại với cùng ID
        if (lastFetchedTemplateId == templateId &&
            _deviceDisplayInfoState.value is DeviceDisplayInfoState.Success
        ) return

        lastFetchedTemplateId = templateId
        _deviceDisplayInfoState.value = DeviceDisplayInfoState.Loading

        viewModelScope.launch {
            getDeviceDisplayInfoUseCase(templateId).fold(
                onSuccess = { result ->
                    _deviceDisplayInfoState.value = DeviceDisplayInfoState.Success(
                        product = result.product,
                        category = result.category
                    )
                },
                onFailure = { error ->
                    _deviceDisplayInfoState.value = DeviceDisplayInfoState.Error(
                        error.message ?: "Không thể lấy thông tin sản phẩm"
                    )
                }
            )
        }
    }

    private val _deviceStateUiState = MutableStateFlow<DeviceStateUiState>(DeviceStateUiState.Idle)
    val deviceStateUiState: StateFlow<DeviceStateUiState> = _deviceStateUiState

    fun fetchDeviceState(deviceId: String, serialNumber: String) {
        _deviceStateUiState.value = DeviceStateUiState.Loading

        viewModelScope.launch {
            getDeviceStateUseCase(deviceId, serialNumber).fold(
                onSuccess = { response ->
                    _deviceStateUiState.value = DeviceStateUiState.Success(
                        state = response.state,
                        timestamp = response.timestamp
                    )
                },
                onFailure = { error ->
                    _deviceStateUiState.value = DeviceStateUiState.Error(
                        error.message ?: "Không thể lấy trạng thái thiết bị"
                    )
                }
            )
        }
    }

    private val _updateDeviceStateUiState = MutableStateFlow<UpdateDeviceStateUiState>(UpdateDeviceStateUiState.Idle)
    val updateDeviceStateUiState: StateFlow<UpdateDeviceStateUiState> = _updateDeviceStateUiState

    fun updateDeviceState(
        deviceId: String,
        serial: String,
        power: Boolean? = null,
        brightness: Int? = null,
        color: String? = null
    ) {
        _updateDeviceStateUiState.value = UpdateDeviceStateUiState.Loading

        val request = UpdateDeviceStateRequest(
            serial_number = serial,
            power_status = power,
            brightness = brightness,
            color = color
        )

        viewModelScope.launch {
            updateDeviceStateUseCase(deviceId, request).fold(
                onSuccess = { response ->
                    _updateDeviceStateUiState.value = UpdateDeviceStateUiState.Success(response.message)
                    Log.d("UpdateDeviceState", "Thành công: ${'$'}{response.message}")
                },
                onFailure = { error ->
                    _updateDeviceStateUiState.value = UpdateDeviceStateUiState.Error(
                        error.message ?: "Lỗi không xác định"
                    )
                    Log.e("UpdateDeviceState", "Thất bại: ${'$'}{error.message}")
                }
            )
        }
    }

    private val _updateDeviceStateBulkUiState = MutableStateFlow<UpdateDeviceStateBulkUiState>(UpdateDeviceStateBulkUiState.Idle)
    val updateDeviceStateBulkUiState: StateFlow<UpdateDeviceStateBulkUiState> = _updateDeviceStateBulkUiState

    fun updateDeviceStateBulk(
        deviceId: String,
        serial: String,
        updates: List<Map<String, Any>>
    ) {
        _updateDeviceStateBulkUiState.value = UpdateDeviceStateBulkUiState.Loading

        val request = BulkDeviceStateUpdateRequest(
            serial_number = serial,
            updates = updates
        )

        viewModelScope.launch {
            updateDeviceStateBulkUseCase(deviceId, request).fold(
                onSuccess = {
                    _updateDeviceStateBulkUiState.value = UpdateDeviceStateBulkUiState.Success(it.message ?: "Thành công")
                },
                onFailure = {
                    _updateDeviceStateBulkUiState.value = UpdateDeviceStateBulkUiState.Error(it.message ?: "Lỗi không xác định")
                }
            )
        }
    }
}

