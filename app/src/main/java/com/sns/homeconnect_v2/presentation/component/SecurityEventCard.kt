package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một hàm Composable hiển thị thẻ sự kiện an ninh.
 *
 * Thẻ này hiển thị thông tin về một sự kiện an ninh, bao gồm tiêu đề, ngày và trạng thái.
 * Trạng thái quyết định màu sắc của văn bản trạng thái (Đỏ cho "Chưa xử lý", Xanh lá cho "Đã xử lý").
 *
 * @param title Tiêu đề của sự kiện an ninh.
 * @param date Ngày của sự kiện an ninh.
 * @param status Một giá trị boolean cho biết trạng thái của sự kiện. `true` có nghĩa là chưa xử lý, `false` có nghĩa là đã xử lý.
 * @param modifier [Modifier] tùy chọn để áp dụng cho thẻ.
 * @author Nguyễn Thanh Sang
 * @since 27-05-2025
 */
@Composable
fun SecurityEventCard(
    title: String,
    date: String,
    status: Boolean,
    modifier: Modifier = Modifier
) {
    val textColor = if (status) Color.Red else Color(0xFF2E7D32)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
        color = Color(0xFFD8E4E8),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon ở ngoài cùng trái, căn giữa dọc
            Icon(
                imageVector = Icons.Default.Warning, // Thay icon phù hợp
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp),
                tint = Color(0xFF545B62)
            )
            Spacer(modifier = Modifier.width(12.dp))
            // Phần nội dung chính (title + date)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = date,
                    color = Color(0xFF7A7F87),
                    fontSize = 20.sp,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = if (status) "Chưa xử lý" else "Đã xử lý",
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SecurityEventCardPreview() {
    SecurityEventCard(
        title = "Đăng nhập bất thường",
        date = "15/05/2025",
        status = true
    )
}
