package com.sns.homeconnect_v2.presentation.viewmodel.iot_device


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.LinkDeviceResponse
import com.sns.homeconnect_v2.domain.usecase.iot_device.LinkDeviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

sealed class LinkDeviceState {
    data object Idle : LinkDeviceState()
    data object Loading : LinkDeviceState()
    data class LinkSuccess(val message: String, val linkDevice: LinkDeviceResponse) : LinkDeviceState()
    data class Error(val error: String) : LinkDeviceState()
}

@HiltViewModel
class AddDeviceViewModel @Inject constructor(
    private val linkDeviceUseCase: LinkDeviceUseCase,
) : ViewModel() {

    private val _deviceState = MutableStateFlow<LinkDeviceState>(LinkDeviceState.Idle)
    val linkDeviceState = _deviceState.asStateFlow()

    fun linkDevice(deviceId: String, spaceId: String, deviceName: String) {
        _deviceState.value = LinkDeviceState.Loading
        viewModelScope.launch {
            linkDeviceUseCase(deviceId, spaceId, deviceName).fold(
                onSuccess = { response ->
                    Log.d("LinkDeviceViewModel", "Link device success: $response")
                    _deviceState.value = LinkDeviceState.LinkSuccess("Link device success", response)
                },
                onFailure = { error ->
                    Log.e("LinkDeviceViewModel", "Error linking device: ${error.message}")
                    _deviceState.value = when (error) {
                        is HttpException -> LinkDeviceState.Error("HTTP error: ${error.code()}")
                        else -> LinkDeviceState.Error("Unknown error: ${error.message}")
                    }
                }
            )
        }
    }
}