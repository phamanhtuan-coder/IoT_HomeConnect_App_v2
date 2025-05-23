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
import androidx.compose.material.icons.filled.AddHomeWork
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.rememberCoroutineScope
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
 * -----------------------------------------
 * @param navController: Đối tượng điều khiển điều hướng
 * @param type: Loại Header (Home hoặc Back)
 * @param title: Tiêu đề của Header
 * @param username: Tên người dùng
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
                DrawerHeader(username = username)
                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Menu items
                DrawerMenuItem(
                    icon = Icons.Filled.Home,
                    label = "Trang chủ",
                    onClick = {
                        navController.navigate(Screens.Home.route)
                        scope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Filled.Devices,
                    label = "Thiết bị",
                    onClick = {
                        navController.navigate(Screens.ListDevices.route)
                        scope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Filled.AddHomeWork,
                    label = "Quản lý không gian",
                    onClick = {
                        navController.navigate(Screens.HouseManagement.route)
                        scope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Filled.Person,
                    label = "Hồ sơ người dùng",
                    onClick = {
                        navController.navigate(Screens.Profile.route)
                        scope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Filled.Notifications,
                    label = "Thông báo",
                    onClick = {
                        navController.navigate(Screens.AllNotifications.route)
                        scope.launch { drawerState.close() }
                    }
                )

                DrawerMenuItem(
                    icon = Icons.Filled.Settings,
                    label = "Cài đặt",
                    onClick = {
                        navController.navigate(Screens.Settings.route)
                        scope.launch { drawerState.close() }
                    }
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
                        onMenuClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }
                    )
                    "Back" -> BackHeaderWithMenu(
                        navController = navController,
                        title = title,
                        onMenuClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }
                    )
                    "Notification" -> NotificationHeaderWithMenu(
                        navController = navController,
                        title = title,
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
                RoundedIconButton(
                    icon = Icons.Filled.Menu,
                    description = "Menu",
                    onClick = onMenuClick
                )
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
                Row {
                    RoundedIconButton(
                        icon = Icons.Filled.Menu,
                        description = "Menu",
                        onClick = onMenuClick
                    )
                    RoundedIconButton(
                        icon = Icons.Filled.Notifications,
                        description = "Notifications",
                        onClick = {
                            navController.navigate(Screens.AllNotifications.route)
                        }
                    )
                }
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
                    icon = Icons.Filled.Menu,
                    description = "Menu",
                    onClick = onMenuClick
                )
            }
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