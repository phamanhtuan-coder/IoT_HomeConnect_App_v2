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
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceCapabilitiesViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceDisplayInfoState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceDisplayViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

@Composable
fun DynamicDeviceDetailScreen(
    deviceId: String,
    serialNumber: String,
    productId: Int,
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
    displayViewModel: DeviceDisplayViewModel = hiltViewModel(),
    capabilitiesViewModel: DeviceCapabilitiesViewModel = hiltViewModel()
) {
    val displayState by displayViewModel.deviceDisplayInfoState.collectAsState()
    val controls by capabilitiesViewModel.controls.collectAsState()

    LaunchedEffect(productId, deviceId, serialNumber) {
        displayViewModel.getDeviceDisplayInfo(productId)
        capabilitiesViewModel.loadCapabilities(deviceId, serialNumber)
    }

    when (displayState) {
        is DeviceDisplayInfoState.Loading -> CircularProgressIndicator()

        is DeviceDisplayInfoState.Error -> snackbarViewModel.showSnackbar(
            msg = (displayState as DeviceDisplayInfoState.Error).error,
            SnackbarVariant.ERROR
        )

        is DeviceDisplayInfoState.Success -> {
            val category = (displayState as DeviceDisplayInfoState.Success).category
            val product = (displayState as DeviceDisplayInfoState.Success).product
            val parentName = category.parent_name

            Log.d("CHECK", "parentName: $parentName, controls=$controls")

            val screen = DeviceScreenFactory.getScreen(
                parentName = parentName,
                product = product,
                controls = if (controls.isNotEmpty()) controls else mapOf(),
                snackbarViewModel = { snackbarViewModel }
            )

            screen(navController)
        }

        else -> Unit
    }
}
