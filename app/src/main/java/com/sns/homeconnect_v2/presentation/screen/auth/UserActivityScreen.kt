package com.sns.homeconnect_v2.presentation.screen.auth

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.DeviceSessionListCard
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.viewmodel.user_activity.UserActivityViewModel
import com.sns.homeconnect_v2.presentation.model.DeviceSession
import com.sns.homeconnect_v2.presentation.model.SecurityEvent
import com.sns.homeconnect_v2.presentation.model.SyncHistoryItem
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

/**
 * Hàm Composable cho Màn hình Quản lý Hoạt động Người dùng.
 * Màn hình này hiển thị thông tin liên quan đến hoạt động của người dùng, bao gồm các phiên hoạt động của thiết bị,
 * cảnh báo bảo mật và lịch sử đồng bộ hóa. Nó cho phép người dùng quản lý bảo mật tài khoản
 * và xem lại các hoạt động trước đó.
 *
 * Màn hình được cấu trúc với một thanh ứng dụng trên cùng để điều hướng, một thanh điều hướng dưới cùng,
 * và một khu vực nội dung có thể cuộn. Khu vực nội dung được chia thành các phần cho:
 * - Các phiên hoạt động của thiết bị: Hiển thị danh sách các thiết bị hiện đang đăng nhập vào tài khoản,
 *   với tùy chọn đăng xuất khỏi các phiên cụ thể.
 * - Cảnh báo bảo mật: Hiển thị danh sách các sự kiện bảo mật gần đây, chẳng hạn như đăng nhập bất thường hoặc
 *   kết nối thiết bị mới.
 * - Lịch sử đồng bộ hóa: Liệt kê các hoạt động đồng bộ hóa dữ liệu trước đó trên các thiết bị khác nhau.
 *
 * Màn hình sử dụng một số thành phần composable tùy chỉnh để hiển thị thông tin một cách
 * có cấu trúc và thân thiện với người dùng, bao gồm `DeviceSessionListCard`, `SecurityEventListCard`,
 * và `SyncHistoryItemListCard`. Nó cũng bao gồm một nút hành động để thay đổi mật khẩu tài khoản.
 *
 * Giao diện người dùng được tạo kiểu theo `IoTHomeConnectAppTheme` và sử dụng các thành phần Material Design.
 * Dữ liệu giả hiện đang được sử dụng cho mục đích minh họa.
 *
 * @param navController NavHostController được sử dụng để điều hướng trong ứng dụng.
 * @author Nguyễn Thanh Sang
 * @since 27-05-2025
 */
@Composable
fun UserActivityManagementScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
    viewModel: UserActivityViewModel = hiltViewModel()
) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        val activities by viewModel.activities.collectAsState()

        // Gọi API khi vào màn hình
        LaunchedEffect(Unit) {
            viewModel.loadActivities()
        }
//        val fakeItems = listOf(
//            SyncHistoryItem("Windows - Edge", "16/05/2025 15:40", true),
//            SyncHistoryItem("Android - Chrome", "15/05/2025 10:15", true),
//            SyncHistoryItem("Mac - Safari", "14/05/2025 09:20", false),
//            SyncHistoryItem("iOS - Safari", "13/05/2025 11:05", true),
//            SyncHistoryItem("Linux - Firefox", "12/05/2025 08:00", false)
//        )
//        val fakeEvents = listOf(
//            SecurityEvent("Đăng nhập bất thường", "15/05/2025", true),
//            SecurityEvent("IP lạ", "14/05/2025", true),
//            SecurityEvent("Thiết bị mới", "13/05/2025", false),
//            SecurityEvent("Mật khẩu sai nhiều lần", "12/05/2025", true),
//            SecurityEvent("Trình duyệt mới", "11/05/2025", false)
//        )

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type          = "Back",
                    title         = "Settings"
                )
            },
            containerColor = Color.White,
            bottomBar = {
                MenuBottom(navController = navController)
            }
        ) { paddingValues ->
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    ColoredCornerBox(
                        cornerRadius = 40.dp
                    ) {
                    }

                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ) {

                    }

                    Column (
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DeviceSessionListCard(
                            sessions = activities, // <-- Đã lấy từ server
                            onLogout = { /* Xử lý logout */ }
                        )
                        ActionButtonWithFeedback(
                            label = "Đổi mật khẩu",
                            style = HCButtonStyle.PRIMARY,
                            onAction = { _, _ -> },
                            snackbarViewModel = snackbarViewModel
                        )
//                        Text(
//                            text        = "Cảnh báo bảo mật",
//                            color       = Color.Black,
//                            fontSize    = 20.sp,
//                            fontWeight  = FontWeight.Bold,
//                            letterSpacing = 0.5.sp,
//                        )
//
//                        SecurityEventListCard(
//                            events = fakeEvents
//                        )
//
//                        Text(
//                            text        = "Lịch sử đồng bộ",
//                            color       = Color.Black,
//                            fontSize    = 20.sp,
//                            fontWeight  = FontWeight.Bold,
//                            letterSpacing = 0.5.sp,
//                        )
//
//                        SyncHistoryItemListCard(items = fakeItems)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "CreateGroupScreen Preview - Phone")
@Composable
fun ReportLostDeviceScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        UserActivityManagementScreen(navController = rememberNavController())
    }
}
