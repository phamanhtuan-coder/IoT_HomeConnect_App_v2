package com.sns.homeconnect_v2.presentation.viewmodel.iot_device.access_point_connection

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sns.homeconnect_v2.domain.usecase.iot_device.connect.SendWifiCredentialsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WifiConnectionViewModel @Inject constructor(
    private val sendWifiCredentialsUseCase: SendWifiCredentialsUseCase
): ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    suspend fun sendCredentials(context: Context, espIp: String, port: Int, ssid: String, password: String): Boolean {
        isLoading = true
        val result = sendWifiCredentialsUseCase(espIp, port, ssid, password)
        isLoading = false
        return when (result) {
            is com.sns.homeconnect_v2.core.util.Result.Success -> {
                Toast.makeText(context, "Wi-Fi credentials sent successfully!", Toast.LENGTH_SHORT).show()
                true
            }
            is com.sns.homeconnect_v2.core.util.Result.Error -> {
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                false
            }
        }
    }
}