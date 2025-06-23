package com.sns.homeconnect_v2.presentation.screen.ticket

import ChatMessageCard
import ChatMessageType
import IoTHomeConnectAppTheme
import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.TicketDetailCard
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.model.ChatMessageUi
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.ticket.GetDetailTicketViewModel
import java.text.SimpleDateFormat
import java.util.Locale

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
fun TicketDetailScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
    ticketId: String? = null // Thêm tham số ticketId nếu cần thiết
) {
    IoTHomeConnectAppTheme {
        // Các biến dữ liệu
        val colorScheme = MaterialTheme.colorScheme

        val ticketDetail: GetDetailTicketViewModel = hiltViewModel()
        val ticketDetailState by ticketDetail.ticketDetail.collectAsState()

        // Gọi API để lấy chi tiết ticket khi ticketId thay đổi
        LaunchedEffect(ticketId) {
            ticketId?.let {
                Log.d("TicketDetailScreen", "Fetching ticket detail for ID: $it")
                ticketDetail.getTicketDetail(it)
            } ?: run {
                Log.w("TicketDetailScreen", "ticketId is null")
                snackbarViewModel.showSnackbar("Không có ticketId")
            }
        }

        // --- Biến chính ---
        val ticketStatus = Status.APPROVED
        val ticketReason = "Báo mất thiết bị"

        // Danh sách tin nhắn (có thể dùng List nếu muốn hiển thị động nhiều chat)
        // Tạo chatMessages từ evidence (logs và images)
        val chatMessages = ticketDetailState?.evidence?.let { evidence ->
            evidence.logs.mapIndexed { index, log ->
                ChatMessageUi(
                    message = log,
                    imageUrl = evidence.images.getOrNull(index),
                    time = ticketDetailState!!.createdAt,
                    type = if (index % 2 == 0) ChatMessageType.SENT else ChatMessageType.RECEIVED,
                    avatarUrl = if (index % 2 == 0) null else "https://randomuser.me/api/portraits/men/1.jpg"
                )
            }
        } ?: emptyList()


//        val buttonLabel = "Đã xem"

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
                    ColoredCornerBox(cornerRadius = 40.dp) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            when {
                                ticketId == null -> {
                                    Text(
                                        text = "Không có ticketId",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }

                                ticketDetailState == null -> {
                                    Text(
                                        text = "Đang tải chi tiết ticket...",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }

                                else -> {
                                    TicketDetailCard(
                                        status = try {
                                            Status.valueOf(
                                                ticketDetailState!!.status?.uppercase() ?: "PENDING"
                                            )
                                        } catch (e: IllegalArgumentException) {
                                            Status.PENDING
                                        },
                                        reason = ticketDetailState!!.ticketTypeName
                                    )
                                }
                            }
                        }
                    }

                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ) {}
//                    Column {
//                        // Hiển thị thêm thông tin chi tiết từ API
//                        ticketDetailState?.let { ticket ->
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(horizontal = 16.dp, vertical = 8.dp)
//                            ) {
//                                Text(
//                                text = "Người tạo: ${ticket.userName}",
//                                style = MaterialTheme.typography.bodyLarge
//                                )
//                                Text(
//                                    text = "Ngày: ${formatDate(ticket.createdAt ?: "N/A")}",
//                                    style = MaterialTheme.typography.bodyLarge
//                                )
//                                Text(
//                                    text = "Mô tả: ${ticket.description}",
//                                    style = MaterialTheme.typography.bodyLarge
//                                )
//                                Text(
//                                    text = "Thiết bị: ${ticket.deviceName}",
//                                    style = MaterialTheme.typography.bodyLarge
//                                )
//
//                                ticket.assignedName?.let {
//                                    Text(
//                                        text = "Người xử lý: ${ticket.assignedName}",
//                                        style = MaterialTheme.typography.bodyLarge
//                                    )
//                                }
//                                ticket.resolvedAt?.let {
//                                    Text(
//                                        text = "Ngày giải quyết: ${ticket.resolvedAt}",
//                                        style = MaterialTheme.typography.bodyLarge
//                                    )
//                                }
////                                ticket.resolveSolution?.let {
////                                    Text(
////                                        text = "Giải pháp: ${ticket.resolveSolution}",
////                                        style = MaterialTheme.typography.bodyLarge
////                                    )
////                                }
//                            }
//                        }

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
//                    Column(
//                        modifier = Modifier.padding(16.dp),
//                    ) {
//                        ActionButtonWithFeedback(
//                            label = buttonLabel,
//                            style = HCButtonStyle.PRIMARY,
//                            onAction = { _, _ -> },
//                            snackbarViewModel = snackbarViewModel
//                        )
//                    }
                }
            }
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(dateString)!!)
    } catch (e: Exception) {
        "N/A"
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "TicketDetailScreen Preview")
@Composable
fun TicketDetailScreenPreview() {
    val navController = rememberNavController()
    TicketDetailScreen(navController = navController)
}