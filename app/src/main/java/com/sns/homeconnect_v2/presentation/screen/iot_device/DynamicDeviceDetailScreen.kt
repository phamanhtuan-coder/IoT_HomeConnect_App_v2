package com.sns.homeconnect_v2.presentation.screen.iot_device

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.presentation.navigation.DeviceScreenFactory
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceCapabilitiesUiState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceCapabilitiesViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceDisplayInfoState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceDisplayViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

@Composable
fun DynamicDeviceDetailScreen(
    deviceId: String,
    deviceName: String,
    serialNumber: String,
    productId: String,
    groupId: Int,
    isViewOnly: Boolean,
    navController: NavHostController,
    deviceTypeName: String?,
    deviceTypeParentName: String?,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
    displayViewModel: DeviceDisplayViewModel = hiltViewModel(),
    capabilitiesViewModel: DeviceCapabilitiesViewModel = hiltViewModel()
) {
    val alreadyHasTypeInfo = !deviceTypeName.isNullOrBlank() && !deviceTypeParentName.isNullOrBlank()

    if (alreadyHasTypeInfo) {
        val displayState by displayViewModel.displayState.collectAsState()

        if (displayState is DeviceDisplayInfoState.Success) {
            val product = (displayState as DeviceDisplayInfoState.Success).product
            val controlsMap = mapOf("permission_type" to if (isViewOnly) "VIEW" else "CONTROL")

            val screen = DeviceScreenFactory.getScreen(
                deviceId = deviceId,
                deviceName = deviceName,
                deviceTypeName = deviceTypeName,
                deviceTypeParentName = deviceTypeParentName,
                serialNumber = serialNumber,
                groupId = groupId,
                product = product,
                controls = controlsMap,
                snackbarViewModel = { snackbarViewModel }
            )
            screen(navController)
            return
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            // ⏲ Timeout sau 3 giây nếu vẫn chưa Success thì quay lại màn list
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(3000)
                val currentState = displayViewModel.displayState.value
                if (currentState !is DeviceDisplayInfoState.Success) {
                    Log.d("CHECK", "Timeout loading, điều hướng về ListDevices")
                    navController.navigate(Screens.ListDevices.route) {
                        popUpTo(Screens.ListDevices.route) { inclusive = true }
                    }
                }
            }

            return
        }
    }


    // 🔹 Nếu chưa có thông tin → Gọi ViewModel để lấy như bình thường
    val capabilitiesState by capabilitiesViewModel.uiState.collectAsState()
    val displayState by displayViewModel.displayState.collectAsState()

    Log.d("CHECK", "Gọi ViewModel: deviceId=$deviceId, productId=$productId")

    LaunchedEffect(productId, deviceId, serialNumber) {
        displayViewModel.fetchDisplayInfo(productId)
        capabilitiesViewModel.loadCapabilities(deviceId, serialNumber)
    }

    when {
        displayState is DeviceDisplayInfoState.Loading ||
                capabilitiesState is DeviceCapabilitiesUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            // ⏲ Timeout sau 3 giây nếu vẫn chưa Success thì quay lại màn list
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(3000)
                val currentState = displayViewModel.displayState.value
                if (currentState !is DeviceDisplayInfoState.Success) {
                    Log.d("CHECK", "Timeout loading, điều hướng về ListDevices")
                    navController.navigate(Screens.ListDevices.route) {
                        popUpTo(Screens.ListDevices.route) { inclusive = true }
                    }
                }
            }
        }

        displayState is DeviceDisplayInfoState.Error -> {
            snackbarViewModel.showSnackbar(
                (displayState as DeviceDisplayInfoState.Error).error,
                SnackbarVariant.ERROR
            )
        }

        capabilitiesState is DeviceCapabilitiesUiState.Error -> {
            snackbarViewModel.showSnackbar(
                (capabilitiesState as DeviceCapabilitiesUiState.Error).error,
                SnackbarVariant.ERROR
            )
        }

        displayState is DeviceDisplayInfoState.Success &&
                capabilitiesState is DeviceCapabilitiesUiState.Success -> {
            val category = (displayState as DeviceDisplayInfoState.Success).category
            val product = (displayState as DeviceDisplayInfoState.Success).product
            val parentName = category.parent_name
            val baseControls = (capabilitiesState as DeviceCapabilitiesUiState.Success)
                .data.capabilities.merged_capabilities.controls

            val controlsMap = baseControls + mapOf("permission_type" to if (isViewOnly) "VIEW" else "CONTROL")

            val screen = DeviceScreenFactory.getScreen(
                deviceId = deviceId,
                deviceName = deviceName,
                deviceTypeName = category.name,
                deviceTypeParentName = parentName,
                serialNumber = serialNumber,
                groupId = groupId,
                product = product,
                controls = controlsMap,
                snackbarViewModel = { snackbarViewModel }
            )
            screen(navController)
        }

        else -> {
            Log.d("CHECK", "Trạng thái ngoài dự kiến")
        }
    }
}
