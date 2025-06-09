package com.sns.homeconnect_v2.core.util.validation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getIconByName(name: String?): ImageVector {
    return name?.toIcon() ?: Icons.Default.Home
}

// Extension function để chuyển String sang Icon
fun String.toIcon(): ImageVector {
    return when (this.lowercase()) {
        "home" -> Icons.Default.Home
        "work" -> Icons.Default.Work
        "school" -> Icons.Default.School
        "apartment" -> Icons.Default.Apartment
        "hotel" -> Icons.Default.Hotel
        "villa" -> Icons.Default.Villa
        "cottage" -> Icons.Default.Cottage
        "castle" -> Icons.Default.Castle
        "library" -> Icons.Default.LocalLibrary
        "account_balance" -> Icons.Default.AccountBalance
        else -> Icons.Default.Home
    }
}

// Extension function để chuyển Icon sang String name
fun ImageVector.toIconString(): String {
    return when (this) {
        Icons.Default.Home -> "home"
        Icons.Default.Work -> "work"
        Icons.Default.School -> "school"
        Icons.Default.Apartment -> "apartment"
        Icons.Default.Hotel -> "hotel"
        Icons.Default.Villa -> "villa"
        Icons.Default.Cottage -> "cottage"
        Icons.Default.Castle -> "castle"
        Icons.Default.LocalLibrary -> "library"
        Icons.Default.AccountBalance -> "account_balance"
        else -> "home"
    }
}
