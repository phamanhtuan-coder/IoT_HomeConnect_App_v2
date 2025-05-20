package com.sns.homeconnect_v2.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.presentation.screen.iot_device.DefaultScreen

object DeviceScreenFactory {
    private val screenMap: Map<Int, @Composable (NavHostController, Int?) -> Unit> = mapOf(
//        1 to { navController, id -> FireAlarmDetailScreen(navController, id) },
//        2 to { navController, id -> DeviceDetailScreen(navController,  id) },
//        3 to { navController, id -> DeviceDetailScreen(navController,  id) }
    )

    fun getScreen(typeID: Int): @Composable (NavHostController, Int?) -> Unit {
        return screenMap[typeID] ?: { navController, _ -> DefaultScreen(navController) }
    }

}