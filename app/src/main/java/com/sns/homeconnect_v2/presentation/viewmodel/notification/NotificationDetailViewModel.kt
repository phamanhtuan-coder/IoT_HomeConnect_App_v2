package com.sns.homeconnect_v2.presentation.viewmodel.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.Alert
import com.sns.homeconnect_v2.data.remote.dto.response.AlertDetail
import com.sns.homeconnect_v2.domain.usecase.notification.GetAlertByIdUseCase
import com.sns.homeconnect_v2.domain.usecase.notification.ReadNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NotificationDetailState {
    object Idle : NotificationDetailState()               // Chưa làm gì
    object Loading : NotificationDetailState()            // Đang loading
    data class Success(val alert: AlertDetail) : NotificationDetailState()
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
) : ViewModel() {

    private val _alertState = MutableStateFlow<NotificationDetailState>(NotificationDetailState.Idle)
    val alertState = _alertState.asStateFlow()

    /**
     * Lấy danh sách thông báo
     */
    fun getAlertById(alertId: Int) {
        _alertState.value = NotificationDetailState.Loading

        viewModelScope.launch {
            getAlertByIdUseCase(alertId).fold(
                onSuccess = { response ->
                    Log.d("NotificationModel", "Alerts: $response")
                    _alertState.value = NotificationDetailState.Success(response)
                },
                onFailure = { error ->
                    Log.e("NotificationModel", "Error fetching alerts: ${error.message}")
                    _alertState.value =
                        NotificationDetailState.Error(error.message ?: "Thong bao load thất bại!")
                }
            )
        }
    }
    private val _alertReadState =
        MutableStateFlow<NotificationReadState>(NotificationReadState.Idle)
    val alertReadState = _alertReadState.asStateFlow()

    fun readNotification(alertId: Int) {
        _alertReadState.value = NotificationReadState.Loading

        viewModelScope.launch {
           readNotificationUseCase(alertId).fold(
                onSuccess = { response ->
                    Log.d("NotificationModel", "Alerts: $response")
                    _alertReadState.value = NotificationReadState.Success(response)
                },
                onFailure = { error ->
                    Log.e("NotificationModel", "Error fetching alerts: ${error.message}")
                    _alertReadState.value =
                        NotificationReadState.Error(error.message ?: "Thong bao load thất bại!")
                }
            )
        }
    }
}