package com.sns.homeconnect_v2.presentation.viewmodel.iot_device.sharing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.CreateTicketRequest
import com.sns.homeconnect_v2.data.remote.dto.request.Evidence
import com.sns.homeconnect_v2.data.remote.dto.response.SharedDeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.SharedUser
import com.sns.homeconnect_v2.domain.repository.SharedRepository
import com.sns.homeconnect_v2.domain.usecase.iot_device.sharing.AddSharedUserUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.sharing.GetSharedDevicesUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.sharing.GetSharedUsersUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.sharing.RevokePermissionUseCase
import com.sns.homeconnect_v2.domain.usecase.ticket.CreateTicketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//sealed class DeviceSharingState {
//    data object Idle : DeviceSharingState()// Chưa làm gì
//    data object Loading : DeviceSharingState()            // Đang loading
//    data class Success(val sharedUsers: List<SharedUser>) : DeviceSharingState()
//    data class Error(val error: String) : DeviceSharingState()
//}
//
//sealed class DeviceSharingActionState {
//    data object Idle : DeviceSharingActionState()               // Chưa làm gì
//    data object Loading : DeviceSharingActionState()            // Đang loading
//    data class Success(val success: String) : DeviceSharingActionState()
//    data class Error(val error: String) : DeviceSharingActionState()
//}

sealed class DeviceSharingActionState {
    data object Idle : DeviceSharingActionState()
    data object Loading : DeviceSharingActionState()
    data class Success(val success: String) : DeviceSharingActionState()
    data class Error(val error: String) : DeviceSharingActionState()
}

sealed class SharedDeviceState {
    data object Idle : SharedDeviceState()
    data object Loading : SharedDeviceState()
    data class Success(val devices: List<SharedDeviceResponse>) : SharedDeviceState()
    data class Error(val message: String) : SharedDeviceState()
}

@HiltViewModel
class DeviceSharingViewModel @Inject constructor (
//    private val getSharedUsersUseCase: GetSharedUsersUseCase,
//    private val addSharedUserUseCase: AddSharedUserUseCase,
//    private val revokePermissionUseCase: RevokePermissionUseCase,
    private val createTicketUseCase: CreateTicketUseCase,
    private val getSharedDevicesUseCase: GetSharedDevicesUseCase
) : ViewModel() {
    private val _createTicketState = MutableStateFlow<DeviceSharingActionState>(DeviceSharingActionState.Idle)
    val createTicketState = _createTicketState.asStateFlow()

    fun createSupportTicket(
        title: String,
        description: String,
        ticketTypeId: Int,
        deviceSerial: String,
        assignedTo: String
    ) {
        _createTicketState.value = DeviceSharingActionState.Loading

        val request = CreateTicketRequest(
            title = title,
            description = description,
            ticket_type_id = ticketTypeId,
            device_serial = deviceSerial,
            evidence = Evidence(images = emptyList(), logs = emptyList()),
            assigned_to = assignedTo
        )

        viewModelScope.launch {
            createTicketUseCase(request).fold(
                onSuccess = {
                    _createTicketState.value = DeviceSharingActionState.Success("Tạo ticket thành công!")
                },
                onFailure = { error ->
                    _createTicketState.value = DeviceSharingActionState.Error(
                        error.message ?: "Tạo ticket thất bại!"
                    )
                }
            )
        }
    }

    private val _sharedDevicesState = MutableStateFlow<SharedDeviceState>(SharedDeviceState.Idle)
    val sharedDevicesState = _sharedDevicesState.asStateFlow()

    fun getSharedDevicesForUser() {
        _sharedDevicesState.value = SharedDeviceState.Loading
        viewModelScope.launch {
            getSharedDevicesUseCase().fold(
                onSuccess = { devices ->
                    _sharedDevicesState.value = SharedDeviceState.Success(devices)
                },
                onFailure = { e ->
                    _sharedDevicesState.value =
                        SharedDeviceState.Error(e.message ?: "Lỗi khi tải danh sách thiết bị được chia sẻ!")
                }
            )
        }
    }


//    private val _sharedUsersState = MutableStateFlow<DeviceSharingState>(DeviceSharingState.Idle)
//    val sharedUsersState = _sharedUsersState.asStateFlow()
//
//    fun getSharedUsers(deviceID: Int) {
//        _sharedUsersState.value = DeviceSharingState.Loading
//
//        viewModelScope.launch {
//             getSharedUsersUseCase(deviceID).fold(
//                    onSuccess = { response ->
//                        _sharedUsersState.value = DeviceSharingState.Success(response)
//                    },
//                    onFailure = { error ->
//                        _sharedUsersState.value =
//                            DeviceSharingState.Error(error.message ?: "Lỗi tải danh sách người dùng chia sẻ!")
//                    }
//                )
//            }
//    }
//
//    private val _addSharedUserState = MutableStateFlow<DeviceSharingActionState>(DeviceSharingActionState.Idle)
//    val addSharedUserState = _addSharedUserState.asStateFlow()
//
//    fun addSharedUser(deviceId: Int,sharedWithUser: String) {
//        _addSharedUserState.value = DeviceSharingActionState.Loading
//        viewModelScope.launch {
//            addSharedUserUseCase(deviceId, sharedWithUser).fold(
//                onSuccess = { response ->
//                    _addSharedUserState.value =
//                        DeviceSharingActionState.Success("Thêm người dùng chia sẻ thành công!")
//                },
//                onFailure = { error ->
//                    _addSharedUserState.value =
//                        DeviceSharingActionState.Error(error.message ?: "Lỗi thêm người dùng chia sẻ!")
//                }
//            )
//        }
//    }
//
//    private val _revokePermissionState =
//        MutableStateFlow<DeviceSharingActionState>(DeviceSharingActionState.Idle)
//    val revokePermissionState = _revokePermissionState.asStateFlow()
//
//    fun revokePermission(permissionID: Int) {
//        _revokePermissionState.value = DeviceSharingActionState.Loading
//
//        viewModelScope.launch {
//            revokePermissionUseCase(permissionID).fold(
//                onSuccess = { response ->
//                    _revokePermissionState.value =
//                        DeviceSharingActionState.Success("Xóa quyền chia sẻ thành công!")
//                },
//                onFailure = { error ->
//                    _revokePermissionState.value =
//                        DeviceSharingActionState.Error(error.message ?: "Lỗi xóa quyền chia sẻ!")
//                }
//            )
//        }
//    }
}