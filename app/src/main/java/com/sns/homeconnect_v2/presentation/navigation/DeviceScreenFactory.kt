package com.sns.homeconnect_v2.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.presentation.screen.iot_device.DefaultScreen

object DeviceScreenFactory {
    private val screenMap: Map<Int, @Composable (NavHostController, Int?) -> Unit> = mapOf(
// (Removed commented-out screen mappings to improve readability)
    )

    fun getScreen(typeID: Int): @Composable (NavHostController, Int?) -> Unit {
        return screenMap[typeID] ?: { navController, _ -> DefaultScreen(navController) }
    }

}