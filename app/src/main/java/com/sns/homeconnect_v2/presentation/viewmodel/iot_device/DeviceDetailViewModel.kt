package com.sns.homeconnect_v2.presentation.viewmodel.iot_device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.CategoryData
import com.sns.homeconnect_v2.data.remote.dto.response.ProductData
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDeviceDisplayInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DeviceDisplayInfoState {
    data object Idle : DeviceDisplayInfoState()
    data object Loading : DeviceDisplayInfoState()
    data class Success(
        val product: ProductData,
        val category: CategoryData
    ) : DeviceDisplayInfoState()
    data class Error(val error: String) : DeviceDisplayInfoState()
}

@HiltViewModel
class DeviceDisplayViewModel @Inject constructor(
    private val getDeviceDisplayInfoUseCase: GetDeviceDisplayInfoUseCase
) : ViewModel() {

    private val _deviceDisplayInfoState = MutableStateFlow<DeviceDisplayInfoState>(DeviceDisplayInfoState.Idle)
    val deviceDisplayInfoState = _deviceDisplayInfoState.asStateFlow()

    fun getDeviceDisplayInfo(templateId: Int) {
        _deviceDisplayInfoState.value = DeviceDisplayInfoState.Loading

        viewModelScope.launch {
            getDeviceDisplayInfoUseCase(templateId).fold(
                onSuccess = { result ->
                    _deviceDisplayInfoState.value = DeviceDisplayInfoState.Success(
                        product = result.product,
                        category = result.category
                    )
                },
                onFailure = { error ->
                    _deviceDisplayInfoState.value = DeviceDisplayInfoState.Error(
                        error.message ?: "Không thể lấy thông tin sản phẩm"
                    )
                }
            )
        }
    }
}

