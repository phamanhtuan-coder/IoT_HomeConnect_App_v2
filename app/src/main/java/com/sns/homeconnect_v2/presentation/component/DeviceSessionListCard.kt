package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sns.homeconnect_v2.data.remote.dto.response.UserActivityResponse

/**
 * Một Composable hiển thị danh sách các phiên thiết bị.
 * Các phiên được chia thành hai nhóm: "Thiết bị đang hoạt động" (online) và "Thiết bị chưa hoạt động" (offline).
 * Ban đầu, nó hiển thị tối đa 3 phiên tổng cộng (bao gồm cả online và offline).
 * Nếu có nhiều hơn 3 phiên, một nút "Xem thêm..." được cung cấp để mở rộng và hiển thị tất cả các phiên.
 * Nếu tất cả các phiên được hiển thị, nút này sẽ chuyển thành "Ẩn bớt..." để thu gọn danh sách trở lại hiển thị tối đa 3 phiên.
 *
 * Mỗi phiên được hiển thị bằng Composable [DeviceSessionCard].
 *
 * @param sessions Danh sách các đối tượng [UserActivityResponse] sẽ được hiển thị.
 * @param onLogout Một hàm callback được gọi khi hành động đăng xuất được kích hoạt cho một [UserActivityResponse] cụ thể.
 *
 * @author Nguyễn Thanh Sang
 * @since 27-05-2025
 */
@Composable
fun DeviceSessionListCard(
    sessions: List<UserActivityResponse>,
    onLogout: (() -> Unit)? = null,
) {
    var showAll by remember { mutableStateOf(false) }
//    val displayList = if (showAll) sessions else sessions.take(3)

    // Phân chia danh sách thiết bị thành hai nhóm online và offline
    val onlineSessions = sessions.filter { isLoggedIn(it.last_out) }
    val offlineSessions = sessions.filter { !isLoggedIn(it.last_out) }

    Column (
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Thiết bị đang hoạt động",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        )

        onlineSessions.forEach { session ->
            DeviceSessionCard(
                userActivityResponse = session,
                onLogout = onLogout
            )
        }

        Text(
            text = "Thiết bị chưa hoạt động",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        )

        offlineSessions.forEach { session ->
            DeviceSessionCard(
                userActivityResponse = session,
                onLogout = onLogout
            )
        }

        if (sessions.size > 3) {
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
fun DeviceSessionListCardPreview() {
    val fakeSessions = List(5) {
        UserActivityResponse(
            user_device_id = 1,
            user_id = "CUST4E040TZCD017N1YK6B2OMNYX",
            device_name = "iPhone 13 Pro",
            device_id = "device123",
            device_uuid = "UDVCE4I2EUDTUCQXXILDZMR62GJO",
            device_token = null,
            last_login = "2025-06-05T02:46:39.000Z",  // Ví dụ login gần đây
            last_out = "2025-06-05T03:46:39.000Z", // Dữ liệu login
            created_at = "2025-06-04T03:49:35.000Z",
            updated_at = "2025-06-05T02:46:39.000Z",
            is_deleted = false
        )
    }

    DeviceSessionListCard(
        sessions = fakeSessions,
        onLogout = {}
    )
}
