package com.sns.homeconnect_v2.presentation.viewmodel.iot_device.access_point_connection

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sns.homeconnect_v2.core.permission.PermissionManager
import com.sns.homeconnect_v2.domain.usecase.iot_device.connect.ScanWifiNetworksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccessPointViewModel @Inject constructor(
    private val scanWifiNetworksUseCase: ScanWifiNetworksUseCase,
    val permissionManager: PermissionManager
): ViewModel() {
    var wifiList by mutableStateOf(emptyList<android.net.wifi.ScanResult>())
        private set
    var isLoading by mutableStateOf(false)
        private set

    suspend fun scanWifiNetworks(context: Context) {
        isLoading = true
        val result = scanWifiNetworksUseCase()
        isLoading = false
        when (result) {
            is com.sns.homeconnect_v2.core.util.Result.Success -> wifiList = result.data
            is com.sns.homeconnect_v2.core.util.Result.Error -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun checkPermissions(context: Context): Boolean {
        return permissionManager.getLocationWifiPermissions().all { permissionManager.isPermissionGranted(it) }
    }
}