package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity


@Composable
fun InvertedCornerHeader(
    modifier: Modifier = Modifier,
    topEndCornerPercent: Int = 90,
    backgroundColor: Color,
    overlayColor: Color,
    content: @Composable () -> Unit
) {
    var contentHeightPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val contentHeightDp = maxOf(with(density) { contentHeightPx.toDp() }, 40.dp)

    Box(modifier = modifier.fillMaxWidth()) {

        // ✅ Overlay màu xanh đậm, luôn có kích thước vuông bằng với box dưới
        Box(
            modifier = Modifier
                .size(contentHeightDp)
                .align(Alignment.TopEnd)
                .background(overlayColor)
                .zIndex(1f)
        )

        // ✅ Nền trắng với chiều cao tối thiểu 40.dp và có thể dãn
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 40.dp)
                .onSizeChanged { contentHeightPx = it.height }
                .clip(RoundedCornerShape(topEndPercent = topEndCornerPercent))
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(topEndPercent = topEndCornerPercent)
                )
                .zIndex(2f)
        ) {
            content()
        }
    }
}


@Preview(showBackground = true, name = "InvertedCornerHeader Preview")
@Composable
fun InvertedCornerHeaderPreview() {
    InvertedCornerHeader(
        backgroundColor = Color.White,
        overlayColor = Color(0xFF3A4750) // Màu xanh dương đậm
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {},
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3A4750),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {},
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3A4750),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = "Wifi",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}