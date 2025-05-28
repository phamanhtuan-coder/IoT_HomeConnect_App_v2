package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.presentation.model.SecurityEvent

/**
 * Một hàm Composable hiển thị danh sách các sự kiện bảo mật.
 *
 * Hàm này nhận một danh sách các đối tượng [SecurityEvent] và hiển thị chúng dưới dạng thẻ.
 * Ban đầu, nó hiển thị tối đa 3 sự kiện. Nếu có nhiều hơn 3 sự kiện,
 * một nút "Xem thêm..." sẽ được hiển thị. Nhấp vào nút này sẽ hiển thị
 * tất cả các sự kiện và thay đổi văn bản nút thành "Ẩn bớt...".
 * Nhấp lại vào nút sẽ trở lại hiển thị chỉ 3 sự kiện đầu tiên.
 *
 * Mỗi sự kiện được hiển thị bằng cách sử dụng composable [SecurityEventCard].
 *
 * @param events Danh sách các đối tượng [SecurityEvent] sẽ được hiển thị.
 * @author Nguyễn Thanh Sang
 * @since 27-05-2025
 */
@Composable
fun SecurityEventListCard(
    events: List<SecurityEvent>
) {
    var showAll by remember { mutableStateOf(false) }
    val displayList = if (showAll) events else events.take(3)

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
        displayList.forEach { event ->
            Spacer(modifier = Modifier.height(8.dp))
            SecurityEventCard(
                title = event.title,
                date = event.date,
                status = event.status
            )
        }

        if (events.size > 3) {
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
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SecurityEventListCardPreview() {
    val fakeEvents = listOf(
        SecurityEvent("Đăng nhập bất thường", "15/05/2025", true),
        SecurityEvent("IP lạ", "14/05/2025", true),
        SecurityEvent("Thiết bị mới", "13/05/2025", false),
        SecurityEvent("Mật khẩu sai nhiều lần", "12/05/2025", true),
        SecurityEvent("Trình duyệt mới", "11/05/2025", false)
    )

    SecurityEventListCard(
        events = fakeEvents
    )
}


