package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một Composable function hiển thị một mục chú thích của biểu đồ.
 *
 * @param color Màu của hình vuông và văn bản của mục chú thích.
 * @param label Nhãn văn bản cho mục chú thích.
 * @param modifier [Modifier] tùy chọn được áp dụng cho bố cục.
 * @param squareSize Kích thước của hình vuông màu trong mục chú thích. Mặc định là 16.dp.
 * @param labelFontSize Kích thước phông chữ của văn bản nhãn. Mặc định là 16.dp.
 * @author Nguyễn Thanh Sang
 * @since 25-05-25
 */
@Composable
fun ChartLegend(
    color: Color,
    label: String,
    modifier: Modifier = Modifier,
    squareSize: Dp = 16.dp,
    labelFontSize: Dp = 16.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(squareSize)
                .background(color = color, shape = RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = labelFontSize.value.sp,
            color = color
        )
    }
}

@Preview
@Composable
fun ChartLegendPreview() {
    ChartLegend(color = Color(0xFF2196F3), label = "Độ ẩm")
}