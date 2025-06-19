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
        "blue", "#2196F3" -> Color(0xFF2196F3)
        "red", "#F44336" -> Color(0xFFF44336)
        "green", "#4CAF50" -> Color(0xFF4CAF50)
        "yellow", "#FFEB3B" -> Color(0xFFFFEB3B)
        "purple", "#9C27B0" -> Color(0xFF9C27B0)
        "orange", "#FF9800" -> Color(0xFFFF9800)
        "teal", "#009688" -> Color(0xFF009688)
        "pink", "#E91E63" -> Color(0xFFE91E63)
        "cyan", "#00BCD4" -> Color(0xFF00BCD4)
        "gray", "grey", "#9E9E9E" -> Color(0xFF9E9E9E)
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
