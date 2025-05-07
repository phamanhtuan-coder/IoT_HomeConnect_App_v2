package com.sns.homeconnect_v2.presentation.screen.notification

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.data.remote.dto.response.AlertResponse
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.notification.ListNotificationViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.notification.NotificationState
import kotlinx.coroutines.launch


//Todo: Bỏ các object này để làm isTablet
private object NotificationStyle {
    val cardPadding = 8.dp
    val cardElevation = 4.dp
    val cardCornerRadius = 12.dp
    val iconSize = 48.dp
    val spacerSize = 12.dp
    val titleFontSize = 16.sp
    val descriptionFontSize = 14.sp
    val buttonPadding = 16.dp
    val noNoticeIconSize = 100.dp
    val textPadding = 8.dp
}

@Composable
fun NotificationScreen(
    navController: NavHostController,
    viewModel: ListNotificationViewModel = hiltViewModel()
) {
    val notificationState by viewModel.alertListState.collectAsState()
    var notifications by remember { mutableStateOf(emptyList<AlertResponse>()) }
    var errorState by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getAllByUser()
    }

    when (notificationState) {
        is NotificationState.Success -> {
            val successState = notificationState as NotificationState.Success
            notifications = successState.alertList.filter { it.data != null } // Filter out null data
        }
        is NotificationState.Error -> {
            errorState = (notificationState as NotificationState.Error).error
        }
        else -> {
            /* Do nothing */
        }
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = colorScheme.background,
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Danh sách thông báo"
                )
            },
            bottomBar = {
                MenuBottom(navController)
            },
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (notifications.isEmpty()) {
                        EmptyNotificationScreen()
                    } else {
                        NotificationList(
                            notifications,
                            navController,
                            onSearch = { query ->
                                viewModel.viewModelScope.launch {
                                    viewModel.searchNotification(query)
                                }
                            }
                        )
                    }
                    if (errorState.isNotEmpty()) {
                        Text(
                            text = "Error: $errorState",
                            color = colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        )
    }
}
// Màn hình khi không có thông báo
@Composable
fun EmptyNotificationScreen() {
    IoTHomeConnectAppTheme{
        val colorScheme = MaterialTheme.colorScheme
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(NotificationStyle.buttonPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Không có thông báo",
                tint = colorScheme.primary,
                modifier = Modifier.size(NotificationStyle.noNoticeIconSize)
            )
            Spacer(modifier = Modifier.height(NotificationStyle.textPadding))
            Text(
                text = "Không có thông báo ngay bây giờ!",
                color = colorScheme.primary,
                fontSize = NotificationStyle.titleFontSize,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(NotificationStyle.textPadding))
            Text(
                text = "Hãy quay trở lại sau nhé!",
                color = colorScheme.primary,
                fontSize = NotificationStyle.descriptionFontSize
            )

        }
    }
}

// Danh sách các thông báo
@Composable
fun NotificationList(
    notifications: List<AlertResponse>, navController: NavHostController,
    onSearch: (String) -> Unit
) {

    val searchState = remember { mutableStateOf("") }
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Column(
            modifier = Modifier
                .fillMaxSize(), // Chiếm toàn bộ kích thước của màn hình
//                    .padding(bottom = layoutConfig.outerPadding) // Padding linh hoạt
            verticalArrangement = Arrangement.Top, // Sắp xếp các item theo chiều dọc, bắt đầu từ trên xuống.
            horizontalAlignment = Alignment.CenterHorizontally // Căn chỉnh các item theo chiều ngang vào giữa.
        ) {
            // Box lớn chứa phần tiêu đề và các thành phần bên trong
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight() // Chiều cao vừa đủ với nội dung bên trong
                    .background(color = colorScheme.background)
            ) {
                // Cột chứa các phần tử con
                Column {
                    // Hộp màu xanh dương bo tròn góc dưới bên trái
                    Box(
                        modifier = Modifier
                            .fillMaxWidth() // Chiếm toàn bộ chiều rộng
                            .wrapContentHeight() // Chiều cao vừa đủ với nội dung
                            .background(
                                color = colorScheme.primary,
                                shape = RoundedCornerShape(bottomStart = 40.dp)
                            )
                    ) {
                        // Nội dung bên dưới

                        // Cột chứa văn bản tiêu đề và các TextField
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(), // Chiếm toàn bộ chiều rộng
                            horizontalAlignment = Alignment.CenterHorizontally // Căn giữa các phần tử con theo chiều ngang
                        ) {
                            TextField(
                                value = searchState.value,
                                onValueChange = { query ->
                                    searchState.value = query
                                    onSearch(query)

                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search Icon",
                                        tint = colorScheme.onSecondary
                                    )
                                },
                                placeholder = {
                                    Text(text = "Tìm kiếm ....", color = Color.Gray)
                                },
                                singleLine = true,
                                textStyle = TextStyle(fontSize = 16.sp),
                                modifier = Modifier
                                    .width(500.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .padding(12.dp),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = colorScheme.onPrimary,
                                    focusedContainerColor = colorScheme.onPrimary,
                                    unfocusedTextColor = colorScheme.onSecondary,
                                    focusedTextColor = colorScheme.onSecondary
                                )
                            )
                            Spacer(modifier = Modifier.heightIn(16.dp))
                        }

                    }
                    // Box chứa góc lõm màu xám
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight() // Chiều cao linh hoạt theo LayoutConfig
                    ) {
                        // Box màu vàng nhỏ nằm trên góc phải
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.TopEnd)
                                .background(color = colorScheme.primary)
                                .zIndex(1f)  // Z-index thấp hơn
                        )

                        // Box màu xám bo tròn góc lõm trên cùng bên phải
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .background(
                                    color = colorScheme.background,
                                    shape = RoundedCornerShape(topEndPercent = 50)
                                )
                                .zIndex(2f), // Z-index cao hơn
                            contentAlignment = Alignment.Center // Căn Row vào giữa Box
                        ) {

                        }
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Kiểm tra và truyền đúng danh sách
                items(notifications) { notification ->
                    NotificationCard(
                        notification,
                        navController = navController
                    ) // Hiển thị từng Card
                }
            }
        }
    }
}

// Card cho từng thông báo
@Composable
fun NotificationCard(notification: AlertResponse, navController: NavHostController) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        // Check if data is available
        notification.data?.let { alert ->
            Card(
                modifier = Modifier
                    .width(500.dp)
                    .padding(NotificationStyle.cardPadding)
                    .clickable {
                        navController.navigate(Screens.NotificationDetail.route + "?id=${alert.id}")
                    },
                elevation = CardDefaults.cardElevation(NotificationStyle.cardElevation),
                shape = RoundedCornerShape(NotificationStyle.cardCornerRadius),
                colors = CardDefaults.cardColors(colorScheme.primary)
            ) {
                Row(
                    modifier = Modifier
                        .padding(NotificationStyle.spacerSize)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon thông báo
                    Box(
                        modifier = Modifier
                            .size(NotificationStyle.iconSize)
                            .background(colorScheme.onPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notification Icon",
                            tint = colorScheme.primary,
                        )
                    }
                    Spacer(modifier = Modifier.width(NotificationStyle.spacerSize))

                    // Nội dung thông báo
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Device ${alert.deviceId}", // Use deviceId instead of device.Name
                            fontSize = NotificationStyle.titleFontSize,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = alert.message,
                            fontSize = NotificationStyle.descriptionFontSize,
                            color = colorScheme.onPrimary,
                        )
                    }

                    // Icon trạng thái đã đọc
                    if (alert.status) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Đã đọc",
                            tint = colorScheme.onPrimary,
                        )
                    }
                }
            }
        } ?: run {
            // Handle case where data is null (e.g., API error)
            Card(
                modifier = Modifier
                    .width(500.dp)
                    .padding(NotificationStyle.cardPadding),
                elevation = CardDefaults.cardElevation(NotificationStyle.cardElevation),
                shape = RoundedCornerShape(NotificationStyle.cardCornerRadius),
                colors = CardDefaults.cardColors(colorScheme.primary)
            ) {
                Row(
                    modifier = Modifier
                        .padding(NotificationStyle.spacerSize)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.error ?: "No notification data",
                        fontSize = NotificationStyle.descriptionFontSize,
                        color = colorScheme.error
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewNotification() {
    NotificationScreen(
        rememberNavController()
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEmptyNotification() {
    NotificationScreen(
        rememberNavController()
    )
}