package com.sns.homeconnect_v2.presentation.viewmodel.iot_device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.CategoryData
import com.sns.homeconnect_v2.data.remote.dto.response.ProductData
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceState
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDeviceDisplayInfoUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDeviceStateUseCase
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

@HiltViewModel
class DeviceDisplayViewModel @Inject constructor(
    private val getDeviceDisplayInfoUseCase: GetDeviceDisplayInfoUseCase,
    private val getDeviceStateUseCase: GetDeviceStateUseCase
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
}

