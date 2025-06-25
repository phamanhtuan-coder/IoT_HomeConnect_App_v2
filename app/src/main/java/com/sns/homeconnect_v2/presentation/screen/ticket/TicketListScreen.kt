package com.sns.homeconnect_v2.ticket_screen

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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.presentation.component.BottomSheetWithTrigger
import com.sns.homeconnect_v2.presentation.component.FilterSheetContent
import com.sns.homeconnect_v2.presentation.component.TicketCardSwipeable
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.ticket.CancelTicketViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.ticket.GetListTicketViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Hàm Composable hiển thị danh sách các yêu cầu hỗ trợ.
 *
 * Màn hình này cho phép người dùng xem và quản lý các yêu cầu hỗ trợ của họ.
 * Mỗi yêu cầu được hiển thị trong một [TicketCardSwipeable] cho phép các hành động như mở rộng,
 * thu gọn và xóa hiển thị (nhưng không kích hoạt nếu trạng thái là cancelled).
 *
 * @param navController [NavHostController] để điều hướng.
 * @author Nguyễn Thanh Sang
 * @since 26-05-2025
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    val ticketViewModel: GetListTicketViewModel = hiltViewModel()
    val tickets = ticketViewModel.tickets.collectAsState().value
    val cancelTicketViewModel: CancelTicketViewModel = hiltViewModel()

    var revealedIndex by remember { mutableIntStateOf(-1) }
    var selectedDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var ticketToDelete by remember { mutableStateOf<String?>(null) }

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
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        snackbarViewModel.showSnackbar("Đã nhấn nút thêm!")
                        navController.navigate(Screens.CreateTicket.route)
                    },
                    containerColor = colorScheme.primary ?: Color(0xFF6200EE),
                    contentColor = colorScheme.onPrimary ?: Color.White,
                    modifier = Modifier.size(60.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "+",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onPrimary ?: Color.White
                        )
                    }
                }
            },
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
                            label = "Hỗ trợ chưa xem",
                            value = unviewedCount.toString()
                        )
                        Row {
                            IconButton(onClick = {
                                ticketViewModel.fetchTickets()
                                snackbarViewModel.showSnackbar("Đã tải lại danh sách!", SnackbarVariant.SUCCESS)
                            }) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Tải lại danh sách",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            IconButton(onClick = { isSheetVisible = true }) {
                                Icon(
                                    Icons.Default.FilterList,
                                    contentDescription = "Mở bộ lọc",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
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
                                onDelete = {
                                    if (ticket.status.toString().lowercase() != "cancelled") {
                                        ticketToDelete = ticket.ticketId
                                        showDeleteDialog = true
                                    }
                                },
                                onClick = {
                                    ticketViewModel.markTicketAsViewed(index)
                                    navController.navigate(
                                        Screens.DetailTicket.createRoute(ticket.ticketId)
                                    )
                                    println("Navigating to ticket detail with ID: ${ticket.ticketId}")
                                },
                                ticketId = ticket.ticketId,
                                isDeleteEnabled = ticket.status.toString().lowercase() != "cancelled"
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
                        Text("Không tìm thấy yêu cầu hỗ trợ")
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
                            TextButton(onClick = { showDatePicker = false }) { Text("Hủy") }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false; ticketToDelete = null; revealedIndex = -1 },
                        shape = RoundedCornerShape(16.dp),
                        containerColor = colorScheme.surface,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = colorScheme.error,
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        title = {
                            Text(
                                text = "Xác nhận hủy",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = colorScheme.onSurface
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        text = {
                            Text(
                                text = "Bạn có muốn hủy yêu cầu này không?",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = colorScheme.onSurfaceVariant
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        },
                        confirmButton = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        showDeleteDialog = false
                                        ticketToDelete = null
                                        revealedIndex = -1
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF44336), // Màu đỏ
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 4.dp)
                                        .height(48.dp)
                                ) {
                                    Text(
                                        "Hủy",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                                Button(
                                    onClick = {
                                        ticketToDelete?.let { ticketId ->
                                            cancelTicketViewModel.cancelTicket(ticketId)
                                            snackbarViewModel.showSnackbar(
                                                "Đã hủy yêu cầu hỗ trợ",
                                                SnackbarVariant.SUCCESS
                                            )
                                            println("Canceled ticket with ID: $ticketId")
                                            ticketViewModel.fetchTickets()
                                            revealedIndex = -1
                                        }
                                        showDeleteDialog = false
                                        ticketToDelete = null
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4CAF50), // Màu xanh
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 4.dp)
                                        .height(48.dp)
                                ) {
                                    Text(
                                        "Xác nhận",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .widthIn(max = 340.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "TicketListScreen - Phone")
@Composable
fun TicketListScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        TicketListScreen(navController = rememberNavController())
    }
}