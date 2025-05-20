package com.sns.homeconnect_v2.presentation.component

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một composable function hiển thị danh sách các mục cuộn dọc,
 * với mục hiện đang được chọn được làm nổi bật.
 *
 * Thành phần này tạo ra một danh sách lặp vô hạn bằng cách lặp lại các mục được cung cấp.
 * Nó sử dụng [LazyListState] để quản lý vị trí cuộn và xác định mục được chọn.
 * Mục được chọn có thể phân biệt trực quan với kích thước phông chữ lớn hơn và độ mờ hoàn toàn,
 * trong khi các mục khác có kích thước phông chữ nhỏ hơn và độ mờ giảm.
 *
 * @param T Loại của các mục trong danh sách.
 * @param state [LazyListState] để kiểm soát và quan sát việc cuộn danh sách.
 * @param items Danh sách các mục cần hiển thị.
 * @param modifier [Modifier] tùy chọn được áp dụng cho `RollingColumn`.
 * @param label Một hàm lambda nhận một mục kiểu `T` và trả về một [String]
 *              đại diện để hiển thị.
 * @param fontSize Kích thước phông chữ cho mục được chọn. Các mục không được chọn sẽ có
 *                 kích thước phông chữ là `fontSize * 0.8f`. Mặc định là `36.sp`.
 *
 * @author Nguyễn Thanh Sang
 * @since 19-05-2025
 */

@Composable
fun <T> RollingColumn(
    state: LazyListState,
    items: List<T>,
    modifier: Modifier = Modifier,
    label: (T) -> String,
    fontSize: TextUnit = 36.sp // Kích thước font cho mục được chọn
) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        val snappingBehavior = rememberSnapFlingBehavior(state)

        // derivedState tránh đọc trực tiếp state trong composable
        val selectedIndex by remember {
            derivedStateOf {
                (state.firstVisibleItemIndex + 1) % items.size
            }
        }

        LazyColumn(
            state = state,
            flingBehavior = snappingBehavior,
            modifier = modifier
                .height(140.dp)
                .background(colorScheme.background.copy(alpha = 0.8f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items.size * 100) { index ->
                val item = items[index % items.size]
                val isSelected = (index % items.size == selectedIndex)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label(item),
                        fontSize = if (isSelected) fontSize else fontSize * 0.8f,
                        color = colorScheme.onBackground.copy(alpha = if (isSelected) 1f else 0.4f),
                        modifier = Modifier.padding(vertical = if (isSelected) 6.dp else 4.dp)
                    )
                }
            }
        }
    }
}
