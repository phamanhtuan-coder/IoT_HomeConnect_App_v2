package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Một hàm Composable hiển thị danh sách các tùy chọn màu theo chiều ngang để lựa chọn.
 *
 * Thành phần này cho phép người dùng chọn một màu từ danh sách được xác định trước. Màu được chọn
 * được làm nổi bật bằng đường viền. Giao diện người dùng bao gồm tiêu đề "Chọn màu sắc:".
 *
 * Các tùy chọn màu được hiển thị dưới dạng các bề mặt hình tròn. Khi một màu được chọn,
 * nó sẽ có đường viền sử dụng `MaterialTheme.colorScheme.primary`.
 *
 * @param selectedColorLabel Nhãn của màu hiện được chọn. Điều này được sử dụng để làm nổi bật
 *                           màu đang hoạt động trong giao diện người dùng.
 * @param onColorSelected Một hàm lambda được gọi khi một màu mới được chọn.
 *                        Nó nhận nhãn ([String]) của màu được chọn làm tham số của nó.
 */
@Composable
fun ColorPicker(
    selectedColorLabel: String,
    onColorSelected: (String) -> Unit
) {
    val colors = listOf(
        Color.Red to "#FF0000",
        Color.Green to "#00FF00",
        Color.Blue to "#0000FF",
        Color.Yellow to "#FFFF00",
        Color.Cyan to "#00FFFF",
        Color.Magenta to "#FF00FF",
        Color.Gray to "#808080",
        Color.Black to "#000000",
        Color.White to "#FFFFFF",
        Color(0xFF2196F3) to "#2196F3"
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Chọn màu sắc:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            colors.forEach { (color, label) ->
                Surface(
                    shape = CircleShape,
                    color = color,
                    border = if (label == selectedColorLabel)
                        BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
                    else null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onColorSelected(label) }
                ) {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewColorPickerCombined() {
    var selectedLabel by remember { mutableStateOf("blue") }

    ColorPicker(
        selectedColorLabel = selectedLabel,
        onColorSelected = { selectedLabel = it }
    )

}