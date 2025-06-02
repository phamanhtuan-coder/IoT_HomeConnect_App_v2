package com.sns.homeconnect_v2.presentation.component

import Status
import StatusCircle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

/**
 * Hàm Composable hiển thị thẻ chi tiết phiếu.
 *
 * Thẻ này bao gồm một vòng tròn trạng thái và lý do của phiếu.
 *
 * @param status Trạng thái của phiếu (ví dụ: ĐÃ DUYỆT, ĐANG CHỜ, BỊ TỪ CHỐI).
 * @param reason Lý do liên quan đến phiếu.
 * @author Nguyễn Thanh Sang
 * @since 26-05-25
 */
@Composable
fun TicketDetailCard(
    status: Status,
    reason: String,
) {
    Row (
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        StatusCircle(status = status)
        Text(
            text = reason,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            fontSize = 34.sp,
            lineHeight = 42.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, widthDp = 350)
@Composable
fun TicketDetailCardPreview() {
    TicketDetailCard(
        status = Status.APPROVED,
        reason = "Báo mất"
    )
}
