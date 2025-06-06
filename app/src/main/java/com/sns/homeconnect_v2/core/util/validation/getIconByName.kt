package com.sns.homeconnect_v2.core.util.validation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getIconByName(name: String?): ImageVector {
    return when (name?.lowercase()) {
        "home" -> Icons.Default.Home
        "work" -> Icons.Default.Work
        "school" -> Icons.Default.School
        "account_balance" -> Icons.Default.AccountBalance
        "apartment" -> Icons.Default.Apartment
        "hotel" -> Icons.Default.Hotel
        "villa" -> Icons.Default.Villa
        "cottage" -> Icons.Default.Cottage
        "castle" -> Icons.Default.Castle
        "library" -> Icons.Default.LocalLibrary
        else -> Icons.Default.Home // fallback về home
    }
}
