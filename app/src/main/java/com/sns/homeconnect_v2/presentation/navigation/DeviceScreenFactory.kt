package com.sns.homeconnect_v2.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.data.remote.dto.response.ProductData
import com.sns.homeconnect_v2.presentation.screen.iot_device.CameraDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.DefaultDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.DeviceDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.DoorDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.FireAlarmDetailScreen
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

object DeviceScreenFactory {
    fun getScreen(
        deviceId: String,
        deviceName: String,
        deviceTypeName: String?,
        deviceTypeParentName: String?,
        serialNumber: String,
        product: ProductData? = null,
        groupId: Int,
        controls: Map<String, String>,
        snackbarViewModel: @Composable () -> SnackbarViewModel
    ): @Composable (NavHostController) -> Unit {
        val normalized = deviceTypeParentName?.trim()?.lowercase()
        val isViewOnly = controls["permission_type"]?.uppercase() == "VIEW"

        // một ProductData rỗng (cần constructor mặc định trong data class)
        val safeProduct = product ?: ProductData()

        Log.d("CHECK", "parentName='$deviceTypeParentName', normalized='$normalized', isViewOnly=$isViewOnly, controls=$controls")

        return when {
            normalized == "đèn led" -> { navController ->
                DeviceDetailScreen(
                    navController = navController,
                    deviceId = deviceId,
                    deviceName = deviceName,
                    deviceTypeName = deviceTypeName ?: "",
                    serialNumber = serialNumber,
                    product = safeProduct,
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
                    deviceTypeName = deviceTypeName ?: "",
                    serialNumber = serialNumber,
                    product = safeProduct,
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
                    deviceTypeName = deviceTypeName ?: "",
                    serialNumber = serialNumber,
                    controls = controls,
                    snackbarViewModel = snackbarViewModel(),
                    isViewOnly = isViewOnly               // ✅ Truyền xuống
                )
            }

            normalized?.contains("cửa thông minh") == true -> { navController ->
                DoorDetailScreen(
                    navController = navController,
                    deviceId = deviceId,
                    deviceName = deviceName,
                    deviceTypeName = deviceTypeName ?: "",
                    serialNumber = serialNumber,
                    controls = controls,
                    snackbarViewModel = snackbarViewModel(),
                    isViewOnly = isViewOnly
                )
            }


            else -> { navController ->
                DefaultDetailScreen(navController)
            }
        }
    }
}
