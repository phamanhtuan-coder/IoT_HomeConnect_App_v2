package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

/**
 * Một hàm composable hiển thị một thẻ mini cho một phiên thiết bị.
 *
 * @param checked Liệu hộp kiểm có được chọn hay không.
 * @param onCheckedChange Hàm callback khi trạng thái hộp kiểm thay đổi.
 * @param deviceName Tên của thiết bị.
 * @param browser Trình duyệt được sử dụng cho phiên.
 * @param ip Địa chỉ IP của thiết bị.
 * @param lastAccess Thời gian truy cập cuối cùng của phiên.
 * @param onLogout Hàm callback khi nút đăng xuất được nhấp.
 * @param modifier Modifier để áp dụng cho composable.
 *
 * @author Nguyễn Thanh Sang
 * @since 27-05-2025
 */
@Composable
fun DeviceSessionMiniCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    deviceName: String,
    browser: String,
    ip: String,
    lastAccess: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(end = 6.dp)
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFD8E4E8),
            shadowElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Devices,
                    contentDescription = null,
                    tint = Color(0xFF607D8B),
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 6.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$deviceName - $browser",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        maxLines = 1
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = ip,
                        color = Color(0xFF757575),
                        fontSize = 14.sp
                    )
                    Text(
                        text = lastAccess,
                        color = Color(0xFFB0B0B0),
                        fontSize = 13.sp
                    )
                }
                Spacer(Modifier.width(8.dp))
                TextButton(
                    onClick = onLogout,
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
                ) {
                    Text(
                        "Đăng xuất",
                        color = Color(0xFFD32F2F),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun DeviceSessionMiniCardPreview() {
    var checked by remember { mutableStateOf(false) }

    DeviceSessionMiniCard(
        checked = checked,
        onCheckedChange = { checked = it },
        deviceName = "Android",
        browser = "Chrome",
        ip = "192.168.1.2",
        lastAccess = "2 giờ trước",
        onLogout = {}
    )
}

