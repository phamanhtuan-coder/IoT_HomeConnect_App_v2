package com.sns.homeconnect_v2.presentation.viewmodel.space

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponseSpace
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDevicesBySpaceIdUseCase
import com.sns.homeconnect_v2.domain.usecase.space.GetListSpaceUseCase
import com.sns.homeconnect_v2.domain.usecase.space.GetSpaceDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SpaceScreenDetailViewModel @Inject constructor(
    private val getDevicesBySpaceIdUseCase: GetDevicesBySpaceIdUseCase
) :ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _spacesDevice = MutableStateFlow<List<DeviceResponseSpace>>(emptyList())
    val spacesDevice = _spacesDevice.asStateFlow()

    fun getSpaces(spaceId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getDevicesBySpaceIdUseCase.invoke(spaceId)
            result.onSuccess { spacesDevice ->
                _spacesDevice.value = spacesDevice // Đảm bảo spaces là List<Space>
                Log.d("ViewModel space", spacesDevice.toString())
            }.onFailure { error ->
                error.printStackTrace()
                Log.e("ViewModel error", error.toString())
            }
            _isLoading.value = false
        }
    }

    fun updateRevealState(index: Int) {
        _spacesDevice.value = _spacesDevice.value.mapIndexed { i, item ->
            item.copy(isRevealed = i == index)
        }
    }

    fun collapseItem(index: Int) {
        _spacesDevice.value = _spacesDevice.value.mapIndexed { i, item ->
            if (i == index) item.copy(isRevealed = false) else item
        }
    }
}