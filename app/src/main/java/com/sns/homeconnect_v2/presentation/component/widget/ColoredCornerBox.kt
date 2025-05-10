package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding

@Composable
fun ColoredCornerBox(
    backgroundColor: Color,
    cornerRadius: Dp = 40.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(bottomStart = cornerRadius)
            )
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun ColoredCornerBoxPreview() {
    ColoredCornerBox(
        backgroundColor = Color(0xFF3A4750), // Màu xanh dương đẹp hơn
        cornerRadius = 40.dp
    ) {
        Text(
            text = "Preview Box",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp) // Tăng khoảng cách bên trong
        )
    }
}
