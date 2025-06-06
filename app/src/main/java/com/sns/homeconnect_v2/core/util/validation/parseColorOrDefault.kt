package com.sns.homeconnect_v2.core.util.validation

import androidx.compose.ui.graphics.Color
import android.graphics.Color as AndroidColor

fun parseColorOrDefault(hex: String?, fallback: Color = Color.Gray): Color {
    return try {
        if (hex.isNullOrBlank()) fallback else Color(AndroidColor.parseColor(hex))
    } catch (e: Exception) {
        fallback
    }
}
