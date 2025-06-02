package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sns.homeconnect_v2.presentation.model.SyncHistoryItem

/**
 * Một hàm Composable hiển thị danh sách các mục lịch sử đồng bộ.
 *
 * Hàm này nhận một danh sách các đối tượng [SyncHistoryItem] và hiển thị chúng trong một [Column].
 * Nếu danh sách chứa nhiều hơn 3 mục, ban đầu nó sẽ chỉ hiển thị 3 mục đầu tiên
 * và cung cấp một nút "Xem thêm..." để mở rộng danh sách. Khi được mở rộng,
 * nút sẽ thay đổi thành "Ẩn bớt..." để thu gọn danh sách trở lại 3 mục.
 *
 * Mỗi mục trong danh sách được hiển thị bằng cách sử dụng Composable [SyncHistoryItemCard].
 *
 * @param items Danh sách các [SyncHistoryItem] sẽ được hiển thị.
 * @author Nguyễn Thanh Sang
 * @since 27-05-2025
 */
@Composable
fun SyncHistoryItemListCard(
    items: List<SyncHistoryItem>
) {
    var showAll by remember { mutableStateOf(false) }
    val displayList = if (showAll) items else items.take(3)

    Column {
        displayList.forEach { item ->
            SyncHistoryItemCard(
                deviceInfo = item.deviceInfo,
                syncTime = item.syncTime,
                status = item.status
            )
        }

        if (items.size > 3) {
            TextButton(
                onClick = { showAll = !showAll },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (showAll) "Ẩn bớt..." else "Xem thêm...",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SyncHistoryItemListCardPreview() {
    val fakeItems = listOf(
        SyncHistoryItem("Windows - Edge", "16/05/2025 15:40", true),
        SyncHistoryItem("Android - Chrome", "15/05/2025 10:15", true),
        SyncHistoryItem("Mac - Safari", "14/05/2025 09:20", false),
        SyncHistoryItem("iOS - Safari", "13/05/2025 11:05", true),
        SyncHistoryItem("Linux - Firefox", "12/05/2025 08:00", false)
    )

    SyncHistoryItemListCard(items = fakeItems)
}
