package com.sns.homeconnect_v2.presentation.component.navigation

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddHomeWork
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.navigation.Screens
import kotlinx.coroutines.launch

/**
 * Header có MenuDrawer
 * -----------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 23/05/2025
 * Lần cập nhật cuối: 23/05/2025
 * -----------------------------------------
 * @param navController: Đối tượng điều khiển điều hướng
 * @param type: Loại Header (Home hoặc Back)
 * @param title: Tiêu đề của Header
 * @param username: Tên người dùng
 * @param showDrawerButton: Hiển thị nút drawer menu. Mặc định là hiển thị với Header Home và ẩn với Header Back
 * @return TopAppBar chứa thông tin Header và MenuDrawer
 * ---------------------------------------
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderWithDrawer(
    navController: NavHostController,
    type: String = "Home",
    title: String = "",
    username: String = "Chúc bạn có một ngày tốt lành!",
    showDrawerButton: Boolean = type == "Home",  // Default: only show drawer button on Home headers
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = MaterialTheme.colorScheme.background,
                drawerContentColor = MaterialTheme.colorScheme.onBackground,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { scope.launch { drawerState.close() } },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Đóng menu",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                DrawerHeader(username = username)

                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Menu items grouped
                val homeGroup = listOf(
                    Triple(Icons.Filled.Home, "Trang chủ", Screens.Home.route),
                    Triple(Icons.Filled.PieChart, "Dashboard", Screens.Dashboard.route)
                )

                DrawerMenuGroup(
                    title = "Trang chủ",
                    icon = Icons.Filled.Home,
                    items = homeGroup,
                    navController = navController,
                    onCloseDrawer = { scope.launch { drawerState.close() } }
                )

                val devicesGroup = listOf(
                    Triple(Icons.Filled.Devices, "Danh sách thiết bị", Screens.ListDevices.route),
                    Triple(Icons.Filled.Add, "Thêm thiết bị", Screens.AddDevice.route),
                    Triple(Icons.Filled.History, "Lịch sử hoạt động", Screens.ActivityHistory.createRoute(0))
                )

                DrawerMenuGroup(
                    title = "Quản lý thiết bị",
                    icon = Icons.Filled.Devices,
                    items = devicesGroup,
                    navController = navController,
                    onCloseDrawer = { scope.launch { drawerState.close() } }
                )

                val spaceGroup = listOf(
                    Triple(Icons.Filled.AddHomeWork, "Quản lý không gian", Screens.HouseManagement.route),
                    Triple(Icons.Filled.Home, "Nhóm thiết bị", Screens.Groups.route)
                )

                DrawerMenuGroup(
                    title = "Không gian và nhóm",
                    icon = Icons.Filled.AddHomeWork,
                    items = spaceGroup,
                    navController = navController,
                    onCloseDrawer = { scope.launch { drawerState.close() } }
                )

                val accountGroup = listOf(
                    Triple(Icons.Filled.Person, "Hồ sơ người dùng", Screens.Profile.route),
                    Triple(Icons.Filled.Notifications, "Thông báo", Screens.AllNotifications.route),
                    Triple(Icons.Filled.Settings, "Cài đặt", Screens.Settings.route)
                )

                DrawerMenuGroup(
                    title = "Tài khoản",
                    icon = Icons.Filled.Person,
                    items = accountGroup,
                    navController = navController,
                    onCloseDrawer = { scope.launch { drawerState.close() } }
                )

                Spacer(modifier = Modifier.weight(1f))

                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                DrawerMenuItem(
                    icon = Icons.Filled.Help,
                    label = "Trợ giúp",
                    onClick = {
                        // TODO: Navigate to help screen
                        scope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Filled.Info,
                    label = "Giới thiệu",
                    onClick = {
                        // TODO: Navigate to about screen
                        scope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Filled.Logout,
                    label = "Đăng xuất",
                    onClick = {
                        // TODO: Implement logout functionality
                        navController.navigate(Screens.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                        scope.launch { drawerState.close() }
                    },
                    tint = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        content = {
            Column {
                when (type) {
                    "Home" -> HomeHeaderWithMenu(
                        navController = navController,
                        username = username,
                        showDrawerButton = showDrawerButton,
                        onMenuClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }
                    )
                    "Back" -> BackHeaderWithMenu(
                        navController = navController,
                        title = title,
                        showDrawerButton = showDrawerButton,
                        onMenuClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }
                    )
                    "Notification" -> NotificationHeaderWithMenu(
                        navController = navController,
                        title = title,
                        showDrawerButton = showDrawerButton,
                        onMenuClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }
                    )
                }
                content()
            }
        }
    )
}

@Composable
fun DrawerHeader(username: String) {
    IoTHomeConnectAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = getGreeting(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = username,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Nhóm menu trong drawer
 */
@Composable
fun DrawerMenuGroup(
    title: String,
    icon: ImageVector,
    items: List<Triple<ImageVector, String, String>>,
    navController: NavHostController,
    onCloseDrawer: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) "Thu gọn" else "Mở rộng",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        if (expanded) {
            items.forEach { (itemIcon, label, route) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(route)
                            onCloseDrawer()
                        }
                        .padding(start = 56.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = itemIcon,
                        contentDescription = label,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Chuyển tới",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onBackground
) {
    IoTHomeConnectAppTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                color = tint,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * HomeHeader with MenuDrawer
 * -----------------------------------------
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeHeaderWithMenu(
    navController: NavHostController,
    username: String,
    showDrawerButton: Boolean = true,
    onMenuClick: () -> Unit
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
            navigationIcon = {
                if (showDrawerButton) {
                    RoundedIconButton(
                        icon = Icons.Filled.Menu,
                        description = "Menu",
                        onClick = onMenuClick
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
            }
        )
    }
}

/**
 * BackHeader with MenuDrawer
 * -----------------------------------------
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackHeaderWithMenu(
    navController: NavHostController,
    title: String,
    showDrawerButton: Boolean = false,
    onMenuClick: () -> Unit
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
                            val canGoBack = navController.previousBackStackEntry != null
                            if (canGoBack) {
                                navController.navigateUp()
                            }
                        }
                    )

                    if (showDrawerButton) {
                        Spacer(modifier = Modifier.width(8.dp))
                        RoundedIconButton(
                            icon = Icons.Filled.Menu,
                            description = "Menu",
                            onClick = onMenuClick
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
 * NotificationHeader with MenuDrawer
 * -----------------------------------------
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationHeaderWithMenu(
    navController: NavHostController,
    title: String,
    showDrawerButton: Boolean = false,
    onMenuClick: () -> Unit
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
                            val canGoBack = navController.previousBackStackEntry != null
                            if (canGoBack) {
                                navController.navigateUp()
                            }
                        }
                    )

                    if (showDrawerButton) {
                        Spacer(modifier = Modifier.width(8.dp))
                        RoundedIconButton(
                            icon = Icons.Filled.Menu,
                            description = "Menu",
                            onClick = onMenuClick
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
fun HeaderWithDrawerPreview() {
    IoTHomeConnectAppTheme {
        HeaderWithDrawer(
            navController = rememberNavController(),
            type = "Home",
            username = "Alice Nguyen",
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Content goes here")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun BackHeaderWithDrawerPreview() {
    IoTHomeConnectAppTheme {
        HeaderWithDrawer(
            navController = rememberNavController(),
            type = "Back",
            title = "Chi tiết thiết bị",
            username = "Bob Nguyen",
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Content goes here")
            }
        }
    }
}