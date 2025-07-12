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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush

/**
 * Một hàm Composable hiển thị một Box với nền màu và góc dưới bên trái được bo tròn.
 *
 * @param backgroundColor Màu nền. Mặc định là màu chính từ MaterialTheme.
 * @param cornerRadius Bán kính của góc dưới bên trái được bo tròn. Mặc định là 40.dp.
 * @param content Nội dung sẽ được hiển thị bên trong Box.
 */
@Composable
fun ColoredCornerBox(
    startColor: Color = MaterialTheme.colorScheme.primary, // Xanh dương đậm
    endColor: Color = Color(0xFFE3F2FD), // Xanh nhạt
    cornerRadius: Dp = 40.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 57.dp)
            .clip(RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(startColor, endColor)
                )
                )
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun ColoredCornerBoxPreview() {
    IoTHomeConnectAppTheme {
        ColoredCornerBox(
            startColor = Color(0xFF1976D2), // Xanh dương đậm
            endColor = Color(0xFFE3F2FD), // Xanh nhạt
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