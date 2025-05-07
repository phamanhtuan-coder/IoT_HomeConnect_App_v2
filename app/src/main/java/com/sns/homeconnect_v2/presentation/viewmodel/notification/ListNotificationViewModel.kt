package com.sns.homeconnect_v2.presentation.viewmodel.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.AlertResponse
import com.sns.homeconnect_v2.domain.usecase.notification.GetAllByUserUseCase
import com.sns.homeconnect_v2.domain.usecase.notification.SearchNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class NotificationState {
    object Idle : NotificationState()               // Chưa làm gì
    object Loading : NotificationState()            // Đang loading
    data class Success(val alertList: List<AlertResponse>) : NotificationState()
    data class Error(val error: String) : NotificationState()
}

@HiltViewModel
class ListNotificationViewModel @Inject constructor(
    private val getAllByUserUseCase: GetAllByUserUseCase,
    private val searchNotificationUseCase: SearchNotificationUseCase,
) : ViewModel() {
    private val _alertListState = MutableStateFlow<NotificationState>(NotificationState.Idle)
    val alertListState = _alertListState.asStateFlow()

    /**
     * Lấy danh sách thông báo
     */
    fun getAllByUser() {
        _alertListState.value = NotificationState.Loading

        viewModelScope.launch {
            getAllByUserUseCase().fold(
                onSuccess = { response ->
                    Log.d("ListNotificationViewModel", "Alerts: $response")
                    _alertListState.value = NotificationState.Success(response)
                },
                onFailure = { error ->
                    Log.e("ListNotificationViewModel", "Error fetching alerts: ${error.message}")
                    _alertListState.value =
                        NotificationState.Error(error.message ?: "Danh sach load thất bại!")
                }
            )
        }

    }

    fun searchNotification(search: String) {
        _alertListState.value = NotificationState.Loading
        viewModelScope.launch {
            searchNotificationUseCase(search).fold(
                onSuccess = { response ->
                    Log.d("ListNotificationViewModel", "Alerts: $response")
                    _alertListState.value = NotificationState.Success(response)
                },
                onFailure = { error ->
                    Log.e("ListNotificationViewModel", "Error fetching alerts: ${error.message}")
                    _alertListState.value =
                        NotificationState.Error(error.message ?: "Danh sach load thất bại!")
                }
            )
        }
    }
}