package com.sns.homeconnect_v2.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class DeviceStatCardItem(
    val icon: ImageVector,
    val iconColor: Color,
    val title: String,
    val value: String
)
