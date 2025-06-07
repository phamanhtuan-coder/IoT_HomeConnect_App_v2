package com.sns.homeconnect_v2.data.remote.dto.base

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

sealed class GroupIcon(val label: String, val name: String) {
    data class Vector(val icon: ImageVector, val iconName: String, val iconLabel: String)
        : GroupIcon(iconLabel, iconName)

    data class Image(val painter: Painter, val iconName: String, val iconLabel: String)
        : GroupIcon(iconLabel, iconName)
}


