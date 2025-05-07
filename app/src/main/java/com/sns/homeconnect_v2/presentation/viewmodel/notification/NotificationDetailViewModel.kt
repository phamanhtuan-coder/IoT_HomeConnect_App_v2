package com.example.ungdungquanlyvadieukhieniot_homeconnect.ui.screen.notification_detail

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungquanlyvadieukhieniot_homeconnect.data.remote.dto.Alert
import com.example.ungdungquanlyvadieukhieniot_homeconnect.data.remote.dto.AlertDetail
import com.example.ungdungquanlyvadieukhieniot_homeconnect.data.repository.AlertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NotificationState {
    object Idle : NotificationState()               // Chưa làm gì
    object Loading : NotificationState()            // Đang loading
    data class Success(val alert: AlertDetail) : NotificationState()
    data class Error(val error: String) : NotificationState()
}

sealed class NotificationReadState {
    object Idle : NotificationReadState()               // Chưa làm gì
    object Loading : NotificationReadState()            // Đang loading
    data class Success(val alert: Alert) : NotificationReadState()
    data class Error(val error: String) : NotificationReadState()
}


class NotificationViewModel(application: Application, context: Context) :
    AndroidViewModel(application) {
    private val repository = AlertRepository(context)
    private val _alertState = MutableStateFlow<NotificationState>(NotificationState.Idle)
    val alertState = _alertState.asStateFlow()

    /**
     * Lấy danh sách thông báo
     */
    fun getAllByUser(alertId: Int) {
        viewModelScope.launch {
            try {
                _alertState.value = NotificationState.Loading
                val response = repository.getAlertById(alertId)
                Log.d("NotificationModel", "Alerts: $response")
                _alertState.value = NotificationState.Success(response)
            } catch (e: Exception) {
                Log.e("NotificationModel", "Error fetching alerts: ${e.message}")
                _alertState.value =
                    NotificationState.Error(e.message ?: "Thong bao load thất bại!")
            }
        }
    }
    private val _alertReadState =
        MutableStateFlow<NotificationReadState>(NotificationReadState.Idle)
    val alertReadState = _alertReadState.asStateFlow()

    fun readNotification(alertId: Int) {
        viewModelScope.launch {
            try {
                _alertReadState.value = NotificationReadState.Loading
                val response = repository.readNotification(alertId)
                Log.d("NotificationModel", "Alerts: $response")
                _alertReadState.value = NotificationReadState.Success(response)
            } catch (e: Exception) {
                Log.e("NotificationModel", "Error fetching alerts: ${e.message}")
                _alertReadState.value =
                    NotificationReadState.Error(e.message ?: "Cập nhật trạng thái thất bại!")
            }
        }
    }
}