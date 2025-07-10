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
        groupId: Int,
        controls: Map<String, String>,
        snackbarViewModel: @Composable () -> SnackbarViewModel
    ): @Composable (NavHostController) -> Unit {
        val normalized = parentName?.trim()?.lowercase()
        val isViewOnly = controls["permission_type"]?.uppercase() == "VIEW" // ✅ Thêm dòng này

        Log.d("CHECK", "parentName='$parentName', normalized='$normalized', isViewOnly=$isViewOnly, controls=$controls")

        return when {
            normalized == "Đèn Led" -> { navController ->
                DeviceDetailScreen(
                    navController = navController,
                    deviceId = deviceId,
                    deviceName = deviceName,
                    serialNumber = serialNumber,
                    product = product,
                    controls = controls,
                    groupId = groupId,
                    snackbarViewModel = snackbarViewModel(),
                    isViewOnly = isViewOnly
                )
            }

            normalized?.contains("cảm biến") == true -> { navController ->
                FireAlarmDetailScreen(
                    navController = navController,
                    deviceId = deviceId,
                    deviceName = deviceName,
                    serialNumber = serialNumber,
                    product = product,
                    controls = controls,
                    groupId   = groupId,
                    snackbarViewModel = snackbarViewModel(),
                    isViewOnly = isViewOnly
                )
            }

            normalized?.contains("camera") == true -> { navController ->
                CameraDetailScreen(
                    navController = navController,
                    deviceId = deviceId,
                    deviceName = deviceName,
                    serialNumber = serialNumber,
                    controls = controls,
                    snackbarViewModel = snackbarViewModel(),
                    isViewOnly = isViewOnly               // ✅ Truyền xuống
                )
            }

            else -> { navController ->
                DefaultDetailScreen(navController)
            }
        }
    }
}
