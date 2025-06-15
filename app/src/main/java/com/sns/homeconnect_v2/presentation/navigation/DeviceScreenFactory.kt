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
        snackbarViewModel: @Composable () -> SnackbarViewModel
    ): @Composable (NavHostController) -> Unit {
        val nameLog = parentName ?: "null"
        Log.d("CHECK", "Vào getScreen: parentName='$nameLog'")
        return when (parentName?.lowercase()) {
            "đèn" -> { navController ->
                Log.d("CHECK", "Vào màn DeviceDetailScreen, parentName='$nameLog'")
                DeviceDetailScreen(navController, product, snackbarViewModel())
            }
            "cảm biến" -> { navController ->
                Log.d("CHECK", "Vào màn FireAlarmDetailScreen, parentName='$nameLog'")
                FireAlarmDetailScreen(navController, product, snackbarViewModel())
            }
            else -> { navController ->
                Log.d("CHECK", "Vào màn DefaultDetailScreen, parentName='$nameLog'")
                DefaultDetailScreen(navController)
            }
        }
    }
}


