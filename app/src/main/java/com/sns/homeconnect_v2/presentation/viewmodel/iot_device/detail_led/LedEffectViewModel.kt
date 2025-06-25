package com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_led

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.*
import com.sns.homeconnect_v2.data.remote.dto.response.*
import com.sns.homeconnect_v2.domain.usecase.iot_device.ApplyLedEffectUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.ApplyLedPresetUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetLedEffectsUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.StopLedEffectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/* ---------- UI STATE ---------- */
sealed interface LedUiState {
    object Idle : LedUiState
    object Loading : LedUiState
    data class Success(val device: DeviceResponse) : LedUiState
    data class EffectsLoaded(val effects: LedEffectsResponse) : LedUiState
    data class Error(val message: String) : LedUiState
}

@HiltViewModel
class LedEffectViewModel @Inject constructor(
    private val applyLedEffect: ApplyLedEffectUseCase,
    private val stopLedEffect:  StopLedEffectUseCase,
    private val applyLedPreset: ApplyLedPresetUseCase,
    private val getLedEffects:  GetLedEffectsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LedUiState>(LedUiState.Idle)
    val uiState = _uiState.asStateFlow()

    /* ------------ PUBLIC API ------------ */

    fun fetchEffects(serial_number: String) = launchWithState {
        val result = getLedEffects(serial_number)
        _uiState.value = result.fold(
            onSuccess = { LedUiState.EffectsLoaded(it) },
            onFailure = { LedUiState.Error(it.localizedMessage ?: "Lỗi lấy danh sách hiệu ứng") }
        )
    }

    fun applyEffect(
        deviceId: String,
        serial: String,
        effect: String,
        speed: Int? = null,
        count: Int? = null,
        duration: Int? = null,
        color1: String? = null,
        color2: String? = null
    ) = launchWithState {
        val req = LedEffectRequest(serial, effect, speed, count, duration, color1, color2)
        val result = applyLedEffect(deviceId, req)
        handleDeviceResult(result)
    }

    fun stopEffect(serial_number: String, serial: String) = launchWithState {
        val result = stopLedEffect(serial_number, StopLedEffectRequest(serial))
        handleDeviceResult(result)
    }

    fun applyPreset(
        deviceId: String,
        serial: String,
        preset: String,
        duration: Int? = null
    ) = launchWithState {
        val req = LedPresetRequest(serial, preset, duration)
        val result = applyLedPreset(deviceId, req)
        handleDeviceResult(result)
    }

    /* ------------ PRIVATE HELPERS ------------ */

    private inline fun launchWithState(crossinline block: suspend () -> Unit) {
        _uiState.value = LedUiState.Loading
        viewModelScope.launch { block() }
    }

    private fun handleDeviceResult(result: Result<DeviceResponse>) {
        _uiState.value = result.fold(
            onSuccess = { LedUiState.Success(it) },
            onFailure = { LedUiState.Error(it.localizedMessage ?: "Thao tác thất bại") }
        )
    }
}
