package com.sns.homeconnect_v2.presentation.screen.ticket

import IoTHomeConnectAppTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.BottomSheetWithTrigger
import com.sns.homeconnect_v2.presentation.component.FilterSheetContent
import com.sns.homeconnect_v2.presentation.component.TicketCardSwipeable
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.LabeledBox
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.ticket.GetListTicketViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
//    val tickets = remember {
//        mutableStateListOf(
//            TicketUi(1, "Nguyễn Văn A", "Báo mất", "1/1/2025", "Làm rơi chìa khóa", TicketStatus.UNPROCESSED),
//            TicketUi(2, "Trần Thị B", "Báo hỏng", "2/2/2025", "Thiết bị không hoạt động", TicketStatus.PROCESSED),
//            TicketUi(3, "Lê Văn C", "Yêu cầu hỗ trợ", "3/3/2025", "Cần hỗ trợ lắp đặt", TicketStatus.UNPROCESSED)
//        )
//    }

    val ticketViewModel: GetListTicketViewModel = hiltViewModel()
    val tickets= ticketViewModel.tickets.collectAsState().value

    var revealedIndex by remember { mutableIntStateOf(-1) }
    var selectedDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }


// Log để debug
    LaunchedEffect(tickets) {
        tickets.forEachIndexed { index, ticket ->
            println("Ticket $index: userName=${ticket.userName}, status=${ticket.status}")
        }
    }

    val options = listOf("Tất cả", "Đã xử lý", "Chưa xử lý")
    var selectedStatus by remember { mutableStateOf(options[0]) }
    var isSheetVisible by remember { mutableStateOf(false) }

    // Đếm số lượng ticket chưa xem
    val unviewedCount = tickets.count { !it.IsViewed }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Báo mất thiết bị"
                )
            },
            containerColor = Color.White,
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = { MenuBottom(navController) }
        ) { scaffoldPadding ->
            Column(modifier = Modifier.padding(scaffoldPadding)) {
                ColoredCornerBox(cornerRadius = 40.dp) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {}
                }

                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LabeledBox(
                            label = "Hổ trợ chưa xem",
                            value = unviewedCount.toString()
                        )
                        IconButton(onClick = { isSheetVisible = true }) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Open Sheet",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                if (tickets.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        itemsIndexed(tickets) { index, ticket ->
                            Spacer(modifier = Modifier.height(8.dp))
                            TicketCardSwipeable(
                                name = ticket.userName,
                                ticketType = ticket.ticketTypeName,
                                ticketDate = ticket.createdAt,
                                status = ticket.status.toString().lowercase(),
                                isRevealed = revealedIndex == index,
                                onExpand = { revealedIndex = index },
                                onCollapse = { if (revealedIndex == index) revealedIndex = -1 },
                                onDelete = {},
                                onClick = {
                                    // Đánh dấu ticket là đã xem
                                    ticketViewModel.markTicketAsViewed(index)
                                    // Điều hướng đến màn hình chi tiết
                                    navController.navigate(
                                        Screens.DetailTicket.createRoute(ticket.ticketId)
                                    )
                                    println("Navigating to ticket detail with ID: ${ticket.ticketId}")
                                },
                                ticketId = ticket.ticketId
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Không tìm yêu cầu hổ trợ")
                    }
                }

                BottomSheetWithTrigger(
                    isSheetVisible = isSheetVisible,
                    onDismiss = { isSheetVisible = false },
                    sheetContent = {
                        FilterSheetContent(
                            titleContent = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        "Lọc danh sách báo lỗi",
                                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            },
                            dateContent = {
                                Column {
                                    Text(
                                        "Ngày gửi",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        OutlinedTextField(
                                            value = selectedDate,
                                            onValueChange = {},
                                            readOnly = true,
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.CalendarToday,
                                                    contentDescription = "Chọn ngày"
                                                )
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(56.dp),
                                            placeholder = { Text("Chọn ngày") },
                                            singleLine = true,
                                            textStyle = MaterialTheme.typography.bodyLarge,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        Button(
                                            onClick = { showDatePicker = true },
                                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                                containerColor = Color.Transparent
                                            ),
                                            modifier = Modifier
                                                .matchParentSize()
                                                .padding(0.dp),
                                            contentPadding = PaddingValues(0.dp),
                                            elevation = null,
                                            enabled = true
                                        ) {}
                                    }
                                }
                            },
                            statusContent = {
                                Column {
                                    Text(
                                        "Trạng thái xử lý",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    options.forEach { option ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { selectedStatus = option }
                                                .padding(vertical = 6.dp)
                                        ) {
                                            RadioButton(
                                                selected = selectedStatus == option,
                                                onClick = { selectedStatus = option }
                                            )
                                            Text(
                                                text = option,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        }
                                    }
                                }
                            },
                            actionContent = {
                                ActionButtonWithFeedback(
                                    label = "Xác nhận",
                                    style = HCButtonStyle.PRIMARY,
                                    onAction = { _, _ -> isSheetVisible = false },
                                    snackbarViewModel = snackbarViewModel
                                )
                            }
                        )
                    }
                )

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    selectedDate = dateFormat.format(Date(it))
                                }
                                showDatePicker = false
                            }) { Text("OK") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) { Text("Huỷ") }
                        }
                    ) {
                        DatePicker(state = datePickerState)
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