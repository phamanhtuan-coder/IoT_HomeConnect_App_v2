package com.sns.homeconnect_v2.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class SpaceUi(
    val id: Int,
    val name: String,
    val device: Int,
    val isRevealed: Boolean,
    val icon: ImageVector,
    val iconColor: Color
)