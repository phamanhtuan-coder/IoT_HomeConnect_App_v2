package com.sns.homeconnect_v2.presentation.viewmodel.iot_device

import android.util.Log
import org.json.JSONObject
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponseSpace
import com.sns.homeconnect_v2.data.remote.dto.response.SpaceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDevicesBySpaceIdUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetSpacesByHomeIdUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.ToggleDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.socket.ConnectSocketUseCase
import com.sns.homeconnect_v2.domain.usecase.socket.ObserveSocketEventUseCase
import com.sns.homeconnect_v2.domain.usecase.socket.SendCommandUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class DeviceState {
    data object Idle : DeviceState()               // Chưa làm gì
    data object Loading : DeviceState()            // Đang loading
    data class Success(val deviceList: List<DeviceResponseSpace>) : DeviceState()
    data class Error(val error: String) : DeviceState()
}

sealed class SpaceState {
    data object Idle : SpaceState()              // Chưa làm gì
    data object Loading : SpaceState()            // Đang loading
    data class Success(val spacesList: List<SpaceResponse>) : SpaceState()
    data class Error(val error: String) : SpaceState()
}

sealed class ToggleState {
    data object Idle : ToggleState()               // Chưa làm gì
    data object Loading : ToggleState()            // Đang loading
    data class Success(val toggle: ToggleResponse) : ToggleState()
    data class Error(val error: String) : ToggleState()
}

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val toggleDeviceUseCase: ToggleDeviceUseCase,
    private val getSpacesByHomeIdUseCase: GetSpacesByHomeIdUseCase,
    private val getDevicesBySpaceIdUseCase: GetDevicesBySpaceIdUseCase,
    private val connectSocketUseCase: ConnectSocketUseCase,
    private val sendCommandUseCase: SendCommandUseCase,
    private val observeSocketEventUseCase: ObserveSocketEventUseCase,
    private val authManager: AuthManager
) : ViewModel() {
    private val _toggleState = MutableStateFlow<ToggleState>(ToggleState.Idle)
    val toggleState = _toggleState.asStateFlow()

    fun toggleDevice(deviceId: Int, toggle: ToggleRequest) {
        _toggleState.value = ToggleState.Loading
        viewModelScope.launch {
            toggleDeviceUseCase(deviceId, toggle).fold(
                onSuccess = { response ->
                    _toggleState.value = ToggleState.Success(response)
                },
                onFailure = { error ->
                    Log.e("DeviceViewModel", "Lỗi toggle device : ${error.message}")
                    _toggleState.value =
                        ToggleState.Error(error.message ?: "Toggling device thất bại!")
                }
            )
        }
    }

    private val _spacesListState = MutableStateFlow<SpaceState>(SpaceState.Idle)
    val spacesListState = _spacesListState.asStateFlow()

    /**
     * Lấy danh sách Spaces theo Home ID
     */
    fun getSpacesByHomeId(homeId: Int) {
        _spacesListState.value = SpaceState.Loading
        viewModelScope.launch {
            getSpacesByHomeIdUseCase(homeId).fold(
                onSuccess = { response ->
                    _spacesListState.value = SpaceState.Success(response)
                },
                onFailure = { error ->
                    Log.e("DeviceViewModel", "Error fetching spaces: ${error.message}")
                    _spacesListState.value =
                        SpaceState.Error(error.message ?: "Danh sách không tải được!")
                }
            )
        }
    }

    private val _deviceListState = MutableStateFlow<DeviceState>(DeviceState.Idle)
    val deviceListState = _deviceListState.asStateFlow()

    /**
     * Lấy danh sách thiết bị theo Space ID
     */
    fun getDevicesBySpaceId(spaceId: Int) {
        _deviceListState.value = DeviceState.Loading
        viewModelScope.launch {
            getDevicesBySpaceIdUseCase(spaceId).fold(
                onSuccess = { response ->
                    _deviceListState.value = DeviceState.Success(response)
                },
                onFailure = { error ->
                    Log.e("DeviceViewModel", "Error fetching devices: ${error.message}")
                    _deviceListState.value =
                        DeviceState.Error(error.message ?: "Danh sách không tải được!")
                }
            )
        }
    }

    val alarmAlert = mutableStateOf<JSONObject?>(null)

    val sensorData = mutableStateOf<JSONObject?>(null)

    val deviceStatus = mutableStateOf<JSONObject?>(null)

    fun initSocket(deviceId: String, serial: String) {
        val accId = authManager.getAccountId()

        connectSocketUseCase(deviceId, serial, accId)

        observeSocketEventUseCase { evt, data ->
            when (evt) {
                "device_online"        -> Log.d("SOCKET", "✅ Online $data")
                "sensorData"           -> sensorData.value   = data
                "alarmAlert"           -> alarmAlert.value   = data
                "deviceStatus"         -> deviceStatus.value = data
                "buzzerStatus"         -> Log.d("SOCKET", "🔔 Buzzer $data")
                "realtime_device_value"-> Log.d("SOCKET", "📡 Value  $data")
            }
        }
    }

    fun sendPowerCommand(power: Boolean) {
        val command = JSONObject().apply {
            put("action", "updateState")
            put("state", JSONObject().apply {
                put("power_status", power)
            })
            put("timestamp", System.currentTimeMillis())
        }

        sendCommandUseCase(command)
    }
}