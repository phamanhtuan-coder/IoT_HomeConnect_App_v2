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
            .padding(vertical = 8.dp)
            .wrapContentWidth()
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = if (isActive) Color(0xFF2979FF) else Color(0xFFF4F7FB),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = room.icon,
                contentDescription = room.name,
                tint = if (isActive) Color.White else Color(0xFF404B5A),
                modifier = Modifier.size(34.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = room.name,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp,
            color = if (isActive) Color(0xFF2979FF) else Color(0xFF404B5A),
            modifier = Modifier.padding(top = 2.dp)
        )
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