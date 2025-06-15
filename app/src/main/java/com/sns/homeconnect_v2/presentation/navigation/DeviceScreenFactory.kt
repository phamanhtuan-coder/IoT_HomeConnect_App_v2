package com.sns.homeconnect_v2.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.data.remote.dto.response.ProductData
import com.sns.homeconnect_v2.presentation.screen.iot_device.DefaultDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.DeviceDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.FireAlarmDetailScreen
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

object DeviceScreenFactory {
    fun getScreen(
        parentName: String?,
        product: ProductData,
        controls: Map<String, String>,
        snackbarViewModel: @Composable () -> SnackbarViewModel
    ): @Composable (NavHostController) -> Unit {
        return when (parentName?.lowercase()) {
            "đèn" -> { navController ->
                DeviceDetailScreen(navController, product, controls, snackbarViewModel())
            }
            "cảm biến" -> { navController ->
                FireAlarmDetailScreen(navController, product, controls, snackbarViewModel())
            }
            else -> { navController ->
                DefaultDetailScreen(navController)
            }
        }
    }

}


