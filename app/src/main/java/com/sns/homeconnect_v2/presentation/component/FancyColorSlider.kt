package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.sns.homeconnect_v2.data.remote.dto.request.AttributeRequest

/**
 * Một hàm Composable hiển thị một thanh trượt màu với bảng màu được xác định trước.
 *
 * Thanh trượt này cho phép người dùng chọn một màu từ một dải màu đã được xác định trước.
 * Màu được chọn sẽ được hiển thị dưới dạng văn bản cùng với mã hex của nó.
 *
 * @param attribute Đối tượng [AttributeRequest] chứa màu hiện tại.
 *                  Thuộc tính `color` của đối tượng này sẽ được cập nhật khi màu thay đổi.
 * @param onColorChange Một hàm lambda được gọi khi màu được chọn thay đổi.
 *                      Nó nhận vào chuỗi hex của màu mới.
 * @param modifier [Modifier] tùy chọn được áp dụng cho Composable gốc.
 * @param thumbDia Đường kính của thumb (con trượt). Mặc định là `40.dp`.
 * @param trackHeight Chiều cao của track/dải màu. Mặc định là `12.dp`.
 *
 * @author Nguyễn Thanh Sang
 * @since 19-05-2025
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FancyColorSlider(
    attribute: AttributeRequest,
    onColorChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    thumbDia: Dp = 40.dp,          // đường kính thumb
    trackHeight: Dp = 12.dp        // chiều cao track/gradient
) {
    val palette = listOf(
        Color.Black to "Black",
        Color.Gray to "Gray",
        Color.LightGray to "Light Gray",
        Color.White to "White",
        Color(0xFF800000) to "Maroon",
        Color.Red to "Red",
        Color(0xFFFFA500) to "Orange",
        Color.Yellow to "Yellow",
        Color(0xFF808000) to "Olive",
        Color.Green to "Green",
        Color(0xFF00FF00) to "Lime",
        Color.Cyan to "Cyan",
        Color(0xFF0000FF) to "Blue",
        Color.Blue to "Navy",
        Color(0xFF800080) to "Purple",
        Color.Magenta to "Fuchsia"
    )

    /* ---------- state ---------- */
    var pos by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(attribute.color) {             // chạy mỗi khi màu thay đổi
        pos = findClosestColorPosition(
            parseHexToColor(attribute.color) ?: Color.White,
            palette.map { it.first }
        )
    }

    val idx = (pos * (palette.size - 1)).toInt()
    val currentColor = palette[idx].first

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* ---------- track & slider ---------- */
        Box(                                          // cha cao đủ cho thumb
            Modifier
                .fillMaxWidth()
                .height(thumbDia)                     // 26 dp
        ) {
            // vẽ dải màu mỏng ở chính giữa
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(trackHeight)              // 12 dp
                    .align(Alignment.Center)
                    .drawBehind {
                        drawRoundRect(
                            brush = Brush.horizontalGradient(palette.map { it.first }),
                            cornerRadius = CornerRadius(trackHeight.toPx() / 2)
                        )
                    }
            )

            // slider ẩn track gốc
            Slider(
                value = pos,
                onValueChange = { if (enabled) pos = it },
                onValueChangeFinished = {
                    if (enabled) {
                        val i = (pos * (palette.size - 1)).toInt()
                        val hex = colorToHex(palette[i].first)
                        attribute.color = hex
                        onColorChange(hex)
                    }
                },
                enabled = enabled,
                valueRange = 0f..1f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(thumbDia)
                    .align(Alignment.Center),
                colors = SliderDefaults.colors(
                    thumbColor         = Color.Transparent,
                    activeTrackColor   = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                ),
                thumb = {
                    Box(
                        Modifier
                            .size(thumbDia, 25.dp)            // 40 × 35 dp
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
                    )
                }
            )
        }

        Spacer(Modifier.height(8.dp))
        Text(
            text = "${palette[idx].second}  •  ${colorToHex(currentColor)}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Spacer(Modifier.height(8.dp))
    }
}

// Hàm chuyển đổi mã hex sang Color
fun parseHexToColor(hex: String): Color? {
    return try {
        val colorInt = hex.toColorInt()
        Color(colorInt)
    } catch (_: IllegalArgumentException) {
        null
    }
}

// Hàm tìm vị trí gần nhất của màu trong danh sách
fun findClosestColorPosition(targetColor: Color, colors: List<Color>): Float {
    var closestIndex = 0
    var minDistance = Float.MAX_VALUE

    colors.forEachIndexed { index, color ->
        val distance = colorDistance(targetColor, color)
        if (distance < minDistance) {
            minDistance = distance
            closestIndex = index
        }
    }

    return closestIndex.toFloat() / (colors.size - 1)
}

// Hàm tính khoảng cách giữa hai màu
fun colorDistance(color1: Color, color2: Color): Float {
    val rDiff = color1.red - color2.red
    val gDiff = color1.green - color2.green
    val bDiff = color1.blue - color2.blue
    return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff
}

// Hàm chuyển đổi Color sang mã hex
fun colorToHex(color: Color): String {
    val argb = color.toArgb()
    val r = (argb shr 16) and 0xFF
    val g = (argb shr 8) and 0xFF
    val b = argb and 0xFF
    return String.format("#%02X%02X%02X", r, g, b)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FancyColorSliderPreview() {
    val dummyAttribute = remember {
        AttributeRequest(
            brightness = 100,
            color = "#FF0000"
        )
    }

    FancyColorSlider(
        attribute = dummyAttribute,
        onColorChange = {}
    )
}
