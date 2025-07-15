package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sns.homeconnect_v2.presentation.model.Room

/**
 * Một hàm composable hiển thị nội dung của một tab phòng.
 * Nó hiển thị biểu tượng và tên của phòng, với kiểu dáng khác nhau dựa trên việc tab có đang hoạt động hay không.
 *
 * @param room Đối tượng [Room] chứa dữ liệu để hiển thị (biểu tượng và tên).
 * @param isActive Một giá trị boolean cho biết tab này hiện có đang được chọn/hoạt động hay không.
 *                 Điều này ảnh hưởng đến màu nền của biểu tượng và kiểu chữ.
 *
 * @author Nguyễn Thanh Sang
 * @since 29-05-2025
 */
@Composable
fun RoomTabContent(room: Room, isActive: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 3.dp)
            .wrapContentWidth()
    ) {
        Box(
            modifier = Modifier
                .size(if (isActive) 36.dp else 32.dp) // Tăng kích thước cho dễ nhìn
                .background(
                    color = if (isActive) Color(0xFF1E88E5) else Color(0xFFF7F2FA),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = room.icon,
                contentDescription = room.name,
                tint = if (isActive) Color(0xFFFFFFFF) else Color(0xFF9CA3AF),
                modifier = Modifier.size(if (isActive) 20.dp else 18.dp)
            )
        }
        if (isActive) { // Chỉ hiển thị tên cho tab được chọn
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = room.name,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = Color(0xFF1E88E5),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis, // Ngăn cắt chữ
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRoomTabContent_Active() {
    RoomTabContent(
        room = Room(
            id = "1",
            name = "Phòng Khách",
            icon = Icons.Default.Home
        ),
        isActive = true
    )
}