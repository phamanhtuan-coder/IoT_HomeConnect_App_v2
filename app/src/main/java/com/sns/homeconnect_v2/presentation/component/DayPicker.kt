package com.sns.homeconnect_v2.presentation.component

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một hàm Composable hiển thị bộ chọn ngày.
 *
 * Hàm này cho phép người dùng chọn nhiều ngày trong tuần.
 * Các ngày đã chọn được hiển thị bên dưới bộ chọn.
 *
 * Giao diện người dùng bao gồm:
 * - Một tiêu đề "Select Days".
 * - Một hàng các nút, mỗi nút đại diện cho một ngày trong tuần (Thứ Hai, Thứ Ba, Thứ Tư, Thứ Năm, Thứ Sáu, Thứ Bảy, Chủ Nhật).
 * - Văn bản bên dưới các nút hiển thị các ngày hiện đang được chọn, được phân tách bằng dấu phẩy.
 *
 * Khi một nút ngày được nhấp vào:
 * - Nếu ngày đó đã được chọn, nó sẽ bị bỏ chọn.
 * - Nếu ngày đó chưa được chọn, nó sẽ được chọn.
 *
 * Thành phần này sử dụng MaterialTheme để tạo kiểu.
 *
 * @Preview Chú thích này cho phép xem trước Composable trong Android Studio.
 * @author Nguyễn Thanh Sang
 * @since 19-05-2025
 */

@Preview
@Composable
fun DayPicker() {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        // Danh sách các thứ trong tuần
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        // State lưu trữ các ngày đã được chọn
        var selectedDays by remember { mutableStateOf(setOf<String>()) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Days",
                fontSize = 24.sp,
                color = colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Hiển thị các ngày trong tuần dưới dạng Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                daysOfWeek.forEach { day ->
                    DayItem(
                        day = day,
                        isSelected = selectedDays.contains(day),
                        onDayClicked = {
                            selectedDays = if (selectedDays.contains(day)) {
                                selectedDays - day
                            } else {
                                selectedDays + day
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị kết quả các ngày đã chọn
            Text(
                text = "Selected Days: ${selectedDays.joinToString(", ")}",
                fontSize = 16.sp,
                color = colorScheme.onSecondary
            )
        }
    }
}

@Composable
fun DayItem(day: String, isSelected: Boolean, onDayClicked: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(50.dp)
            .background(
                color = if (isSelected) Color(0xFF4CAF50) else Color(0xFFEEEEEE),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onDayClicked() }
    ) {
        Text(
            text = day,
            fontSize = 16.sp,
            color = if (isSelected) Color.White else Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DayItemPreviewSelected() {
    DayItem(
        day = "Mon",
        isSelected = true,
        onDayClicked = {}
    )
}