package com.sns.homeconnect_v2.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class FabChild(
    val icon: ImageVector,
    val onClick: () -> Unit,
    val containerColor: Color = Color(0xFF455A64),
    val contentColor: Color   = Color.White
)