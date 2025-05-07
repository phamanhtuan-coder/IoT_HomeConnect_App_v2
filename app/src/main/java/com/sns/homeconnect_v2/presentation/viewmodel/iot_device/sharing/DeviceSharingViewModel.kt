package com.sns.homeconnect_v2.presentation.viewmodel.iot_device.sharing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.SharedUser
import com.sns.homeconnect_v2.domain.usecase.iot_device.sharing.AddSharedUserUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.sharing.GetSharedUsersUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.sharing.RevokePermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DeviceSharingState {
    data object Idle : DeviceSharingState()// Chưa làm gì
    data object Loading : DeviceSharingState()            // Đang loading
    data class Success(val sharedUsers: List<SharedUser>) : DeviceSharingState()
    data class Error(val error: String) : DeviceSharingState()
}

sealed class DeviceSharingActionState {
    data object Idle : DeviceSharingActionState()               // Chưa làm gì
    data object Loading : DeviceSharingActionState()            // Đang loading
    data class Success(val success: String) : DeviceSharingActionState()
    data class Error(val error: String) : DeviceSharingActionState()
}

@HiltViewModel
class DeviceSharingViewModel @Inject constructor (
    private val getSharedUsersUseCase: GetSharedUsersUseCase,
    private val addSharedUserUseCase: AddSharedUserUseCase,
    private val revokePermissionUseCase: RevokePermissionUseCase,
) : ViewModel() {
    private val _sharedUsersState = MutableStateFlow<DeviceSharingState>(DeviceSharingState.Idle)
    val sharedUsersState = _sharedUsersState.asStateFlow()

    fun getSharedUsers(deviceID: Int) {
        _sharedUsersState.value = DeviceSharingState.Loading

        viewModelScope.launch {
             getSharedUsersUseCase(deviceID).fold(
                    onSuccess = { response ->
                        _sharedUsersState.value = DeviceSharingState.Success(response)
                    },
                    onFailure = { error ->
                        _sharedUsersState.value =
                            DeviceSharingState.Error(error.message ?: "Lỗi tải danh sách người dùng chia sẻ!")
                    }
                )
            }
    }

    private val _addSharedUserState = MutableStateFlow<DeviceSharingActionState>(DeviceSharingActionState.Idle)
    val addSharedUserState = _addSharedUserState.asStateFlow()

    fun addSharedUser(deviceId: Int,sharedWithUser: String) {
        _addSharedUserState.value = DeviceSharingActionState.Loading
        viewModelScope.launch {
            addSharedUserUseCase(deviceId, sharedWithUser).fold(
                onSuccess = { response ->
                    _addSharedUserState.value =
                        DeviceSharingActionState.Success("Thêm người dùng chia sẻ thành công!")
                },
                onFailure = { error ->
                    _addSharedUserState.value =
                        DeviceSharingActionState.Error(error.message ?: "Lỗi thêm người dùng chia sẻ!")
                }
            )
        }
    }

    private val _revokePermissionState =
        MutableStateFlow<DeviceSharingActionState>(DeviceSharingActionState.Idle)
    val revokePermissionState = _revokePermissionState.asStateFlow()

    fun revokePermission(permissionID: Int) {
        _revokePermissionState.value = DeviceSharingActionState.Loading

        viewModelScope.launch {
            revokePermissionUseCase(permissionID).fold(
                onSuccess = { response ->
                    _revokePermissionState.value =
                        DeviceSharingActionState.Success("Xóa quyền chia sẻ thành công!")
                },
                onFailure = { error ->
                    _revokePermissionState.value =
                        DeviceSharingActionState.Error(error.message ?: "Lỗi xóa quyền chia sẻ!")
                }
            )
        }
    }

}