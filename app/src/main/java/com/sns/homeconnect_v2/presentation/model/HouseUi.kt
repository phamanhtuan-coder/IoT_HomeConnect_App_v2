package com.sns.homeconnect_v2.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class HouseUi(
    val id: Int,
    val name: String,
    val spaces: Int,
    val role: String,
    val isRevealed: Boolean,
    val iconName: String,
    val iconColor: Color
)