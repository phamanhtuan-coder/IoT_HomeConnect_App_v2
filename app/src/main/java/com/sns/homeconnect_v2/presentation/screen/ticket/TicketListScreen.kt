package com.sns.homeconnect_v2.presentation.screen.ticket

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.TicketCardSwipeable
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.LabeledBox
import com.sns.homeconnect_v2.presentation.model.TicketUi

/**
 * Hàm Composable hiển thị danh sách các yêu cầu hỗ trợ.
 *
 * Màn hình này cho phép người dùng xem và quản lý các yêu cầu hỗ trợ của họ.
 * Mỗi yêu cầu được hiển thị trong một [TicketCardSwipeable] cho phép các hành động như mở rộng,
 * thu gọn và xóa yêu cầu.
 *
 * Màn hình bao gồm:
 * - Một [Header] với nút quay lại và tiêu đề "Báo mất thiết bị".
 * - Một [ColoredCornerBox] và [InvertedCornerHeader] để tạo kiểu cho phần trên cùng.
 * - Một [LabeledBox] hiển thị tổng số yêu cầu hỗ trợ.
 * - Một [LazyColumn] để hiển thị hiệu quả danh sách các mục [TicketCardSwipeable].
 * - Thông báo "Không tìm yêu cầu hổ trợ" được hiển thị nếu không có yêu cầu nào.
 * - Một [MenuBottom] để điều hướng.
 *
 * Danh sách các yêu cầu hỗ trợ được quản lý bởi một `mutableStateListOf` có tên là `supportTickets`.
 * Khi một thẻ yêu cầu được mở rộng, tất cả các thẻ khác sẽ được thu gọn.
 * Việc xóa một yêu cầu sẽ loại bỏ nó khỏi danh sách `supportTickets`.
 *
 * @param navController [NavHostController] được sử dụng để điều hướng.
 * @author Nguyễn Thanh Sang
 * @since 26-05-2025
 */
@Composable
fun TicketListScreen(navController: NavHostController) {
    val supportTickets = remember {
        mutableStateListOf(
            TicketUi(1, "Nguyễn Văn A", "Báo mất", "1/1/2025", "Làm rơi chìa khóa", false),
            TicketUi(2, "Trần Thị B", "Báo hỏng", "2/2/2025", "Thiết bị không hoạt động", true),
            TicketUi(3, "Lê Văn C", "Yêu cầu hỗ trợ", "3/3/2025", "Cần hỗ trợ lắp đặt", false)
        )
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type          = "Back",
                    title         = "Báo mất thiết bị"
                )
            },
            containerColor = Color.White,
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = {
                MenuBottom(navController)
            }
        ) { scaffoldPadding ->
            Column (
                modifier= Modifier.padding(scaffoldPadding)
            ) {
                ColoredCornerBox(
                    cornerRadius = 40.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                    }
                }

                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LabeledBox(
                            label = "Hổ trợ",
                            value = supportTickets.size.toString()
                        )
                    }
                }

                if (supportTickets.isNotEmpty()) {
                    LazyColumn(modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                    ) {
                        itemsIndexed(supportTickets) { ticketIndex, ticket ->
                            Spacer(modifier = Modifier.height(8.dp))
                            TicketCardSwipeable(
                                name = ticket.nameUser,
                                ticketType = ticket.typeTicket,
                                ticketDate = ticket.date,
                                isRevealed = supportTickets[ticketIndex].isResolved,
                                onExpand = {
                                    supportTickets.indices.forEach { ticketIndex ->
                                        supportTickets[ticketIndex] = supportTickets[ticketIndex].copy(isResolved = ticketIndex == ticketIndex)
                                    }
                                },
                                onCollapse = {
                                    supportTickets[ticketIndex] = ticket.copy(isResolved = false)
                                },
                                onDelete = {
                                    supportTickets.removeAt(ticketIndex)
                                }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Không tìm yêu cầu hổ trợ")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "GroupScreen - Phone")
@Composable
fun TicketListScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        TicketListScreen(navController = rememberNavController())
    }
}