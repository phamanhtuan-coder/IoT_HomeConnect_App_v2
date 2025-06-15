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
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceDisplayInfoState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceDisplayViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

@Composable
fun DynamicDeviceDetailScreen(
    productId: Int,
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
    viewModel: DeviceDisplayViewModel = hiltViewModel()
) {
    val state by viewModel.deviceDisplayInfoState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.getDeviceDisplayInfo(productId)
    }

    when (state) {
        is DeviceDisplayInfoState.Loading -> {
            CircularProgressIndicator()
        }

        is DeviceDisplayInfoState.Error -> {
            snackbarViewModel.showSnackbar(
                msg = (state as DeviceDisplayInfoState.Error).error,
                SnackbarVariant.ERROR
            )
        }

        is DeviceDisplayInfoState.Success -> {
            val category = (state as DeviceDisplayInfoState.Success).category
            val product = (state as DeviceDisplayInfoState.Success).product
            val parentName =  category.parent_name
            val productData = product
            Log.d("DynamicDeviceDetailScreen", "Category: $category, Product: $productData")
            Log.d("CHECK", "parentName sau khi lấy category: $parentName")

            val screen = DeviceScreenFactory.getScreen(
                parentName = category.parent_name,
                product = product,
                snackbarViewModel = { snackbarViewModel }
            )
            screen(navController)
        }

        else -> Unit
    }
}
