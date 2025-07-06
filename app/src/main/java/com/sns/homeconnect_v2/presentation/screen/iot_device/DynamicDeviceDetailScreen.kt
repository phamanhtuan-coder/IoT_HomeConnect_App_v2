package com.sns.homeconnect_v2.presentation.screen.iot_device

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.presentation.navigation.DeviceScreenFactory
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
    spaceId: Int,
    isViewOnly: Boolean,
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
    displayViewModel: DeviceDisplayViewModel = hiltViewModel(),
    capabilitiesViewModel: DeviceCapabilitiesViewModel = hiltViewModel()
) {
    val capabilitiesState by capabilitiesViewModel.uiState.collectAsState()

    Log.d("CHECK", "DynamicDeviceDetailScreen: deviceId=$deviceId, productId=$productId, serialNumber=$serialNumber")

    val displayState by displayViewModel.displayState.collectAsState()

    LaunchedEffect(productId, deviceId, serialNumber) {
        displayViewModel.fetchDisplayInfo(productId)
        capabilitiesViewModel.loadCapabilities(deviceId, serialNumber)
    }

    when {
        displayState is DeviceDisplayInfoState.Loading ||
                capabilitiesState is DeviceCapabilitiesUiState.Loading -> {
            Log.d("CHECK", "Đang Loading")
            CircularProgressIndicator()
        }

        displayState is DeviceDisplayInfoState.Error -> {
            Log.d("CHECK", "Lỗi display: ${(displayState as DeviceDisplayInfoState.Error).error}")
            snackbarViewModel.showSnackbar(
                (displayState as DeviceDisplayInfoState.Error).error,
                SnackbarVariant.ERROR
            )
        }

        capabilitiesState is DeviceCapabilitiesUiState.Error -> {
            Log.d("CHECK", "Lỗi capabilities: ${(capabilitiesState as DeviceCapabilitiesUiState.Error).error}")
            snackbarViewModel.showSnackbar(
                (capabilitiesState as DeviceCapabilitiesUiState.Error).error,
                SnackbarVariant.ERROR
            )
        }

        displayState is DeviceDisplayInfoState.Success &&
                capabilitiesState is DeviceCapabilitiesUiState.Success -> {
            Log.d("CHECK", "CẢ 2 ĐÃ SUCCESS")
            val category = (displayState as DeviceDisplayInfoState.Success).category
            val product = (displayState as DeviceDisplayInfoState.Success).product
            val parentName = category.parent_name
            val baseControls = (capabilitiesState as DeviceCapabilitiesUiState.Success)
                .data.capabilities.merged_capabilities.controls

            val controlsMap = baseControls + mapOf("permission_type" to if (isViewOnly) "VIEW" else "CONTROL")

            Log.d("CHECK", "parentName=$parentName, controls=$controlsMap")

            val screen = DeviceScreenFactory.getScreen(
                deviceId = deviceId,
                deviceName = deviceName,
                parentName = parentName,
                serialNumber = serialNumber,
                spaceId = spaceId,
                product = product,
                controls = controlsMap,
                snackbarViewModel = { snackbarViewModel }
            )
            screen(navController)
        }
        // Để debug trường hợp không rơi vào đâu:
        else -> {
            Log.d("CHECK", "Trạng thái ngoài dự kiến: displayState=$displayState, capabilitiesState=$capabilitiesState")
        }
    }

}
