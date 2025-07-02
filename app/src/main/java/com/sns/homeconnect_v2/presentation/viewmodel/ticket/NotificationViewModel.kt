//package com.sns.homeconnect_v2.presentation.viewmodel.notification
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.sns.homeconnect_v2.data.remote.dto.response.AlertResponse
//import com.sns.homeconnect_v2.data.remote.dto.response.Notification
//import com.sns.homeconnect_v2.domain.repository.NotificationRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class ListNotificationViewModel @Inject constructor(
//    private val notificationRepository: NotificationRepository
//) : ViewModel() {
//
//    private val _alertListState = MutableStateFlow<NotificationState>(NotificationState.Loading)
//    val alertListState: StateFlow<NotificationState> get() = _alertListState
//
//    fun getAllByUser() {
//        viewModelScope.launch {
//            _alertListState.value = NotificationState.Loading
//            try {
//                val notifications = notificationRepository.getUserNotifications()
//                val alertResponses = notifications.map { notification ->
//                    AlertResponse(
//                        id = notification.id.toString(),
//                        data = AlertData(
//                            id = notification.id.toString(),
//                            deviceId = notification.accountId, // Giả sử accountId tương ứng với deviceId
//                            message = notification.text,
//                            status = notification.isRead
//                        )
//                    )
//                }
//                _alertListState.value = NotificationState.Success(alertResponses)
//            } catch (e: Exception) {
//                _alertListState.value = NotificationState.Error(e.message ?: "Đã xảy ra lỗi khi lấy danh sách thông báo")
//            }
//        }
//    }
//}
//
//sealed class NotificationState {
//    object Loading : NotificationState()
//    data class Success(val alertList: List<AlertResponse>) : NotificationState()
//    data class Error(val error: String) : NotificationState()
//}
//
//data class AlertData(
//    val id: String,
//    val deviceId: String,
//    val message: String,
//    val status: Boolean
//)