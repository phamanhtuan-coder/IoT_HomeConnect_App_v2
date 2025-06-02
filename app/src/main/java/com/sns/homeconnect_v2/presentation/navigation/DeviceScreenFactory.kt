package com.sns.homeconnect_v2.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.presentation.screen.iot_device.DefaultDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.DeviceDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.FireAlarmDetailScreen

object DeviceScreenFactory {
    private val screenMap: Map<Int, @Composable (NavHostController, Int?) -> Unit> = mapOf(
        1 to { navController, _ ->
            DeviceDetailScreen(navController)
        },
        2 to { navController, _ ->
            FireAlarmDetailScreen(navController)
        },
        3 to { navController, _ ->
            DefaultDetailScreen(navController)
        },
    )

    fun getScreen(typeID: Int): @Composable (NavHostController, Int?) -> Unit {
        return screenMap[typeID] ?: { navController, _ -> DefaultDetailScreen(navController) }
    }

}