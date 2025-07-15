package com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_door

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleDoorRequest
import com.sns.homeconnect_v2.data.remote.dto.response.DoorStatus
import com.sns.homeconnect_v2.data.remote.dto.response.DoorToggleResponse
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDoorStatusUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.ToggleDoorPowerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoorViewModel @Inject constructor(
    private val getDoorStatusUseCase: GetDoorStatusUseCase,
    private val toggleDoorPowerUseCase: ToggleDoorPowerUseCase
) : ViewModel() {

    private val _doorStatus = MutableStateFlow<ApiResult<DoorStatus>>(ApiResult.Idle)
    val doorStatus: StateFlow<ApiResult<DoorStatus>> = _doorStatus.asStateFlow()

    fun fetchDoorStatus(serialNumber: String) {
        viewModelScope.launch {
            _doorStatus.value = ApiResult.Loading
            try {
                val response = getDoorStatusUseCase(serialNumber)
                _doorStatus.value = ApiResult.Success(response.door_status)
            } catch (e: Exception) {
                _doorStatus.value = ApiResult.Error(e.message ?: "Không thể lấy trạng thái cửa")
            }
        }
    }

    private val _toggleState = MutableStateFlow<ApiResult<DoorToggleResponse>>(ApiResult.Idle)
    val toggleState: StateFlow<ApiResult<DoorToggleResponse>> = _toggleState.asStateFlow()

    fun toggleDoorPower(serialNumber: String, power: Boolean) {
        viewModelScope.launch {
            _toggleState.value = ApiResult.Loading
            try {
                val request = ToggleDoorRequest(
                    power_status = power,
                    force = false,
                    timeout = 30000
                )
                val response = toggleDoorPowerUseCase(serialNumber, request)
                _toggleState.value = ApiResult.Success(response)
            } catch (e: Exception) {
                _toggleState.value = ApiResult.Error(e.message ?: "Không thể gửi lệnh toggle")
            }
        }
    }
}



sealed class ApiResult<out T> {
    object Idle : ApiResult<Nothing>()             // Mặc định chưa làm gì
    object Loading : ApiResult<Nothing>()          // Đang loading
    data class Success<T>(val data: T) : ApiResult<T>()   // Thành công
    data class Error(val message: String) : ApiResult<Nothing>()  // Lỗi
}