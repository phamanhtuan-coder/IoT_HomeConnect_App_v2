package com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_door

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.DoorStatus
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDoorStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoorViewModel @Inject constructor(
    private val getDoorStatusUseCase: GetDoorStatusUseCase
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
}

sealed class ApiResult<out T> {
    object Idle : ApiResult<Nothing>()             // Mặc định chưa làm gì
    object Loading : ApiResult<Nothing>()          // Đang loading
    data class Success<T>(val data: T) : ApiResult<T>()   // Thành công
    data class Error(val message: String) : ApiResult<Nothing>()  // Lỗi
}