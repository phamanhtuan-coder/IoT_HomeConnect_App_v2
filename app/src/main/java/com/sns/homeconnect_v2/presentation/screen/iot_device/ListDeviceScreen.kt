package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.DeviceCardSwipeable
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.CustomTabRow
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.LabeledBox
import com.sns.homeconnect_v2.presentation.component.widget.SearchBar
import com.sns.homeconnect_v2.presentation.model.DeviceUi
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.ListOfUserOwnedDevicesState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.ListOfUserOwnedDevicesViewModel

/** Giao diện màn hình Device Screen
 * -----------------------------------------
 * Người viết: Nguyễn Thanh Sang
 * Ngày viết: 3/12/2024
 * Lần cập nhật cuối: 9/12/2024
 * -----------------------------------------
// * @param navController Đối tượng điều khiển điều hướng.
 *
 * @return Scaffold chứa giao diện màn hình Danh sách Thiết bị
 *
 * ---------------------------------------
 * Nội dung cập nhật: Sủa lại phần giao diện
 * ---------------------------
 * Lần cập nhật 30/12/2024
 * Người cập nhật: Phạm Anh Tuấn
 * ---------------------------
 * Nội dung cập nhật: Sửa lại phần giao diện
 * ---------------------------
 * Lần cập nhât: 31/12/24
 * Người cập nhật: Nguyễn Thanh Sang
 */

@Composable
fun ListDeviceScreen(
    navController: NavHostController = rememberNavController(),
    listOfUserOwnedDevicesViewModel: ListOfUserOwnedDevicesViewModel = hiltViewModel(),
//    deviceViewModel: DeviceViewModel = hiltViewModel(),
//    sharedViewModel: SharedViewModel = hiltViewModel(),
) {
    val ownedDevicesState by listOfUserOwnedDevicesViewModel.listOfUserOwnedDevicesState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val deviceOwnershipTabs = listOf("Sở hữu", "Chia sẽ")

    LaunchedEffect(Unit) {
        // Lấy danh sách thiết bị đã sở hữu
        listOfUserOwnedDevicesViewModel.getListOfUserOwnedDevices()
    }

//    val ownedDevices = remember {
//        mutableStateListOf(
//            DeviceUi(1, "Gia đình", "bedroom", false, Icons.Default.Group, Color.Blue),
//            DeviceUi(2, "Marketing", "living room", false, Icons.Default.Home, Color.Red),
//            DeviceUi(3, "Kỹ thuật", "kitchen", false, Icons.Default.Group, Color.Green),
//            DeviceUi(4, "Tài chính", "office", false, Icons.Default.Info, Color.Magenta),
//            DeviceUi(5, "Quản lý", "garage", false, Icons.Default.Settings, Color.Gray),
//            DeviceUi(6, "Sản xuất", "bathroom", false, Icons.Default.Star, Color.Cyan),
//            DeviceUi(7, "Kế toán", "dining room", false, Icons.Default.Home, Color.Yellow),
//            DeviceUi(8, "Nhân sự", "bedroom", false, Icons.Default.Group, Color.LightGray),
//            DeviceUi(9, "Kho vận", "living room", false, Icons.Default.Info, Color.Blue),
//            DeviceUi(10, "Kinh doanh", "office", false, Icons.Default.Star, Color.Red),
//            DeviceUi(11, "Chăm sóc KH", "kitchen", false, Icons.Default.Group, Color.Green),
//            DeviceUi(12, "Thiết kế", "studio", false, Icons.Default.Settings, Color.Magenta),
//            DeviceUi(13, "Ban giám đốc", "bedroom", false, Icons.Default.Group, Color.Cyan),
//            DeviceUi(14, "PR", "conference", false, Icons.Default.Home, Color.Blue),
//            DeviceUi(15, "Đối ngoại", "office", false, Icons.Default.Info, Color.Red),
//            DeviceUi(16, "CNTT", "server room", false, Icons.Default.Settings, Color.Gray)
//        )
//    }

    val sharedDevices = remember {
        mutableStateListOf(
            DeviceUi(101, "Gia đình", "living room", false, Icons.Default.Home, Color.Blue),
            DeviceUi(102, "Marketing", "bedroom", false, Icons.Default.Group, Color.Red),
            DeviceUi(103, "Kỹ thuật", "garage", false, Icons.Default.Settings, Color.Green),
            DeviceUi(104, "Tài chính", "office", false, Icons.Default.Info, Color.Magenta),
            DeviceUi(105, "Quản lý", "kitchen", false, Icons.Default.Star, Color.Gray),
            DeviceUi(106, "Sản xuất", "bathroom", false, Icons.Default.Home, Color.Cyan),
            DeviceUi(107, "Kế toán", "studio", false, Icons.Default.Group, Color.Yellow),
            DeviceUi(108, "Nhân sự", "conference", false, Icons.Default.Settings, Color.LightGray),
            DeviceUi(109, "Kho vận", "bedroom", false, Icons.Default.Group, Color.Blue),
            DeviceUi(110, "Kinh doanh", "living room", false, Icons.Default.Star, Color.Red),
            DeviceUi(111, "Thiết kế", "office", false, Icons.Default.Group, Color.Green),
            DeviceUi(112, "Ban giám đốc", "server room", false, Icons.Default.Settings, Color.Magenta),
            DeviceUi(113, "Đối ngoại", "garage", false, Icons.Default.Home, Color.Cyan)
        )
    }

//    var selectedTabIndex by remember { mutableIntStateOf(0) }
//    var spaces by remember { mutableStateOf<List<SpaceResponse>>(emptyList()) } // Lắng nghe danh sách thiết bị
//    val spacesListState by deviceViewModel.spacesListState.collectAsState()
//    when (spacesListState) {
//        is SpaceState.Error -> {
//            Log.d("Error", (spacesListState as SpaceState.Error).error)
//        }
//
//        is SpaceState.Success -> {
//            spaces = (spacesListState as SpaceState.Success).spacesList
//            Log.d("List Device", (spacesListState as SpaceState.Success).spacesList.toString())
//            if (selectedTabIndex == 0) {
//                deviceViewModel.getDevicesBySpaceId(spaces.first().SpaceID)
//            }
//        }
//
//        else -> {/* Do nothing */
//        }
//    }
//    var devices by remember { mutableStateOf<List<DeviceResponse>>(emptyList()) } // Lắng nghe danh sách thiết bị
//    val deviceListState by deviceViewModel.deviceListState.collectAsState()
//    when (deviceListState) {
//        is DeviceState.Error -> {
//            Log.d("Error", (deviceListState as DeviceState.Error).error)
//        }
//
//        is DeviceState.Success -> {
//            devices = (deviceListState as DeviceState.Success).deviceList
//            Log.d("List Device", (deviceListState as DeviceState.Success).deviceList.toString())
//        }
//
//        else -> {/* Do nothing */
//        }
//    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        Scaffold(
            containerColor = colorScheme.background,
            modifier = Modifier.fillMaxSize(),
            topBar = {
                /*
            * Hiển thị Header
             */
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Danh sách thiết bị"
                )

            },
            bottomBar = {
                /*
            * Hiển thị Thanh Menu dưới cùng
             */
                MenuBottom(navController)
            },
            floatingActionButton = {

            },
            floatingActionButtonPosition = FabPosition.End,
            content = { contentPadding ->
                Column (
                    modifier= Modifier
                        .padding(contentPadding)
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
                                onSearch = { searchQuery ->
                                    /* TODO: điều kiện search */
                                }
                            )
                        }
                    }

                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ) {
                        CustomTabRow(
                            tabs = deviceOwnershipTabs,
                            selectedTabIndex = selectedTabIndex,
                            onTabSelected = { selectedTabIndex = it },
                        )
                    }

                    when(selectedTabIndex) {
                        0 -> {
                            val ownedDevices = when (ownedDevicesState) {
                                is ListOfUserOwnedDevicesState.Success -> (ownedDevicesState as ListOfUserOwnedDevicesState.Success).deviceList
                                else -> emptyList()
                            }

                            LabeledBox(
                                label = "Thiết bị đã sở hữu",
                                value = ownedDevices.size.toString(),
                                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                            )

                            if (ownedDevices.isNotEmpty()) {
                                LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
                                    itemsIndexed(ownedDevices) { index, device ->
                                        Spacer(modifier = Modifier.height(8.dp))
                                        DeviceCardSwipeable(
                                            deviceName = device.name?: "Thiết bị $index",
                                            roomName = "Rom",
                                            isRevealed = device.isRevealed,
                                            onExpandOnly = {
                                                listOfUserOwnedDevicesViewModel.updateRevealState(index)
                                            },
                                            onCollapse = {
                                                listOfUserOwnedDevicesViewModel.collapseItem(index)
                                            },
                                            onDelete = {
                                            },
                                            onEdit = { }
                                        )
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize().padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Không tìm thấy")
                                }
                            }
                        }
                        1 -> {
                            LabeledBox(
                                label = "Thiết bị được chia sẽ",
                                value = sharedDevices.size.toString(),
                                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                            )

                            if (sharedDevices.isNotEmpty()) {
                                LazyColumn (
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                ){
                                    itemsIndexed(sharedDevices) { index, device ->
                                        Spacer(modifier = Modifier.height(8.dp))
                                        DeviceCardSwipeable(
                                            deviceName = device.name,
                                            roomName = device.room,
                                            isRevealed = device.isRevealed,
                                            onExpandOnly = {
                                                listOfUserOwnedDevicesViewModel.updateRevealState(index)
                                            },
                                            onCollapse = {
                                                listOfUserOwnedDevicesViewModel.collapseItem(index)
                                            },
                                            onDelete = { sharedDevices.removeAt(index) },
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
        )
    }
}