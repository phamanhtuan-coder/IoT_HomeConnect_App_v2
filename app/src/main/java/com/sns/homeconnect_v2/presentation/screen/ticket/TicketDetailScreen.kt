package com.sns.homeconnect_v2.presentation.screen.ticket

import ChatMessageCard
import ChatMessageType
import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.TicketDetailCard
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.model.ChatMessageUi

/**
 * Composable function hiển thị màn hình chi tiết yêu cầu.
 *
 * Màn hình này hiển thị chi tiết của một yêu cầu cụ thể, bao gồm trạng thái, lý do
 * và lịch sử trò chuyện liên quan đến yêu cầu. Nó cũng cung cấp một nút hành động
 * để người dùng xác nhận yêu cầu.
 *
 * @param navController NavHostController để điều hướng giữa các màn hình.
 *
 * @author Nguyễn Thanh Sang
 * @since 26-05-2025
 */
@Composable
fun TicketDetailScreen(navController: NavHostController) {
    IoTHomeConnectAppTheme {
        // Các biến dữ liệu
        val colorScheme = MaterialTheme.colorScheme

        // --- Biến chính ---
        val ticketStatus = Status.APPROVED
        val ticketReason = "Báo mất thiết bị"

        // Danh sách tin nhắn (có thể dùng List nếu muốn hiển thị động nhiều chat)
        val chatMessages = listOf(
            ChatMessageUi(
                message = "Đây là ảnh thiết bị bị hỏng.",
                imageUrl = "https://picsum.photos/id/1015/200/150",
                time = "26/05/2025 09:02",
                type = ChatMessageType.SENT
            ),
            ChatMessageUi(
                message = "Mình đã kiểm tra, thiết bị hoạt động bình thường nhé.",
                time = "26/05/2025 09:03",
                avatarUrl = "https://randomuser.me/api/portraits/men/1.jpg",
                type = ChatMessageType.RECEIVED
            )
        )

        val buttonLabel = "Đã xem"

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Chi tiết yêu cầu"
                )
            },
            containerColor = Color.White,
            bottomBar = {
                MenuBottom(navController = navController)
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    ColoredCornerBox(
                        cornerRadius = 40.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            TicketDetailCard(
                                status = ticketStatus,
                                reason = ticketReason
                            )
                        }
                    }

                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ) {}

                    // Hiển thị danh sách chat (nếu nhiều message)
                    chatMessages.forEach { chat ->
                        ChatMessageCard(
                            message = chat.message,
                            imageUrl = chat.imageUrl,
                            time = chat.time,
                            avatarUrl = chat.avatarUrl,
                            type = chat.type
                        )
                    }

                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        ActionButtonWithFeedback(
                            label = buttonLabel,
                            style = HCButtonStyle.PRIMARY,
                            onAction = { _, _ -> }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "TicketDetailScreen Preview")
@Composable
fun TicketDetailScreenPreview() {
    val navController = rememberNavController()
    TicketDetailScreen(navController = navController)
}