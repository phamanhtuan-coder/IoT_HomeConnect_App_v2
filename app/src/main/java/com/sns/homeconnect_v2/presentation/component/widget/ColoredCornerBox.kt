package com.sns.homeconnect_v2.presentation.component.widget

import IoTHomeConnectAppTheme
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip

@Composable
fun ColoredCornerBox(
    backgroundColor: Color = MaterialTheme.colorScheme.primary, // ✅ dùng colorScheme
    cornerRadius: Dp = 40.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = cornerRadius))
            .background(backgroundColor)
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun ColoredCornerBoxPreview() {
    IoTHomeConnectAppTheme { // ✅ Bao bọc Theme
        ColoredCornerBox(
            cornerRadius = 40.dp
        ) {
            Text(
                text = "Preview Box",
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp)
            )
        }
    }
}

