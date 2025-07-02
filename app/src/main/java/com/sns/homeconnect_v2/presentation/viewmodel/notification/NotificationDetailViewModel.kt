package com.sns.homeconnect_v2.presentation.viewmodel.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.Alert
import com.sns.homeconnect_v2.data.remote.dto.response.AlertDetail
import com.sns.homeconnect_v2.data.remote.dto.response.Notification
import com.sns.homeconnect_v2.data.remote.dto.response.check
import com.sns.homeconnect_v2.domain.usecase.notification.GetAlertByIdUseCase
import com.sns.homeconnect_v2.domain.usecase.notification.ReadNotificationUseCase
import com.sns.homeconnect_v2.domain.usecase.notification.UpdateNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NotificationDetailState {
    object Idle : NotificationDetailState()               // Chưa làm gì
    object Loading : NotificationDetailState()            // Đang loading
    data class Success(val alert: Notification) : NotificationDetailState()
    data class Error(val error: String) : NotificationDetailState()
}

sealed class NotificationReadState {
    object Idle : NotificationReadState()               // Chưa làm gì
    object Loading : NotificationReadState()            // Đang loading
    data class Success(val alert: Alert) : NotificationReadState()
    data class Error(val error: String) : NotificationReadState()
}

@HiltViewModel
class NotificationDetailViewModel @Inject constructor(
    private val getAlertByIdUseCase: GetAlertByIdUseCase,
    private val readNotificationUseCase: ReadNotificationUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase
) : ViewModel() {

    private val _alertState = MutableStateFlow<NotificationDetailState>(NotificationDetailState.Idle)
    val alertState = _alertState.asStateFlow()

    private val _alertReadState = MutableStateFlow<NotificationReadState>(NotificationReadState.Idle)
    val alertReadState = _alertReadState.asStateFlow()

    fun getAlertById(id: Int) {
        _alertState.value = NotificationDetailState.Loading

        viewModelScope.launch {
            try {
                val response = getAlertByIdUseCase(id)
                _alertState.value = NotificationDetailState.Success(response) // Kiểu tùy chỉnh theo cách bạn sửa
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Error: ${e.message}")
                _alertState.value = NotificationDetailState.Error(e.message ?: "Lỗi không xác định")
            }
        }
    }

    fun readNotification(alertId: Int) {
        _alertReadState.value = NotificationReadState.Loading

        viewModelScope.launch {
            readNotificationUseCase(alertId).fold(
                onSuccess = { response ->
                    Log.d("NotificationViewModel", "Read: $response")
                    _alertReadState.value = NotificationReadState.Success(response)
                },
                onFailure = { error ->
                    Log.e("NotificationViewModel", "Read Error: ${error.message}")
                    _alertReadState.value =
                        NotificationReadState.Error(error.message ?: "Đọc thông báo thất bại!")
                }
            )
        }
    }

    fun UpdateNotification(alertId: Int, isRead: check) {
        viewModelScope.launch {
            try {
                updateNotificationUseCase(alertId, isRead)
                Log.d("NotificationViewModel", "Update notification status successfully")
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Update Error: ${e.message}")
            }
        }
    }
}
