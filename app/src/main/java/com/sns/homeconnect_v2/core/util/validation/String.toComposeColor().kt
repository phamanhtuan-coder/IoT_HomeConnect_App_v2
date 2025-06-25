package com.sns.homeconnect_v2.core.util.validation

import androidx.compose.ui.graphics.Color
import android.graphics.Color as AndroidColor   // <-- thêm alias để khỏi “đụng tên”

fun String.toComposeColor(): Color =
    Color(AndroidColor.parseColor(this))
