package com.sns.homeconnect_v2.presentation.component


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.R
import com.sns.homeconnect_v2.presentation.component.widget.ActionIcon
import com.sns.homeconnect_v2.presentation.component.widget.SwipeableItemWithActions

/**
 * Một hàm Composable hiển thị thẻ ticket với chức năng vuốt.
 *
 * @param name Tên của người dùng liên quan đến ticket.
 * @param ticketType Loại ticket.
 * @param ticketDate Ngày của ticket.
 * @param status Trạng thái của ticket.
 * @param isRevealed Một giá trị cho biết các hành động vuốt có được hiển thị hay không.
 * @param onExpand Hàm được gọi khi mục được mở rộng (vuốt).
 * @param onCollapse Hàm được gọi khi mục được thu gọn.
 * @param onDelete Hàm được gọi khi hành động xóa được kích hoạt.
 * @param onClick Hàm được gọi khi thẻ được nhấn.
 * @param ticketId ID của ticket.
 * @param isDeleteEnabled Boolean cho biết nút xóa có được kích hoạt hay không.
 *
 * @author Nguyễn Văn A
 * @since 26-05-2025
 */
@Composable
fun TicketCardSwipeable(
    name: String = "Nguyễn Văn A",
    ticketType: String = "Báo mất",
    ticketDate: String,
    status: String? = "cancelled",
    isRevealed: Boolean = false,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onDelete: () -> Unit,
    onClick: (String) -> Unit,
    ticketId: String,
    isDeleteEnabled: Boolean = true
) {
    Log.d("TicketCardSwipeable", "Status: $status, isDeleteEnabled: $isDeleteEnabled")
    SwipeableItemWithActions(
        isRevealed = isRevealed,
        onExpanded = onExpand,
        onCollapsed = onCollapse,
        actions = {
            Spacer(Modifier.width(8.dp))
            ActionIcon(
                onClick = { if (isDeleteEnabled) onDelete() },
                backgroundColor = if (isDeleteEnabled) Color(0xFFF44336) else Color(0xFFB0B0B0),
                icon = Icons.Default.Delete,
                contentDescription = if (isDeleteEnabled) "Delete ticket" else "Delete ticket disabled",
                enabled = isDeleteEnabled,
                iconTint = if (isDeleteEnabled) Color.White else Color(0xFF808080)
            )
        }
    ) {
        Row(
            modifier = Modifier
                .background(color = Color(0xFFD8E4E8))
                .fillMaxWidth()
                .clickable { onClick(ticketId) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = ticketType,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = Color.Black
                    )
                )
                Text(
                    text = ticketDate,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Black
                    )
                )
            }
            StatusCircle(status = status?.lowercase() ?: "cancelled")
        }
    }
}

@Composable
fun StatusCircle(
    status: String,
    modifier: Modifier = Modifier
) {
    val (icon, color) = when (status) {
        "approved" -> R.drawable.ic_check_circle to Color(0xFF00C853) // Biểu tượng dấu check tròn, xanh lá
        "pending" -> R.drawable.ic_hourglass to Color(0xFFFFC107) // Biểu tượng đồng hồ cát, vàng
        "rejected" -> R.drawable.ic_close_circle to Color.Red // Biểu tượng dấu X tròn, đỏ
        "in_progress" -> R.drawable.ic_sync to Color(0xFF4CAF50) // Biểu tượng đồng bộ, xanh lá đậm
        "resolved" -> R.drawable.ic_done to Color(0xFF9C27B0) // Biểu tượng dấu check, tím
        "cancelled" -> R.drawable.shield_xmark to Color(0xFFF44336) // Biểu tượng hủy, đỏ nhạt
        else -> R.drawable.ic_info to Color.Gray // Biểu tượng thông tin, xám
    }
    Icon(
        painter = painterResource(id = icon),
        contentDescription = status,
        modifier = modifier.size(24.dp),
        tint = color
    )
}

//@Preview(showBackground = true)
//@Composable
//fun TicketCardSwipeablePreview() {
//    val tickets = remember {
//        mutableStateListOf(
//            TicketUi(1, "Nguyễn Văn A", "Báo mất", "1/1/2025", "Làm rơi chìa khóa", TicketStatus.UNPROCESSED),
//            TicketUi(2, "Trần Thị B", "Báo hỏng", "2/2/2025", "Thiết bị không hoạt động", TicketStatus.PROCESSED),
//            TicketUi(3, "Lê Văn C", "Yêu cầu hỗ trợ", "3/3/2025", "Cần hỗ trợ lắp đặt", TicketStatus.UNPROCESSED)
//        )
//    }
//    var revealedIndex by remember { mutableIntStateOf(-1) }
//
//    LazyColumn(modifier = Modifier.padding(8.dp)) {
//        itemsIndexed(tickets) { index, ticket ->
//            Spacer(modifier = Modifier.height(8.dp))
//            TicketCardSwipeable(
//                name = ticket.nameUser,
//                ticketType = ticket.typeTicket,
//                ticketDate = ticket.date,
//                status = ticket.status.toString().lowercase(),
//                isRevealed = revealedIndex == index,
//                onExpand = { revealedIndex = index },
//                onCollapse = { if (revealedIndex == index) revealedIndex = -1 },
//                onDelete = {
//                    tickets.removeAt(index)
//                    if (revealedIndex == index) revealedIndex = -1
//                },
//                onClick = {},
//                ticketId = ticket.id.toString(),
//                isDeleteEnabled = ticket.status.toString().lowercase() != "cancelled"
//            )
//        }
//    }
//}