package com.sns.homeconnect_v2.presentation.screen.group.user

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuItem
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.GenericDropdown
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.SearchBar
import com.sns.homeconnect_v2.presentation.component.SimpleUserCard
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Hàm Composable đại diện cho màn hình "Thêm người dùng".
 * Màn hình này cho phép người dùng tìm kiếm và thêm người dùng mới vào một nhóm,
 * gán vai trò cho họ và điều hướng đến các phần khác của ứng dụng.
 *
 * Màn hình bao gồm:
 * - Thanh ứng dụng trên cùng với nút quay lại và tiêu đề.
 * - Thanh tìm kiếm để tìm người dùng.
 * - Một phần để hiển thị người dùng đang được thêm (hiện tại là một trình giữ chỗ).
 * - Menu thả xuống để chọn vai trò cho người dùng mới (Chủ sở hữu, Phó, Quản trị viên, Thành viên).
 * - Nút "Thêm" để xác nhận việc thêm người dùng.
 * - Thanh điều hướng dưới cùng để điều hướng đến các màn hình khác như Bảng điều khiển, Thiết bị, Trang chủ, Hồ sơ và Cài đặt.
 *
 * @param navController [NavHostController] được sử dụng để điều hướng trong ứng dụng.
 * @author Nguyễn Thanh Sang
 * @since 19-05-2025
 */

@Composable
fun AddUserScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var current by remember { mutableStateOf<String?>(null) }
    val list = listOf("Owner", "Vice", "Admin", "Member")
    val users = listOf(
        "Trần Thị B" to "https://i.pravatar.cc/150?img=8"
    )

    val items = listOf(
        "Dashboard" to Pair(Icons.Filled.PieChart, "dashboard"),
        "Devices" to Pair(Icons.Filled.Devices, "devices"),
        "Home" to Pair(Icons.Filled.Home, "home"),
        "Profile" to Pair(Icons.Filled.Person, "profile"),
        "Settings" to Pair(Icons.Filled.Settings, "settings")
    )
    val context = LocalContext.current
    val isTablet = isTablet(context)

    // Track the last selected route
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Settings"
                )
            },
            containerColor = Color.White,
            bottomBar = {
                BottomAppBar(
                    tonalElevation = 4.dp,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.height(120.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Top
                    ) {
                        items.forEach { item ->
                            val isSelected = currentRoute == item.second.second
                            MenuItem(
                                text = item.first,
                                icon = item.second.first,
                                isSelected = isSelected,
                                onClick = {
                                    navController.navigate(item.second.second) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                isTablet = isTablet,
                            )
                        }
                    }
                }
            }
        ) { inner ->
            Column(
                modifier = Modifier.padding(inner)
            ) {
                ColoredCornerBox(
                    cornerRadius = 40.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        SearchBar(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onSearch = { query ->
                                /* TODO: điều kiện search */
                            }
                        )
                    }
                }

                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    users.forEach { (name, avatar) ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFD8E4E8), // cùng màu như trước
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SimpleUserCard(
                                userName = name,
                                avatarUrl = avatar,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    GenericDropdown(
                        items = list,
                        selectedItem = current,
                        onItemSelected = { current = it },
                        isTablet = isTablet,
                        leadingIcon = Icons.Default.Room
                    )
                    Spacer(Modifier.height(8.dp))
                    ActionButtonWithFeedback(
                        label = "Thêm",
                        style = HCButtonStyle.PRIMARY,
                        onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } },
                        snackbarViewModel = snackbarViewModel
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "CreateUserScreen - Phone")
@Composable
fun CreateUserScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        AddUserScreen(navController = rememberNavController())
    }
}