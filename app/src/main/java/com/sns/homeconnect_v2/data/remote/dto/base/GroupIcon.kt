package com.sns.homeconnect_v2.data.remote.dto.base

import androidx.compose.ui.graphics.vector.ImageVector

enum class GroupIconCategory {
    GROUP, HOUSE, SPACE
}

sealed class GroupIcon {
    abstract val name: String
    abstract val label: String
    abstract val category: GroupIconCategory

    data class Vector(
        val icon: ImageVector,
        override val name: String,
        override val label: String,
        override val category: GroupIconCategory
    ) : GroupIcon()

    data class Svg(
        val assetPath: String,
        override val name: String,
        override val label: String,
        override val category: GroupIconCategory
    ) : GroupIcon()
}



