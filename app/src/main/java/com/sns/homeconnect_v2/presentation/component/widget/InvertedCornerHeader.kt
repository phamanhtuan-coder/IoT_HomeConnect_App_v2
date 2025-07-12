package com.sns.homeconnect_v2.presentation.component.widget

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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


/**
 * Một hàm composable tạo ra một header với hiệu ứng góc lõm.
 *
 * Composable này tạo ra một header với màu nền và màu phủ.
 * Màu phủ tạo hiệu ứng góc lõm ở góc trên bên phải của header.
 * Nội dung của header được cung cấp dưới dạng một lambda composable.
 *
 * @param modifier Modifier được áp dụng cho header.
 * @param topEndCornerPercent Phần trăm bo góc ở góc trên bên phải.
 * @param backgroundColor Màu nền của header.
 * @param overlayColor Màu của lớp phủ tạo hiệu ứng góc lõm.
 * @param content Nội dung của header.
 */
@Composable
fun InvertedCornerHeader(
    modifier: Modifier = Modifier,
    topEndCornerPercent: Int = 100, // Tăng bo góc
    backgroundColor: Color,
    overlayColor: Color,
    content: @Composable () -> Unit
) {
    var contentHeightPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val contentHeightDp = maxOf(with(density) { contentHeightPx.toDp() }, 48.dp)

    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(contentHeightDp)
                .align(Alignment.TopEnd)
                .background(overlayColor)
                .zIndex(1f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .onSizeChanged { contentHeightPx = it.height }
                //.clip(RoundedCornerShape(topEndPercent = topEndCornerPercent))
                .background(backgroundColor)
                //.border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(topEndPercent = topEndCornerPercent))
                .zIndex(2f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InvertedCornerHeaderPreview() {
    IoTHomeConnectAppTheme {
        InvertedCornerHeader(
            backgroundColor = MaterialTheme.colorScheme.surface,
            overlayColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {},
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "Wifi",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}