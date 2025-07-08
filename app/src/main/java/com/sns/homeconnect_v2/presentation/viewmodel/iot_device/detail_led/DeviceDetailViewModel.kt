package com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_led

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.domain.usecase.iot_device.AttributeDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetInfoDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.ToggleDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.UnlinkDeviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class GetInfoDeviceState {
    data object Idle : GetInfoDeviceState()                // Chưa làm gì
    data object Loading : GetInfoDeviceState()             // Đang loading
    data class Success(val device: DeviceResponse) : GetInfoDeviceState()
    data class Error(val error: String) : GetInfoDeviceState()
}

sealed class ToggleState {
    data object Idle : ToggleState()               // Chưa làm gì
    data object Loading : ToggleState()            // Đang loading
    data class Success(val toggle: ToggleResponse) : ToggleState()
    data class Error(val error: String) : ToggleState()
}

sealed class AttributeState {
    data object Idle : AttributeState()               // Chưa làm gì
    data object Loading : AttributeState()           // Đang loading
    data class Success(val message: String, val device: DeviceResponse) : AttributeState()
    data class Error(val error: String) : AttributeState()
}
//sealed class CalculationState {
//    data object Idle : CalculationState()
//    data object Loading : CalculationState()
//
//    // Trả về SensorData (thay vì SensorData2)
//    data class AverageSensorSuccess(
//        val message: String,
//        val data: SensorData
//    ) : CalculationState()
//
//    // Trả về PowerUsageData (thay vì PowerUsageData2)
//    data class PowerUsageSuccess(
//        val message: String,
//        val data: PowerUsageData2
//    ) : CalculationState()
//
//    data class Error(val error: String) : CalculationState()
//}

@HiltViewModel
class DeviceDetailViewModel @Inject constructor(
    private val getInfoDeviceUseCase: GetInfoDeviceUseCase,
    private val toggleDeviceUseCase: ToggleDeviceUseCase,
    private val attributeDeviceUseCase: AttributeDeviceUseCase,
) : ViewModel() {
    private val _infoDeviceState = MutableStateFlow<GetInfoDeviceState>(GetInfoDeviceState.Idle)
    val infoDeviceState = _infoDeviceState.asStateFlow()

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

    private val _attributeState = MutableStateFlow<AttributeState>(AttributeState.Idle)
    val attributeState = _attributeState.asStateFlow()

    fun attributeDevice(deviceId: Int, brightness: Int, color: String) {
        _attributeState.value = AttributeState.Loading
        viewModelScope.launch {
            attributeDeviceUseCase(deviceId, brightness, color).fold(
                onSuccess = { response ->
                    Log.d("DeviceDetailViewModel", "Success: $response")
                    _attributeState.value =
                        AttributeState.Success(response.message, response.device)
                },
                onFailure = { e ->
                    Log.e("DeviceDetailViewModel", "Error: ${e.message}")
                    _attributeState.value =
                        AttributeState.Error(e.message ?: "Lỗi khi cập nhật thuộc tính thiết bị!")
                }
            )
        }
    }

    // ---------------- Tính toán thống kê ---------------- //
//    private val _calculationState = MutableStateFlow<CalculationState>(CalculationState.Idle)
//    val calculationState: StateFlow<CalculationState> = _calculationState.asStateFlow()
//
//    fun calculateDailyAverageSensor(deviceId: Int, date: String) {
//        _calculationState.value = CalculationState.Loading
//        viewModelScope.launch {
//            try {
//                val response = repository2.calculateDailyAverageSensor(deviceId, date)
//                // response.data bây giờ là kiểu SensorData
//                _calculationState.value = CalculationState.AverageSensorSuccess(
//                    message = response.message,
//                    data = response.data
//                )
//            } catch (e: Exception) {
//                _calculationState.value =
//                    CalculationState.Error(e.message ?: "Đã xảy ra lỗi không xác định")
//            }
//        }
//    }
//
//    fun calculateDailyPowerUsage(deviceId: Int, date: String) {
//        _calculationState.value = CalculationState.Loading
//        viewModelScope.launch {
//            try {
//                val response = repository2.calculateDailyPowerUsage(deviceId, date)
//                // response.data bây giờ là kiểu PowerUsageData
//                _calculationState.value = CalculationState.PowerUsageSuccess(
//                    message = response.message,
//                    data = response.data
//                )
//            } catch (e: Exception) {
//                _calculationState.value =
//                    CalculationState.Error(e.message ?: "Đã xảy ra lỗi không xác định")
//            }
//        }
//    }
}