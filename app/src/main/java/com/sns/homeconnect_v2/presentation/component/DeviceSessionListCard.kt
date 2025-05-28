package com.sns.homeconnect_v2.presentation.component

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
import androidx.compose.ui.tooling.preview.Preview
import com.sns.homeconnect_v2.presentation.model.DeviceSession

/**
 * Một Composable hiển thị danh sách các phiên thiết bị.
 * Ban đầu, nó hiển thị tối đa 3 phiên và cung cấp nút "Xem thêm..."
 * để mở rộng và hiển thị tất cả các phiên. Nếu tất cả các phiên được hiển thị, nó sẽ cung cấp
 * nút "Ẩn bớt..." để thu gọn danh sách trở lại 3 phiên.
 *
 * Mỗi phiên được hiển thị bằng Composable [DeviceSessionMiniCard].
 *
 * @param sessions Danh sách các đối tượng [DeviceSession] sẽ được hiển thị.
 * @param onLogout Một hàm callback được gọi khi hành động đăng xuất được kích hoạt cho một [DeviceSession] cụ thể.
 *
 * @author Nguyễn Thanh Sang
 * @since 27-05-2025
 */
@Composable
fun DeviceSessionListCard(
    sessions: List<DeviceSession>,
    onLogout: (DeviceSession) -> Unit
) {
    var showAll by remember { mutableStateOf(false) }
    val displayList = if (showAll) sessions else sessions.take(3)

    Column {
        displayList.forEach { session ->
            var checked by remember { mutableStateOf(false) }
            DeviceSessionMiniCard(
                checked = checked,
                onCheckedChange = { checked = it },
                deviceName = session.deviceName,
                browser = session.browser,
                ip = session.ip,
                lastAccess = session.lastAccess,
                onLogout = { onLogout(session) }
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
        DeviceSession(
            id = it,
            deviceName = "Thiết bị ${it + 1}",
            browser = "Chrome",
            ip = "192.168.1.${it + 1}",
            lastAccess = "${it + 1} giờ trước"
        )
    }

    DeviceSessionListCard(
        sessions = fakeSessions,
        onLogout = {}
    )
}
