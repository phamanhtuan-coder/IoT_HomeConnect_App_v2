package com.sns.homeconnect_v2.presentation.component.navigation

import IoTHomeConnectAppTheme
import android.R.attr.start
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
 * - Ngày cập nhật gần nhất: 23/05/2025
 * -----------------------------------------
 * @param navController: Đối tượng điều khiển điều hướng
 * @param type: Loại Header (Home hoặc Back)
 * @param title: Tiêu đề của Header
 * @param username: Tên người dùng
 * @param showDrawerButton: Hiển thị nút drawer menu
 * @param onDrawerClick: Xử lý sự kiện khi người dùng nhấn vào nút drawer
 * @return TopAppBar chứa thông tin Header
 * ---------------------------------------
 */
@Composable
fun Header(
    navController: NavHostController,
    type: String = "Home",
    title: String = "",
    username: String = "Chúc bạn có một ngày tốt lành!",
    showDrawerButton: Boolean = type == "Home",
    onDrawerClick: () -> Unit = {}
) {
    when (type) {
        "Home" -> HomeHeader(navController, username, showDrawerButton, onDrawerClick)
        "Notification" -> NotificationHeader(navController, title, showDrawerButton, onDrawerClick)
        "Back" -> BackHeader(navController, title, showDrawerButton, onDrawerClick)
    }
}

/**
 * Header cho màn hình cần nút Back
 * -----------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 29/11/2024
 * Ngày cập nhật gần nhất: 23/05/2025
 * -----------------------------------------
 * @param navController: Đối tượng điều khiển điều hướng
 * @param title: Tiêu đề của Header
 * @param showDrawerButton: Hiển thị nút drawer menu
 * @param onDrawerClick: Xử lý sự kiện khi người dùng nhấn vào nút drawer
 * @return TopAppBar chứa thông tin Header
 * ---------------------------------------
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackHeader(
    navController: NavHostController,
    title: String,
    showDrawerButton: Boolean = false,
    onDrawerClick: () -> Unit = {}
) {
    IoTHomeConnectAppTheme {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = MaterialTheme.colorScheme.primary
                    containerColor = Color(0xFF1976D2), // Xanh dương đậm
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
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
                Row(verticalAlignment = Alignment.CenterVertically) {
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

                    if (showDrawerButton) {
                        Spacer(modifier = Modifier.width(10.dp))
                        RoundedIconButton(
                            icon = Icons.Filled.Menu,
                            description = "Menu",
                            onClick = onDrawerClick
                        )
                    }
                }
            },
            actions = {
                RoundedIconButton(
                    icon = Icons.Filled.Notifications,
                    description = "Notifications",
                    onClick = {
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
 * Ngày cập nhật gần nhất: 23/05/2025
 * -----------------------------------------
 * @param navController: Đối tượng điều khiển điều hướng
 * @param username: Tên người dùng
 * @param showDrawerButton: Hiển thị nút drawer menu
 * @param onDrawerClick: Xử lý sự kiện khi người dùng nhấn vào nút drawer
 * @return TopAppBar chứa thông tin Header
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeHeader(
    navController: NavHostController,
    username: String,
    showDrawerButton: Boolean = true,
    onDrawerClick: () -> Unit = {}
) {
    IoTHomeConnectAppTheme {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                //containerColor = MaterialTheme.colorScheme.primary,
                containerColor = Color(0xFF1976D2), // Xanh dương đậm
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            ),
            title = {
                Spacer(modifier = Modifier.width(8.dp)) // Giảm khoảng cách giữa biểu tượng và tiêu đề
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp) // Giảm padding ngang
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
            navigationIcon = {
                if (showDrawerButton) {
                    RoundedIconButton(
                        icon = Icons.Filled.Menu,
                        description = "Menu",
                        onClick = onDrawerClick
                    )
                }
            },
            actions = {
                RoundedIconButton(
                    icon = Icons.Filled.Notifications,
                    description = "Notifications",
                    onClick = {
                        navController.navigate(Screens.AllNotifications.route)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp)) // Giảm khoảng cách giữa các nút
            }
        )
    }
}

@Composable
fun RoundedIconButton(icon: ImageVector, description: String, onClick: () -> Unit) {
    Spacer(modifier = Modifier.width(8.dp))
    IconButton(
        modifier = Modifier
            .padding(horizontal = 10.dp) // Giảm padding
            .size(36.dp) // Giảm kích thước nút
            .shadow(4.dp, CircleShape, clip = false) // Thêm bóng
            .clip(CircleShape)
            .background(Color(0xFFE3F2FD)), // Xanh nhạt, đồng bộ với FeatureButton
        onClick = onClick
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = Color(0xFF1976D2), // Xanh dương đậm cho biểu tượng
            modifier = Modifier.size(20.dp) // Giảm kích thước biểu tượng
        )
    }
}

/**
 * Header cho màn hình Notification
 * -----------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 29/11/2024
 * Ngày cập nhật gần nhất: 23/05/2025
 * -----------------------------------------
 * @param navController: Đối tượng điều khiển điều hướng
 * @param title: Tiêu đề của Header
 * @param showDrawerButton: Hiển thị nút drawer menu
 * @param onDrawerClick: Xử lý sự kiện khi người dùng nhấn vào nút drawer
 * @return TopAppBar chứa thông tin Header
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationHeader(
    navController: NavHostController,
    title: String,
    showDrawerButton: Boolean = false,
    onDrawerClick: () -> Unit = {}
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
                Row(verticalAlignment = Alignment.CenterVertically) {
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

                    if (showDrawerButton) {
                        Spacer(modifier = Modifier.width(8.dp))
                        RoundedIconButton(
                            icon = Icons.Filled.Menu,
                            description = "Menu",
                            onClick = onDrawerClick
                        )
                    }
                }
            },
            actions = {}
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

@Preview(showBackground = true, widthDp = 360)
@Composable
fun HeaderWithDrawerButtonPreview() {
    Header(
        navController = rememberNavController(),
        type = "Back",
        title = "Settings",
        username = "Bob",
        showDrawerButton = true,
        onDrawerClick = {}
    )
}