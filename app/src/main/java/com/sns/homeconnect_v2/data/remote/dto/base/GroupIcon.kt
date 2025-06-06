package com.sns.homeconnect_v2.data.remote.dto.base

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

sealed class GroupIcon(val label: String) {
    class Vector(val icon: ImageVector, label: String) : GroupIcon(label)
    class Image(val painter: Painter, label: String) : GroupIcon(label)
}

