package com.sns.homeconnect_v2.presentation.screen.group.house.space

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.presentation.component.DeviceCardSwipeable
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.LabeledBox
import com.sns.homeconnect_v2.presentation.component.widget.RadialFab
import com.sns.homeconnect_v2.presentation.component.widget.SearchBar
import com.sns.homeconnect_v2.presentation.model.DeviceUi
import com.sns.homeconnect_v2.presentation.model.FabChild

/**
 * Hàm Composable đại diện cho màn hình chi tiết của một không gian/nhóm.
 *
 * Màn hình này hiển thị danh sách các thiết bị trong một nhóm cụ thể. Nó bao gồm một thanh tìm kiếm,
 * một tiêu đề hiển thị số lượng thiết bị và danh sách các thẻ thiết bị có thể vuốt.
 * Một nút hành động nổi (FAB) dạng tròn cung cấp các tùy chọn để chỉnh sửa, xóa hoặc chia sẻ nhóm.
 * Màn hình cũng có một thanh điều hướng ở cuối màn hình.
 *
 * @param modifier Modifier cho composable này. Mặc định là [Modifier].
 * @param navController [NavHostController] để điều hướng trong ứng dụng.
 * @author Sang
 * @since 16/05/2025
 */

@Composable
fun DetailSpaceScreen(modifier: Modifier = Modifier, navController: NavHostController) {
//    val Device = remember {
//        mutableStateListOf(
//            DeviceUi(1, "Gia đình", "bedroom", false, Icons.Default.Group, Color.Blue),
//            DeviceUi(2, "Marketing", "living room", false, Icons.Default.Home, Color.Red),
//            DeviceUi(3, "Kỹ thuật", "kitchen", false, Icons.Default.Group, Color.Green)
//        )
//    }

    val GroupScreen = remember { mutableStateListOf<DeviceUi>() }

    val bottomNavigationItems = listOf(
        "Dashboard" to Pair(Icons.Filled.PieChart, "dashboard"),
        "Devices" to Pair(Icons.Filled.Devices, "devices"),
        "Home" to Pair(Icons.Filled.Home, "home"),
        "Profile" to Pair(Icons.Filled.Person, "profile"),
        "Settings" to Pair(Icons.Filled.Settings, "settings")
    )
    val applicationContext = LocalContext.current
    val isTablet = isTablet(applicationContext)

    // Track the last selected route
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        val floatingActionButtons = listOf(
            FabChild(
                icon = Icons.Default.Edit,
                onClick = { /* TODO: sửa */ },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ),
            FabChild(
                icon = Icons.Default.Delete,
                onClick = { /* TODO: xoá */ },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ),
            FabChild(
                icon = Icons.Default.Share,
                onClick = { /* TODO: share */ },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            )
        )

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type          = "Back",
                    title         = "Settings"
                )
            },
            containerColor = Color.White,
            floatingActionButton = {
                RadialFab(
                    items      = floatingActionButtons,
                    radius     = 120.dp,        // ↑ bán kính ≥ mainSize + miniSize
                    startDeg   = -90f,          // góc bắt đầu –90° (mở thẳng lên)
                    sweepDeg   = -90f,         // quét 90°
                    onParentClick = { /* add new group */ }
                )
            },
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
                        LabeledBox(
                            label = "Thiết bị",
                            value = GroupScreen.size.toString()
                        )
                    }
                }

                if (GroupScreen.isNotEmpty()) {
                    LazyColumn(modifier = modifier
                        .fillMaxSize()
                    ) {
                        itemsIndexed(GroupScreen) { deviceIndex, deviceUi ->
                            Spacer(Modifier.height(8.dp))
                            DeviceCardSwipeable(
                                DeviceName = deviceUi.name,
                                roomName = deviceUi.room,
                                isRevealed = deviceUi.isRevealed,
                                onExpandOnly = {
                                    GroupScreen.indices.forEach { i ->
                                        GroupScreen[i] = GroupScreen[i].copy(isRevealed = i == deviceIndex)
                                    }
                                },
                                onCollapse = {
                                    GroupScreen[deviceIndex] = deviceUi.copy(isRevealed = false)
                                },
                                onDelete = { GroupScreen.removeAt(deviceIndex) },
                                onEdit = { /* TODO */ }
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
                        Text("Không tìm thấy")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "GroupScreen - Phone")
@Composable
fun DetailSpaceScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        DetailSpaceScreen(navController = rememberNavController())
    }
}