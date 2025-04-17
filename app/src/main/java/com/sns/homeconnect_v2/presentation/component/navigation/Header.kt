package com.sns.homeconnect_v2.presentation.component.navigation

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.navigation.Screens
import java.time.LocalTime

/**
 * Lấy lời chào dựa vào thời gian hiện tại
 * -----------------------------------------
 * - Người viết: Phạm Anh Tuấn
 * - Ngày viết: 29/11/2024
 * - Lần cập nhật cuối: 13/12/2024
 * -----------------------------------------
 * @return Lời chào dựa vào thời gian hiện tại
 * ---------------------------------------
 */
@Composable
fun getGreeting(): String {
    val hour = LocalTime.now().hour
    return when (hour) {
        in 6..11 -> "Chào buổi sáng,"
        in 12..14 -> "Chào buổi trưa,"
        in 15..18 -> "Chào buổi chiều,"
        else -> "Chào buổi tối,"
    }
}


/**
 * Header cho màn hình Home hoặc Back
 * -----------------------------------------
 * - Người viết: Phạm Anh Tuấn
 * - Ngày viết: 29/11/2024
 * - Ngày cập nhật gần nhất: 15/12/2024
 * -----------------------------------------
 * @param navController: Đối tượng điều khiển điều hướng
 * @param type: Loại Header (Home hoặc Back)
 * @param title: Tiêu đề của Header
 * @param username: Tên người dùng
 * @return TopAppBar chứa thông tin Header
 * ---------------------------------------
 * Người cập nhật: Phạm Anh Tuấn
 * Ngày cập nhật: 15/12/2024
 * Nội dung cập nhật:
 * - Thêm điều hướng quay lại màn hình trước đó
 *
 */
@Composable
fun Header(
    navController: NavHostController,
    type: String = "Home",
    title: String = "",
    username: String = "Chúc bạn có một ngày tốt lành!",
) {
    when (type) {
        "Home" -> HomeHeader(navController, username)
        "Notification" -> NotificationHeader(navController, title)
        "Back" -> BackHeader(navController, title)
    }
}

/**
 * Header cho màn hình cần nút Back
 * -----------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 29/11/2024
 * Ngày cập nhật gần nhất: 13/12/2024
 * -----------------------------------------
 * @param title: Tiêu đề của Header
 * @return TopAppBar chứa thông tin Header
 * ---------------------------------------
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackHeader(
    navController: NavHostController,
    title: String,

) {
    IoTHomeConnectAppTheme {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                RoundedIconButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    description = "Back",
                    onClick = {
                        // Mimic Facebook-like back navigation
                        val canGoBack = navController.previousBackStackEntry != null
                        if (canGoBack) {
                            // Custom back navigation that doesn't clear the entire stack
                            navController.navigateUp()
                        }
                    }
                )
            },
            actions = {
                RoundedIconButton(
                    icon = Icons.Filled.Notifications,
                    description = "Notifications",
                    onClick = {
                        //Todo: Lấy thông báo từ server
                        navController.navigate(Screens.AllNotifications.route)
                    }
                )
            }
        )
    }
}

/**
 * Header cho màn hình Home
 * -----------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 29/11/2024
 * Ngày cập nhật gần nhất: 13/12/2024
 * -----------------------------------------
 * @param username: Tên người dùng
 * @return TopAppBar chứa thông tin Header
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeHeader(
    navController: NavHostController,
    username: String,
) {
    IoTHomeConnectAppTheme {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            title = {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = getGreeting(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = username,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            },
            actions = {
                RoundedIconButton(
                    icon = Icons.Filled.Notifications,
                    description = "Notifications",
                    onClick = {
                        //Todo: Lấy thông báo từ server
                        navController.navigate(Screens.AllNotifications.route)
                    }
                )
            }
        )
    }
}

@Composable
fun RoundedIconButton(icon: ImageVector, description: String, onClick: () -> Unit) {
    IoTHomeConnectAppTheme {
    IconButton(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .background(MaterialTheme.colorScheme.onPrimary, shape = CircleShape),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = MaterialTheme.colorScheme.primary
        )
    }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationHeader(
    navController: NavHostController,
    title: String
) {
    IoTHomeConnectAppTheme {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                RoundedIconButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    description = "Back",
                    onClick = {
                        // Mimic Facebook-like back navigation
                        val canGoBack = navController.previousBackStackEntry != null
                        if (canGoBack) {
                            // Custom back navigation that doesn't clear the entire stack
                            navController.navigateUp()
                        }
                    }
                )
            },
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun HeaderPhonePreview() {
    Header(navController = rememberNavController(), type = "Home", username = "Alice")
}

@Preview(showBackground = true, widthDp = 720)
@Composable
fun HeaderTabletPreview() {
    Header(
        navController = rememberNavController(),
        type = "Back",
        title = "Settings",
        username = "Bob"
    )
}