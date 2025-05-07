package com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_fire

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetInfoDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.ToggleDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.UnlinkDeviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class GetInfoDeviceState {
    data object Idle : GetInfoDeviceState()               // Chưa làm gì
    data object Loading : GetInfoDeviceState()            // Đang loading
    data class Success(val device: DeviceResponse) : GetInfoDeviceState()
    data class Error(val error: String) : GetInfoDeviceState()
}

sealed class ToggleState {
    data object Idle : ToggleState()               // Chưa làm gì
    data object Loading : ToggleState()            // Đang loading
    data class Success(val toggle: ToggleResponse) : ToggleState()
    data class Error(val error: String) : ToggleState()
}

sealed class UnlinkState {
    data object Idle : UnlinkState()               // Chưa làm gì
    data object Loading : UnlinkState()           // Đang loading
    data class Success(val message: String) : UnlinkState()
    data class Error(val error: String) : UnlinkState()
}


@HiltViewModel
class FireAlarmDetailViewModel @Inject constructor(
    private val getInfoDeviceUseCase: GetInfoDeviceUseCase,
    private val toggleDeviceUseCase: ToggleDeviceUseCase,
    private val unlinkDeviceUseCase: UnlinkDeviceUseCase,
) : ViewModel() {

    private val _infoDeviceState = MutableStateFlow<GetInfoDeviceState>(GetInfoDeviceState.Idle)
    val infoDeviceState = _infoDeviceState.asStateFlow()

    /**
     * Lấy danh sách Spaces theo Home ID
     */
    fun getInfoDevice(deviceId: Int) {
        _infoDeviceState.value = GetInfoDeviceState.Loading
        viewModelScope.launch {
            getInfoDeviceUseCase(deviceId).fold(
                onSuccess = { response ->
                    Log.d("DeviceDetailViewModel", "Success: $response")
                    _infoDeviceState.value = GetInfoDeviceState.Success(response)
                },
                onFailure = { e ->
                    Log.e("DeviceDetailViewModel", "Error: ${e.message}")
                    _infoDeviceState.value = GetInfoDeviceState.Error(e.message ?: "Load thất bại!")
                }
            )
        }
    }

    private val _toggleState = MutableStateFlow<ToggleState>(ToggleState.Idle)
    val toggleState = _toggleState.asStateFlow()

    fun toggleDevice(deviceId: Int, toggle: ToggleRequest) {
        _toggleState.value = ToggleState.Loading
        viewModelScope.launch {
            toggleDeviceUseCase(deviceId, toggle).fold(
                onSuccess = { response ->
                    Log.d("DeviceDetailViewModel", "Success: $response")
                    _toggleState.value = ToggleState.Success(response)
                },
                onFailure = { e ->
                    Log.e("DeviceDetailViewModel", "Error: ${e.message}")
                    _toggleState.value =
                        ToggleState.Error(e.message ?: "Lỗi khi cập nhật trạng thái thiết bị!")
                }
            )
        }
    }

    private val _unlinkState = MutableStateFlow<UnlinkState>(UnlinkState.Idle)
    val unlinkState = _unlinkState.asStateFlow()

    fun unlinkDevice(deviceId: Int) {
        _unlinkState.value = UnlinkState.Loading
        viewModelScope.launch {
            unlinkDeviceUseCase(deviceId).fold(
                onSuccess = { response ->
                    Log.d("DeviceDetailViewModel", "Success: $response")
                    _unlinkState.value = UnlinkState.Success(response.message)
                },
                onFailure = { e ->
                    Log.e("DeviceDetailViewModel", "Error: ${e.message}")
                    _unlinkState.value = UnlinkState.Error(e.message ?: "Lỗi khi xóa thiết bị!")
                }
            )
        }
    }
}