package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.SpaceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.presentation.component.HouseSelection
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.component.SharedViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.SpaceState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.ToggleState

/** Giao diện màn hình Device Screen
 * -----------------------------------------
 * Người viết: Nguyễn Thanh Sang
 * Ngày viết: 3/12/2024
 * Lần cập nhật cuối: 9/12/2024
 * -----------------------------------------
 * @param navController Đối tượng điều khiển điều hướng.
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
fun DeviceScreen(
    navController: NavHostController,
    deviceViewModel: DeviceViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel(),
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }


    var spaces by remember { mutableStateOf<List<SpaceResponse>>(emptyList()) } // Lắng nghe danh sách thiết bị
    val spacesListState by deviceViewModel.spacesListState.collectAsState()

    when (spacesListState) {
        is SpaceState.Error -> {
            Log.d("Error", (spacesListState as SpaceState.Error).error)
        }

        is SpaceState.Success -> {
            spaces = (spacesListState as SpaceState.Success).spacesList
            Log.d("List Device", (spacesListState as SpaceState.Success).spacesList.toString())
            if (selectedTabIndex == 0) {
                deviceViewModel.getDevicesBySpaceId(spaces.first().SpaceID)
            }
        }

        else -> {/* Do nothing */
        }
    }

    var devices by remember { mutableStateOf<List<DeviceResponse>>(emptyList()) } // Lắng nghe danh sách thiết bị
    val deviceListState by deviceViewModel.deviceListState.collectAsState()
    when (deviceListState) {
        is DeviceState.Error -> {
            Log.d("Error", (deviceListState as DeviceState.Error).error)
        }

        is DeviceState.Success -> {
            devices = (deviceListState as DeviceState.Success).deviceList
            Log.d("List Device", (deviceListState as DeviceState.Success).deviceList.toString())
        }

        else -> {/* Do nothing */
        }
    }

    IoTHomeConnectAppTheme {
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp
        val isTablet = screenWidthDp >= 600.dp
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
                FloatingActionButton(
                    onClick = {
                        navController.navigate("add_device")
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Device",
                            tint = colorScheme.primary
                        )
                    },
                    containerColor = colorScheme.onPrimary,
                    contentColor = colorScheme.primary,
                    shape = CircleShape
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize() // Đảm bảo chiếm toàn bộ không gian
                            .imePadding() // Tự động thêm khoảng trống khi bàn phím xuất hiện
                            .verticalScroll(rememberScrollState()) // Cho phép cuộn
                            .padding(innerPadding)
                    ) {
                        // Nội dung bên dưới
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = colorScheme.background)
                                .wrapContentHeight()
                                .defaultMinSize(minHeight = 150.dp)
                        ) {
                            Column {
                                // Hộp màu xanh dương
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .defaultMinSize(minHeight = 110.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(bottomStart = 38.dp)
                                        )
                                        .zIndex(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(if (isTablet) 550.dp else 300.dp)
                                            .align(Alignment.Center)
                                    ) {
                                        HouseSelection(
                                            sharedViewModel = sharedViewModel,
                                            //houses = listOf("House 1", "House 2", "House 3"),
                                            onManageHouseClicked = {
                                                navController.navigate(Screens.HouseManagement.route)
                                            },
                                            onTabSelected = { id ->
                                                deviceViewModel.getSpacesByHomeId(id)
                                            }
                                        )
                                    }
                                }
                                // Hộp màu xanh với góc lõm
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    // Box màu vàng (ở dưới)
                                    Box(
                                        modifier = Modifier
                                            .width(40.dp)
                                            .height(40.dp)
                                            .align(Alignment.TopEnd)
                                            .background(color = colorScheme.primary)
                                            .zIndex(1f) // Z-index thấp hơn
                                    )

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = colorScheme.background,
                                                shape = RoundedCornerShape(topEndPercent = 100)
                                            )
                                            .width(40.dp)
                                            .height(40.dp)
                                            .zIndex(2f) // Z-index cao hơn
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(topEndPercent = 100)) // Clip nội dung ScrollableTabRow
                                        ) {
                                            if (spaces.isNotEmpty()) {
                                                CustomScrollableTabRow(
                                                    selectedTabIndex = selectedTabIndex,
                                                    spaces = spaces,
                                                    onTabSelected = { index, spaceId ->
                                                        selectedTabIndex = index
                                                        deviceViewModel.getDevicesBySpaceId(spaceId) // Cập nhật Space được chọn
                                                    }
                                                )
                                            } else {
                                                // Xử lý khi danh sách spaces trống
                                                Text(
                                                    text = "",
                                                )
                                            }

                                        }
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically // Căn giữa theo chiều dọc
                        ) {
                            Text(
                                text = "Số lượng thiết bị:",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onBackground,
                                modifier = Modifier.padding(end = 4.dp) // Thêm khoảng cách giữa Text và Box
                            )
                            Box(
                                modifier = Modifier
                                    .size(24.dp) // Kích thước Box
                                    .background(
                                        color = colorScheme.primary,
                                        shape = RoundedCornerShape(4.dp)
                                    ), // Màu nền và góc bo
                                contentAlignment = Alignment.Center // Căn Text nằm giữa Box
                            ) {
                                Text(
                                    text = devices.size.toString(),
                                    color = colorScheme.onPrimary, // Màu chữ
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = colorScheme.background),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (devices.isEmpty()) {
                                Text(
                                    text = "Không có thiết bị nào.",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(16.dp)
                                )
                            } else {
                                // Hiển thị danh sách thiết bị
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(500.dp),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    items(devices) { device ->
                                        val matchedSpace =
                                            spaces.find { it.SpaceID == device.SpaceID }
                                        SmartCard(
                                            device = device,
                                            nameSpace = matchedSpace!!.Name,
                                            navController = navController
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun CustomScrollableTabRow(
    selectedTabIndex: Int,
    spaces: List<SpaceResponse>,
    onTabSelected: (Int, Int) -> Unit
) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 8.dp,
            modifier = Modifier
                .background(colorScheme.background, RoundedCornerShape(16.dp)), // Nền và bo góc
            containerColor = colorScheme.background, // Màu nền bên trong TabRow,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 3.dp, // Độ cao con trỏ
                    color = colorScheme.primary // Màu con trỏ
                )
            }
        ) {
            spaces.forEachIndexed { index, space ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        onTabSelected(index, space.SpaceID) // Gửi vị trí và ID Space được chọn
                    },
                    text = {
                        Text(
                            text = space.Name,
                            color = if (selectedTabIndex == index) colorScheme.primary else colorScheme.onBackground, // Đổi màu chữ
                        )
                    },
                    selectedContentColor = colorScheme.onBackground, // Màu khi được chọn
                    unselectedContentColor = colorScheme.onBackground // Màu khi không được chọn
                )
            }
        }
    }
}


@Composable
fun SmartCard(
    device: DeviceResponse, // Nhận thông tin thiết bị từ ViewModel
    nameSpace: String,
    navController: NavHostController,
    deviceViewModel: DeviceViewModel = hiltViewModel(),
) {
    val endPadding = 32.dp

    var powerStatus by remember { mutableStateOf(device.PowerStatus) }

    // Khởi tạo toggle
    var toggle by remember {
        mutableStateOf(ToggleRequest(powerStatus = powerStatus))
    }

    var toggleDevice by remember { mutableStateOf<ToggleResponse?>(null) }
    val toggleDeviceState by deviceViewModel.toggleState.collectAsState()

    when (toggleDeviceState) {
        is ToggleState.Error -> {
            Log.e("Error", (toggleDeviceState as ToggleState.Error).error)
        }

        is ToggleState.Success -> {
            val successState = toggleDeviceState as ToggleState.Success
            toggleDevice = successState.toggle
            Log.e("toggle Device", toggleDevice.toString())
        }

        else -> {/* Do nothing */
        }
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Card(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = colorScheme.primary),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            onClick = {
                navController.navigate("device/${device.TypeID}/${device.DeviceID}")
            }
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = device.Name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = colorScheme.onPrimary
                        )
                        Text(
                            text = nameSpace,
                            fontSize = 12.sp,
                            color = colorScheme.onSecondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Switch(
                            checked = powerStatus,
                            onCheckedChange = {
                                powerStatus = !powerStatus
                                toggle = ToggleRequest(powerStatus = powerStatus)
                                deviceViewModel.toggleDevice(device.DeviceID, toggle)
                            },
                            thumbContent = {
                                Icon(
                                    imageVector = if (powerStatus) Icons.Filled.Check else Icons.Filled.Close,
                                    contentDescription = "On/Off Switch",
                                    tint = if (powerStatus) colorScheme.onPrimary else colorScheme.onSecondary.copy(
                                        alpha = 0.8f
                                    )
                                )
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colorScheme.primary,
                                checkedTrackColor = colorScheme.onPrimary,
                                uncheckedThumbColor = colorScheme.secondary,
                                uncheckedTrackColor = colorScheme.onSecondary.copy(alpha = 0.8f),
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    DeviceInfoSection(device.TypeID, "8 pm", "8 am", endPadding)

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButtonBox("\uD83D\uDDD1")
                        Spacer(modifier = Modifier.height(4.dp))
                        IconButtonBox("\u270E")
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceInfoSection(typeID: Int, fromTime: String, toTime: String, endPadding: Dp) {
    fun getIconForType(typeId: Int): String {
        return when (typeId) {
            1 -> "\uD83D\uDD25" // Fire
            2 -> "\uD83D\uDCA1" // Light
            else -> "❓"         // Biểu tượng mặc định
        }
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconBox(getIconForType(typeID), colorScheme.background)
            TimeInfo("from", fromTime)
            DividerLine(endPadding)
            TimeInfo("to", toTime)
            DividerLine(endPadding)
        }
    }
}

//@Composable
//fun ExtraInfoSection(label: String, value: String, endPadding: Dp) {
//    IoTHomeConnectAppTheme {
//        val colorScheme = MaterialTheme.colorScheme
//        Column(
//            modifier = Modifier.padding(end = endPadding),
//            horizontalAlignment = Alignment.End,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(text = label, fontSize = 12.sp, color = colorScheme.onPrimary)
//            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//        }
//        DividerLine(endPadding)
//    }
//}

@Composable
fun IconBox(icon: String, color: Color) {
    Box(
        modifier = Modifier
            .padding(end = 30.dp)
            .size(40.dp)
            .background(color, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = icon, fontSize = 20.sp)
    }
}

@Composable
fun TimeInfo(label: String, time: String) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Column(
            modifier = Modifier.padding(end = 12.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = label, fontSize = 12.sp, color = colorScheme.onPrimary)
            Text(
                text = time,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun DividerLine(endPadding: Dp) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Box(
            modifier = Modifier
                .padding(end = endPadding)
                .width(1.dp)
                .height(50.dp)
                .background(colorScheme.onSecondary)
        )
    }
}

@Composable
fun IconButtonBox(icon: String) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(colorScheme.background, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 12.sp, color = colorScheme.onBackground)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DeviceScreenPreview() {
    DeviceScreen(navController = rememberNavController())
}