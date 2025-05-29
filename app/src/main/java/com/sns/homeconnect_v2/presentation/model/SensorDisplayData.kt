package com.sns.homeconnect_v2.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class SensorDisplayData(
    val icon: ImageVector,
    val iconTint: Color,
    val label: String,
    val currentValue: String,
    val unit: String,
    val highestValue: String,
    val highestTime: String,
    val highestColor: Color,
    val highestLabel: String,
    val highestIcon: ImageVector,
    val lowestValue: String,
    val lowestTime: String,
    val lowestColor: Color,
    val lowestLabel: String,
    val lowestIcon: ImageVector
)
