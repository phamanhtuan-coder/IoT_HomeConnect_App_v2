package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một hàm composable hiển thị một thẻ cho một mục lịch sử đồng bộ hóa.
 *
 * @param deviceInfo Một chuỗi chứa thông tin về thiết bị đã được đồng bộ hóa.
 * @param syncTime Một chuỗi đại diện cho thời gian đồng bộ hóa.
 * @param status Một boolean cho biết liệu đồng bộ hóa có thành công (true) hay thất bại (false). Mặc định là true.
 * @author Nguyễn Thanh Sang
 * @since 27-05-2025
 */
@Composable
fun SyncHistoryItemCard(
    deviceInfo: String,
    syncTime: String,
    status: Boolean = true
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFD8E4E8),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
        ) {
            Icon(
                imageVector = if (status) Icons.Default.CheckCircleOutline else Icons.Default.Warning,
                contentDescription = "Success",
                tint = if (status) Color(0xFF4CAF50) else Color.Red, // Màu xanh thành công
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = deviceInfo,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black
                )
                Text(
                    text = "Thời gian đồng bộ",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = syncTime,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = if (status) "Thành công" else "Thất bại",
                color = if(status) Color(0xFF2E7D32) else Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SyncHistoryItemCardPreview() {
    SyncHistoryItemCard(
        deviceInfo = "Windows - Edge",
        syncTime = "16/05/2025 15:40",
        status = false
    )
}
