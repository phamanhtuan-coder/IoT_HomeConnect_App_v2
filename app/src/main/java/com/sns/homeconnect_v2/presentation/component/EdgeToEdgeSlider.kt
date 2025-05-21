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
 * Slider "tràn cạnh" với track và thumb có thể tùy chỉnh.
 * Slider này sẽ chiếm toàn bộ chiều rộng của container chứa nó và cho phép tùy chỉnh kiểu dáng của các thành phần.
 *
 * @param value Giá trị hiện tại của slider (từ 0f đến 255f).
 * @param onValueChange Một callback được gọi khi giá trị của slider thay đổi do người dùng kéo thumb.
 * @param modifier Modifier được áp dụng cho slider.
 * @param thumbSize Kích thước của vùng chạm cho thumb.
 * @param trackHeight Chiều cao của track slider.
 * @param thumbColor Màu của thumb.
 * @param thumbBorderColor Màu của đường viền thumb.
 * @param activeTrackColor Màu của phần track đã được kéo qua.
 * @param inactiveTrackColor Màu của phần track chưa được kéo qua.
 *
 * @author Nguyễn Thanh Sang
 * @since 20-05-2025
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdgeToEdgeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    thumbSize: Dp = 40.dp,
    trackHeight: Dp = 6.dp,
    thumbColor: Color = Color.White,
    thumbBorderColor: Color = Color.LightGray,
    activeTrackColor: Color = Color.White,
    inactiveTrackColor: Color = Color.White.copy(alpha = 0.4f)
) {
    val trackFillFraction = (value / MAX_SLIDER_VALUE).coerceIn(0f, 1f)

    Box(modifier) {

        /* ---------- TRACK ---------- */
        // Chưa kéo
        Box(
            Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(trackHeight / 2))
                .background(inactiveTrackColor)
        )
        // Đã kéo
        Box(
            Modifier
                .fillMaxWidth(trackFillFraction)
                .height(trackHeight)
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(trackHeight / 2))
                .background(activeTrackColor)
        )

        /* ---------- SLIDER ẨN TRACK ---------- */
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..255f,
            modifier = Modifier
                .fillMaxWidth()
                .height(thumbSize)
                .align(Alignment.Center),
            colors = SliderDefaults.colors(
                thumbColor         = Color.Transparent,
                activeTrackColor   = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),
            thumb = {
                Box(
                    Modifier
                        .size(thumbSize, 25.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(thumbColor)
                        .border(1.dp, thumbBorderColor, RoundedCornerShape(10.dp))
                )
            }
        )
    }
}

@Preview(
    name = "Edge-to-Edge Slider (preview)",
    showBackground = true,
    backgroundColor = 0xFF1E88E5,
    widthDp = 360
)
@Composable
fun EdgeToEdgeSliderPreview() {
    IoTHomeConnectAppTheme {
        var value by remember { mutableFloatStateOf(128f) }

        EdgeToEdgeSlider(
            value         = value,
            onValueChange = { value = it },
            activeTrackColor  = Color.Red,
            inactiveTrackColor= Color.Red.copy(alpha = 0.3f),
            thumbColor        = Color.Red,
            thumbBorderColor  = Color.DarkGray,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        )
    }
}
