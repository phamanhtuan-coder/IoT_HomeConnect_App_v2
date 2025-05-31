package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một hàm Composable hiển thị một hàng các tab để chọn khoảng thời gian (ví dụ: "24H", "Tuần", "Tháng")
 * và một nút lịch.
 *
 * Thành phần này cho phép người dùng chuyển đổi giữa các khoảng thời gian được xác định trước và cũng có thể mở chế độ xem lịch.
 *
 * @param tabs Danh sách các chuỗi đại diện cho nhãn của mỗi tab.
 *             Mặc định là `listOf("24H", "Tuần", "Tháng")`.
 * @param selectedTab Chỉ mục của tab được chọn ban đầu. Mặc định là `2`.
 * @param onTabSelected Một hàm lambda được gọi khi một tab được chọn.
 *                      Nó nhận chỉ mục của tab đã chọn làm tham số. Mặc định là một lambda trống.
 * @param onCalendarClick Một hàm lambda được gọi khi nút lịch được nhấp.
 *                        Mặc định là một lambda trống.
 * @author Nguyễn THanh Sang
 * @since 29-05-2025
 */
@Composable
fun TimeTabWithCalendar(
    tabs: List<String> = listOf("24H", "Tuần", "Tháng"),
    selectedTab: Int = 2,
    onTabSelected: (Int) -> Unit = {},
    onCalendarClick: () -> Unit = {}
) {
    var currentTab by remember { mutableIntStateOf(selectedTab) }
    var showSheet by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
//    var result by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .height(72.dp), // Điều chỉnh chiều cao cho giống ảnh
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tabs Section
        Row(
            modifier = Modifier
                .weight(1f)
                .height(64.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFFE5E8EC),
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(horizontal = 6.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { idx, label ->
                val isSelected = idx == currentTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(vertical = 2.dp, horizontal = 2.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) Color(0xFFE6F0FE) else Color.Transparent
                        )
                        .clickable {
                            currentTab = idx
                            onTabSelected(idx)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color(0xFF1877F2) else Color(0xFF545A63),
                        fontSize = 18.sp
                    )
                }
            }
        }
        // Space between tabs and calendar button
        Spacer(Modifier.width(18.dp))
        // Calendar Button
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(
                    width = 2.dp,
                    color = Color(0xFFE5E8EC),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { showSheet = true },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Calendar",
                tint = Color(0xFF545A63),
                modifier = Modifier.size(29.dp)
            )
        }

        if (showSheet) {
            DateRangeBottomSheetWithTextField(
                startDate = startDate,
                endDate = endDate,
                onStartDateChange = { startDate = it },
                onEndDateChange = { endDate = it },
                onDismiss = { showSheet = false },
//                onConfirm = { start, end ->
//                    result = "$start đến $end"
//                    showSheet = false
//                }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F6FA)
@Composable
private fun PreviewTimeTabWithCalendar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F6FA))
    ) {
        TimeTabWithCalendar()
    }
}




