package com.sns.homeconnect_v2.core.util.validation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getIconByName(name: String?): ImageVector {
    return when (name?.lowercase()) {
        "home" -> Icons.Default.Home
        "work" -> Icons.Default.Work
        "school" -> Icons.Default.School
        "group" -> Icons.Default.Group
        "settings" -> Icons.Default.Settings
        "person" -> Icons.Default.Person
        "info" -> Icons.Default.Info
        "lock" -> Icons.Default.Lock
        else -> Icons.Default.Group
    }
}
