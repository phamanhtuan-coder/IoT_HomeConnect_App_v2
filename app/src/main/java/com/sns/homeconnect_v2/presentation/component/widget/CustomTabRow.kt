package com.sns.homeconnect_v2.presentation.component.widget

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một hàm composable tùy chỉnh hiển thị một hàng các tab.
 *
 * Hàm này nhận vào một danh sách các tiêu đề tab, chỉ mục tab hiện đang được chọn và một hàm
 * callback để xử lý việc chọn tab. Sau đó, nó hiển thị một `TabRow` với giao diện và hành vi
 * được tùy chỉnh.
 *
 * `TabRow` được tạo kiểu với nền trắng và màu nội dung đen. Chỉ báo tab được chọn
 * được tô màu bằng màu chính từ MaterialTheme. Tiêu đề tab được
 * hiển thị với kích thước phông chữ lớn hơn (18.sp) và tiêu đề của tab được chọn được in đậm.
 *
 * @param tabs Một danh sách các chuỗi đại diện cho tiêu đề của các tab.
 * @param selectedTabIndex Chỉ mục của tab hiện đang được chọn.
 * @param onTabSelected Một hàm callback được gọi khi một tab được chọn. Nó nhận
 *                      chỉ mục của tab được chọn làm tham số.
 * @param modifier Một `Modifier` tùy chọn để áp dụng cho `TabRow`.
 */
@Composable
fun CustomTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    IoTHomeConnectAppTheme {
        ScrollableTabRow (
            selectedTabIndex = selectedTabIndex,
            modifier = modifier.fillMaxWidth(),
            containerColor = Color.White,
            contentColor = Color.Black,
            edgePadding = 0.dp, // tùy chọn: bỏ khoảng trắng hai bên nếu muốn sát lề
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index

                Tab(
                    selected = isSelected,
                    onClick = { onTabSelected(index) },
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color.DarkGray,
                    text = {
                        Text(
                            text  = title,
                            style = MaterialTheme.typography.bodyMedium.copy(     // ← base = bodyMedium
                                fontSize   = 18.sp,                               // ← cỡ chữ lớn hơn
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTabRowPreview() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Chi tiết", "Thành viên")

    // Thay bằng IoTHomeConnectAppTheme nếu có
    IoTHomeConnectAppTheme {
        Column {
            CustomTabRow(
                tabs = tabTitles,
                selectedTabIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}