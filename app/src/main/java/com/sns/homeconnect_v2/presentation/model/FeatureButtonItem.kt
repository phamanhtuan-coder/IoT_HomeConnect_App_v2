package com.sns.homeconnect_v2.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class FeatureButtonItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit
)

