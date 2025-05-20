package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
 * Một hàm Composable hiển thị một thẻ nhật ký thay đổi.
 *
 * Thẻ này được thiết kế để hiển thị thông tin phiên bản và tóm tắt các thay đổi.
 * Nó có nền bo góc tròn và màu nền có thể tùy chỉnh.
 *
 * @param version Chuỗi phiên bản để hiển thị (ví dụ: "V1.0.0").
 * @param summary Một chuỗi chứa tóm tắt các thay đổi cho phiên bản này.
 * @param bgColor Màu nền của thẻ. Mặc định là màu xám xanh nhạt (0xFFD8E4E8).
 *
 * @author Nguyễn Thanh Sang
 * @since 20-05-2025
 */

@Composable
fun ChangeLogCard(
    version: String,
    summary: String,
    bgColor: Color = Color(0xFFD8E4E8)
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                text = version,
                fontWeight = FontWeight.Bold,
                fontSize    = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = summary,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
            )
        }
    }
}

/* ---------------------- 1. Preview đơn  ---------------------- */
@Preview(
    name       = "Single Change-log Card",
    showBackground = true,
    widthDp    = 320
)
@Composable
fun ChangeLogCardPreview() {
    ChangeLogCard(
        version = "V0.015",
        summary = "Tóm tắt nội dung thay đổi"
    )
}

/* ---------------------- 2. Preview danh sách  ---------------------- */
@Preview(
    name       = "Change-log List",
    showBackground = true,
    widthDp    = 360,
    heightDp   = 640
)
@Composable
fun ChangeLogListPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val changes = listOf(
            "• Thêm Dark mode\n• Fix lỗi Wi-Fi",
            "• Cải thiện tốc độ quét mã",
            "• Cập nhật bản dịch tiếng Anh\n• Thêm màn hình trợ giúp"
        )
        changes.forEachIndexed { idx, sum ->
            ChangeLogCard(
                version = "V1.0.${idx + 1}",
                summary = sum
            )
        }
    }
}

