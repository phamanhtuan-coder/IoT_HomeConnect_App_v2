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
    Log.d("CHECK", "deviceTypeName=$deviceTypeName, deviceTypeParentName=$deviceTypeParentName")

    val displayState by displayViewModel.displayState.collectAsState()
    val capabilitiesState by capabilitiesViewModel.uiState.collectAsState()

    LaunchedEffect(productId, deviceId, serialNumber) {
        if (displayState !is DeviceDisplayInfoState.Success &&
            displayState !is DeviceDisplayInfoState.Loading) {
            displayViewModel.fetchDisplayInfo(productId)
        }
        if (capabilitiesState !is DeviceCapabilitiesUiState.Success &&
            capabilitiesState !is DeviceCapabilitiesUiState.Loading) {
            capabilitiesViewModel.loadCapabilities(deviceId, serialNumber)
        }
    }

    when {
        displayState is DeviceDisplayInfoState.Success &&
                capabilitiesState is DeviceCapabilitiesUiState.Success -> {
            val category = (displayState as DeviceDisplayInfoState.Success).category
            val product = (displayState as DeviceDisplayInfoState.Success).product
            val parentName = category.parent_name
            val baseControls = (capabilitiesState as DeviceCapabilitiesUiState.Success)
                .data.capabilities.merged_capabilities.controls
            val controlsMap = baseControls + mapOf("permission_type" to if (isViewOnly) "VIEW" else "CONTROL")

            // 👉 Xác định lại 2 biến trước khi build màn
            val finalDeviceTypeName   = deviceTypeName?.takeIf { it.isNotBlank() } ?: category.name
            val finalParentTypeName   = deviceTypeParentName?.takeIf { it.isNotBlank() } ?: parentName


            val screen = DeviceScreenFactory.getScreen(
                deviceId = deviceId,
                deviceName = deviceName,
                deviceTypeName        = finalDeviceTypeName,   // đã có fallback
                deviceTypeParentName  = finalParentTypeName,   // đã có fallback
                serialNumber = serialNumber,
                groupId = groupId,
                product = product,
                controls = controlsMap,
                snackbarViewModel = { snackbarViewModel }
            )
            screen(navController)
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

        else -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(5000)
                val stillLoading = displayViewModel.displayState.value !is DeviceDisplayInfoState.Success ||
                        capabilitiesViewModel.uiState.value !is DeviceCapabilitiesUiState.Success
                if (stillLoading) {
                    Log.d("CHECK", "Timeout loading, điều hướng về ListDevices")
                    navController.popBackStack()
                }
            }
        }
    }
}
