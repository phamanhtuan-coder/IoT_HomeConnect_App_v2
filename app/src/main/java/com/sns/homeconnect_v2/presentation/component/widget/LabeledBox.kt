package com.sns.homeconnect_v2.presentation.component.widget

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một hàm Composable hiển thị một nhãn và một giá trị trong một hộp được định kiểu.
 * Hàm này thường được sử dụng để trình bày một thông tin với tiêu đề rõ ràng.
 *
 * @param label Văn bản hiển thị làm nhãn (ví dụ: "Số lượng thiết bị").
 * @param value Văn bản hiển thị làm giá trị bên trong hộp (ví dụ: "4").
 * @param modifier [Modifier] tùy chọn để áp dụng cho toàn bộ Row.
 * @param color Màu nền của hộp hiển thị giá trị. Mặc định là [MaterialTheme.colorScheme.primary].
 * @param textColor Màu của văn bản bên trong hộp (giá trị). Mặc định là [Color.White].
 * @param shape Hình dạng của hộp hiển thị giá trị. Mặc định là [RoundedCornerShape] với bán kính 4.dp.
 */
@Composable
fun LabeledBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,          // màu nền mặc định
    textColor: Color = Color.White,            // màu chữ mặc định
    shape: RoundedCornerShape = RoundedCornerShape(4.dp)
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Box(
            modifier = Modifier
                .size(28.dp)                    // tăng nhẹ cho dễ đọc
                .background(color, shape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LabeledBoxPreview() {
    IoTHomeConnectAppTheme {
        LabeledBox(
            label = "Số lượng thiết bị",
            value = "4",
            modifier = Modifier.padding(16.dp)
        )
    }
}