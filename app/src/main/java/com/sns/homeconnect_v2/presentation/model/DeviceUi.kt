package com.sns.homeconnect_v2.presentation.model
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class DeviceUi(
    val id: Int,
    val name: String,
    val room: String,
    val isRevealed: Boolean,
    val icon: ImageVector,
    val iconColor: Color
)
