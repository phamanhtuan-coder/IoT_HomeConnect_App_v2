package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sns.homeconnect_v2.presentation.model.TicketUi
import com.sns.homeconnect_v2.presentation.component.widget.ActionIcon
import com.sns.homeconnect_v2.presentation.component.widget.SwipeableItemWithActions

/**
 * Một hàm Composable hiển thị thẻ ticket với chức năng vuốt để xóa.
 *
 * @param userName Tên của người dùng liên quan đến ticket. Mặc định là "Nguyễn Văn A".
 * @param typeTicket Loại ticket. Mặc định là "Báo mất".
 * @param date Ngày của ticket. Mặc định là "1/1/2025".
 * @param isResolvedForTicket Một giá trị boolean cho biết ticket đã được giải quyết hay chưa. Mặc định là false.
 * @param isRevealed Một giá trị boolean cho biết các hành động vuốt có được hiển thị hay không. Mặc định là false.
 * @param onExpandOnly Một hàm lambda sẽ được thực thi khi mục được mở rộng (vuốt).
 * @param onCollapse Một hàm lambda sẽ được thực thi khi mục được thu gọn (thả vuốt hoặc thực hiện hành động).
 * @param onDelete Một hàm lambda sẽ được thực thi khi hành động xóa được kích hoạt.
 *
 * @author Nguyễn Thanh Sang
 * @since 26-05-2025
 */
@Composable
fun TicketCardSwipeable(
    name: String = "Nguyễn Văn A",
    ticketType: String = "Báo mất",
    ticketDate: String = "1/1/2025",
    isResolved: Boolean = false,
    isRevealed: Boolean = false,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onDelete: () -> Unit
) {
    SwipeableItemWithActions(
        isRevealed = isRevealed,
        onExpanded = onExpand,
        onCollapsed = onCollapse,
        actions = {
            Spacer(Modifier.width(8.dp))

            ActionIcon(
                onClick = onDelete,
                backgroundColor = Color(0xFFF44336),
                icon = Icons.Default.Delete
            )
        }
    ) {
        Row(
            modifier = Modifier
                .background(color = Color(0xFFD8E4E8))
                .fillMaxWidth()
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
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    color = Color.Black
                )
                Text(
                    text = ticketDate,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    color = Color.Black
                )
            }

            Text(
                text = if (isResolved) "Đã xử lý" else "Chưa xử lý",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 16.sp,
                color = if (isResolved) Color.Green else Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketCardSwipeablePreview() {
    val tickets = remember {
        mutableStateListOf(
            TicketUi(1, "Nguyễn Văn A", "Báo mất", "1/1/2025", "Làm rơi chìa khóa", false),
            TicketUi(2, "Trần Thị B", "Báo hỏng", "2/2/2025", "Thiết bị không hoạt động", true),
            TicketUi(3, "Lê Văn C", "Yêu cầu hỗ trợ", "3/3/2025", "Cần hỗ trợ lắp đặt", false)
        )
    }

    LazyColumn(modifier = Modifier.padding(8.dp)) {
        itemsIndexed(tickets) { index, ticket ->
            Spacer(modifier = Modifier.height(8.dp))
            TicketCardSwipeable(
                name = ticket.nameUser,
                ticketType = ticket.typeTicket,
                ticketDate = ticket.date,
                isRevealed = tickets[index].isResolved,
                onExpand = {
                    tickets.indices.forEach { i ->
                        tickets[i] = tickets[i].copy(isResolved = i == index)
                    }
                },
                onCollapse = {
                    tickets[index] = ticket.copy(isResolved = false)
                },
                onDelete = {
                    tickets.removeAt(index)
                }
            )
        }
    }
}
