package com.sns.homeconnect_v2.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

fun getWeatherIcon(weatherCondition: String): ImageVector {
    return when (weatherCondition.lowercase()) {
        "sunny", "clear" -> Icons.Filled.WbSunny
        "partly cloudy", "cloudy" -> Icons.Filled.Visibility
        "rain", "showers", "drizzle" -> Icons.Filled.WaterDrop
        "snow", "sleet" -> Icons.Filled.NightsStay
        "thunderstorm" -> Icons.Filled.Air
        else -> Icons.Filled.Visibility
    }
}