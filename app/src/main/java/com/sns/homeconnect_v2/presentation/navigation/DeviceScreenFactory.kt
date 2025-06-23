package com.sns.homeconnect_v2.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.data.remote.dto.response.ProductData
import com.sns.homeconnect_v2.presentation.screen.iot_device.CameraDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.DefaultDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.DeviceDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.FireAlarmDetailScreen
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

object DeviceScreenFactory {
    fun getScreen(
        deviceId: String,
        deviceName: String,
        parentName: String?,
        serialNumber: String,
        product: ProductData,
        controls: Map<String, String>,
        snackbarViewModel: @Composable () -> SnackbarViewModel
    ): @Composable (NavHostController) -> Unit {
        val normalized = parentName?.trim()?.lowercase()
        Log.d("CHECK", "parentName='$parentName', normalized='$normalized', controls=$controls")
        return when {
            normalized == "đèn" -> { navController ->
                DeviceDetailScreen(navController, deviceId, deviceName, serialNumber, product, controls, snackbarViewModel())
            }
            normalized?.contains("cảm biến") == true -> { navController ->
                FireAlarmDetailScreen(navController, deviceId, deviceName, serialNumber, product, controls, snackbarViewModel())
            }
            normalized?.contains("camera") == true -> { navController ->
                CameraDetailScreen(navController, deviceId, deviceName, serialNumber, controls, snackbarViewModel())
            }
            else -> { navController ->
                DefaultDetailScreen(navController)
            }
        }
    }
}



