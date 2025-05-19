package com.sns.homeconnect_v2.presentation.component

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Một composable slider tùy chỉnh trải dài toàn bộ chiều rộng của vùng chứa,
 * có thiết kế track và thumb tùy chỉnh.
 *
 * Slider này được thiết kế để cung cấp giao diện trực quan "tràn cạnh".
 * Nó đạt được điều này bằng cách:
 * 1. Vẽ một track tùy chỉnh bao gồm hai phần:
 *    - Một track không hoạt động (màu nhạt hơn) lấp đầy toàn bộ chiều rộng.
 *    - Một track hoạt động (màu sáng hơn) biểu thị tiến trình hiện tại.
 * 2. Sử dụng composable `Slider` tiêu chuẩn nhưng ẩn track và thumb mặc định của nó.
 *    `Slider` tiêu chuẩn được sử dụng chủ yếu cho logic xử lý chạm và cập nhật giá trị.
 * 3. Cung cấp một composable thumb tùy chỉnh.
 *
 * @param value Giá trị hiện tại của slider (dự kiến trong khoảng từ 0f đến 255f).
 * @param onValueChange Một callback được gọi khi giá trị của slider thay đổi.
 * @param modifier [Modifier] tùy chọn cho composable này.
 * @param thumbSize Đường kính của vùng có thể chạm cho thumb.
 *                  Chiều cao trực quan của thumb được cố định ở 25.dp, nhưng điều này kiểm soát mục tiêu chạm.
 * @param trackHeight Chiều cao của track slider tùy chỉnh.
 *
 * @author Nguyễn Thanh Sang
 * @since 19-05-2025
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdgeToEdgeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    thumbSize: Dp = 40.dp,
    trackHeight: Dp = 6.dp
) {
    // Tính phần trăm chiều dài track đã kéo (0‥1)
    val fraction = (value / 255f).coerceIn(0f, 1f)

    Box(modifier) {

        /* ---------- TRACK TÙY CHỈNH ---------- */

        // Phần track chưa kéo
        Box(
            Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(trackHeight / 2))
                .background(Color.White.copy(alpha = 0.4f))
        )
        // Phần track đã kéo
        Box(
            Modifier
                .fillMaxWidth(fraction)
                .height(trackHeight)
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(trackHeight / 2))
                .background(Color.White)
        )

        /* ---------- SLIDER ẨN TRACK, CHỈ DÙNG THUMB + LOGIC ---------- */

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..255f,
            modifier = Modifier
                .fillMaxWidth()
                .height(thumbSize)          // cao đủ để nhận touch
                .align(Alignment.Center),
            colors = SliderDefaults.colors(
                thumbColor         = Color.Transparent, // Ẩn track mặc định
                activeTrackColor   = Color.Transparent,
                inactiveTrackColor = Color.Transparent,
                activeTickColor    = Color.Transparent,
                inactiveTickColor  = Color.Transparent
            ),
            thumb = {
                Box(
                    Modifier
                        .size(thumbSize, 25.dp)            // 40 × 35 dp
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
                )
            }
        )
    }
}

@Preview(
    name = "Edge-to-Edge Slider (50 %)",
    showBackground = true,
    backgroundColor = 0xFF1E88E5,     // nền xanh cho dễ nhìn
    widthDp = 360
)
@Composable
fun EdgeToEdgeSliderPreview() {
    IoTHomeConnectAppTheme {
        var value by remember { mutableFloatStateOf(128f) }      // 0‥255

        EdgeToEdgeSlider(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        )
    }
}