package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một hàm Composable hiển thị một bộ chọn khoảng thời gian.
 *
 * Thành phần này cho phép người dùng chọn một khoảng thời gian được xác định trước (ví dụ: "Hôm nay", "Tuần này")
 * hoặc một khoảng ngày tùy chỉnh. Nó hiển thị khoảng thời gian đã chọn và mở một bottom sheet
 * để chọn hoặc tùy chỉnh khoảng thời gian.
 *
 * Giao diện người dùng bao gồm một Button, khi được nhấp vào, sẽ mở một `QuickTimeRangeBottomSheet`.
 * Button hiển thị một biểu tượng, văn bản "Chọn thời gian" và khoảng thời gian hiện được chọn.
 *
 * Trạng thái:
 *  - `sheetOpen`: Một trạng thái boolean để kiểm soát khả năng hiển thị của `QuickTimeRangeBottomSheet`.
 *  - `selectedRange`: Một trạng thái chuỗi để lưu trữ văn bản khoảng thời gian hiện được chọn.
 *                     Mặc định là "Hôm nay".
 *  - `customRange`: Một Pair các Chuỗi có thể null để lưu trữ ngày bắt đầu và ngày kết thúc của một khoảng tùy chỉnh.
 *                   Giá trị này được đặt khi người dùng chọn một khoảng ngày tùy chỉnh trong bottom sheet.
 *
 * @author Nguyễn Thanh Sang
 * @since 29-05-2025
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeRangePicker() {
    var sheetOpen by remember { mutableStateOf(false) }
    var selectedRange by remember { mutableStateOf("Hôm nay") }
    var customRange by remember { mutableStateOf<Pair<String, String>?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { sheetOpen = true },
            modifier = Modifier
                .height(57.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF4F7FB), // Nền nhạt nhẹ, không trắng hoàn toàn
                contentColor = Color(0xFF2979FF)
            ),
            border = BorderStroke(2.dp, Color(0xFF2979FF)),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 12.dp
            )
        ) {
            // Icon lịch nằm trong vòng tròn nhỏ, đậm
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Color(0x332979FF), shape = RoundedCornerShape(20.dp)), // nền xanh nhạt nhạt
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Chọn ngày",
                    tint = Color(0xFF2979FF),
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Chọn thời gian",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF2979FF),
                    letterSpacing = 1.sp
                )
                if (selectedRange.isNotBlank() && selectedRange != "Chọn thời gian") {
                    Text(
                        text = selectedRange,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Mở chọn ngày",
                tint = Color(0xFF2979FF),
                modifier = Modifier.size(28.dp)
            )
        }

        QuickTimeRangeBottomSheet(
            open = sheetOpen,
            onDismiss = { sheetOpen = false },
            selected = selectedRange,
            onSelect = {
                selectedRange = it
                customRange = null
            },
            onCustomDate = { start, end ->
                selectedRange = "$start - $end"
                customRange = start to end
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeRangePickerDemo() {
    TimeRangePicker()
}
