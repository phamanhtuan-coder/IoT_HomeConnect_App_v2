package com.sns.homeconnect_v2.core.util.validation

import androidx.compose.ui.graphics.Color
import android.graphics.Color as AndroidColor

fun parseColorOrDefault(hex: String?, fallback: Color = Color.Gray): Color {
    return try {
        if (hex.isNullOrBlank()) fallback else Color(AndroidColor.parseColor(hex))
    } catch (_: Exception) {
        fallback
    }
}

// Extension function để chuyển String sang Color
fun String.toColor(): Color {
    return when (this.lowercase()) {
        "blue" -> Color(0xFF2196F3)
        "red" -> Color(0xFFF44336)
        "green" -> Color(0xFF4CAF50)
        "yellow" -> Color(0xFFFFEB3B)
        "purple" -> Color(0xFF9C27B0)
        "orange" -> Color(0xFFFF9800)
        "teal" -> Color(0xFF009688)
        "pink" -> Color(0xFFE91E63)
        "cyan" -> Color(0xFF00BCD4)
        "gray", "grey" -> Color(0xFF9E9E9E)
        else -> Color(0xFF2196F3) // Default blue
    }
}

// Extension function để chuyển Color sang String name
fun Color.toColorString(): String {
    return when (this) {
        Color(0xFF2196F3) -> "blue"
        Color(0xFFF44336) -> "red"
        Color(0xFF4CAF50) -> "green"
        Color(0xFFFFEB3B) -> "yellow"
        Color(0xFF9C27B0) -> "purple"
        Color(0xFFFF9800) -> "orange"
        Color(0xFF009688) -> "teal"
        Color(0xFFE91E63) -> "pink"
        Color(0xFF00BCD4) -> "cyan"
        Color(0xFF9E9E9E) -> "gray"
        else -> "blue"
    }
}
