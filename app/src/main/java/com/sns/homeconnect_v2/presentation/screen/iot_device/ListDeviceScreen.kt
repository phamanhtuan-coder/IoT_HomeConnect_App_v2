package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.ListOfUserOwnedDevicesState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.ListOfUserOwnedDevicesViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.sharing.DeviceSharingViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.sharing.SharedDeviceState

@Composable
fun ListDeviceScreen(
    navController: NavHostController = rememberNavController(),
    listOfUserOwnedDevicesViewModel: ListOfUserOwnedDevicesViewModel = hiltViewModel(),
    deviceSharingViewModel: DeviceSharingViewModel = hiltViewModel(),
) {
    val ownedDevicesState by listOfUserOwnedDevicesViewModel.listOfUserOwnedDevicesState.collectAsState()
    val sharedDevicesState by deviceSharingViewModel.sharedDevicesState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val deviceOwnershipTabs = listOf("Sở hữu", "Chia sẻ")
    var searchQuery by remember { mutableStateOf("") }

    // Log trạng thái thiết bị được chia sẻ
    LaunchedEffect(sharedDevicesState) {
        Log.d("SHARED_STATE", sharedDevicesState.toString())
        if (sharedDevicesState is SharedDeviceState.Success) {
            val devices = (sharedDevicesState as SharedDeviceState.Success).devices
            devices.forEachIndexed { i, d ->
                Log.d("SHARED_DEVICE", "[$i] ${d.device_serial} - ${d.device_name}")
            }
        } else if (sharedDevicesState is SharedDeviceState.Error) {
            Log.e("SHARED_ERROR", (sharedDevicesState as SharedDeviceState.Error).message)
        }
    }

    // Lấy danh sách thiết bị ban đầu
    LaunchedEffect(Unit) {
        listOfUserOwnedDevicesViewModel.getListOfUserOwnedDevices()
        deviceSharingViewModel.getSharedDevicesForUser()
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        Scaffold(
            containerColor = colorScheme.background,
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Danh sách thiết bị"
                )
            },
            bottomBar = {
                MenuBottom(navController)
            },
            floatingActionButtonPosition = FabPosition.End,
            content = { contentPadding ->
                Column(
                    modifier = Modifier.padding(contentPadding)
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
                                modifier = Modifier.fillMaxWidth(),
                                onSearch = { query ->
                                    searchQuery = query
                                    if (selectedTabIndex == 0) {
                                        // Tìm kiếm thiết bị sở hữu
                                        listOfUserOwnedDevicesViewModel.searchOwnedDevices(query)
                                    } else {
                                        // Tìm kiếm thiết bị được chia sẻ
                                        //deviceSharingViewModel.searchSharedDevices(query)
                                    }
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
                            onTabSelected = { index ->
                                selectedTabIndex = index
                                // Reset search query khi chuyển tab
                                searchQuery = ""
                                if (index == 0) {
                                    listOfUserOwnedDevicesViewModel.getListOfUserOwnedDevices()
                                } else {
                                    deviceSharingViewModel.getSharedDevicesForUser()
                                }
                            },
                        )
                    }

                    when (selectedTabIndex) {
                        0 -> {
                            val ownedDevices = when (ownedDevicesState) {
                                is ListOfUserOwnedDevicesState.Success -> (ownedDevicesState as ListOfUserOwnedDevicesState.Success).deviceList
                                is ListOfUserOwnedDevicesState.Error -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize().padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Lỗi: ${(ownedDevicesState as ListOfUserOwnedDevicesState.Error).error}")
                                    }
                                    emptyList()
                                }
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
                                            deviceName = device.name ?: "Thiết bị $index",
                                            roomName = "ROM",
                                            isRevealed = device.isRevealed,
                                            isOwner = true,
                                            isView = false,
                                            onExpandOnly = {
                                                listOfUserOwnedDevicesViewModel.updateRevealState(index)
                                            },
                                            onClick = {
                                                navController.navigate(
                                                    Screens.DynamicDeviceDetail.build(
                                                        deviceId = device.device_id,
                                                        deviceName = device.name.orEmpty(),
                                                        serialNumber = device.serial_number,
                                                        productId = device.template_id
                                                    )
                                                )
                                            },
                                            onCollapse = {
                                                listOfUserOwnedDevicesViewModel.collapseItem(index)
                                            },
                                            onDelete = { /* Xử lý xóa nếu cần */ },
                                            onEdit = { /* Xử lý chỉnh sửa nếu cần */ }
                                        )
                                    }
                                }
                            } else if (ownedDevicesState !is ListOfUserOwnedDevicesState.Error) {
                                Box(
                                    modifier = Modifier.fillMaxSize().padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Không tìm thấy thiết bị")
                                }
                            }
                        }
                        1 -> {
                            val sharedDevices = when (sharedDevicesState) {
                                is SharedDeviceState.Success -> (sharedDevicesState as SharedDeviceState.Success).devices
                                is SharedDeviceState.Error -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize().padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Lỗi: ${(sharedDevicesState as SharedDeviceState.Error).message}")
                                    }
                                    emptyList()
                                }
                                else -> emptyList()
                            }

                            LabeledBox(
                                label = "Thiết bị được chia sẻ",
                                value = sharedDevices.size.toString(),
                                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                            )

                            if (sharedDevices.isNotEmpty()) {
                                LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
                                    itemsIndexed(sharedDevices) { index, device ->
                                        Spacer(modifier = Modifier.height(8.dp))
                                        DeviceCardSwipeable(
                                            deviceName = device.device_name ?: "Thiết bị $index",
                                            roomName = when (device.permission_type) {
                                                "VIEW" -> "Chỉ xem"
                                                "CONTROL" -> "Điều khiển"
                                                else -> "Không xác định"
                                            },
                                            isOwner = false,
                                            isRevealed = false,
                                            isView = device.permission_type == "VIEW",
                                            onExpandOnly = {},
                                            onCollapse = {},
                                            onClick = {
                                                navController.navigate(
                                                    Screens.DynamicDeviceDetail.build(
                                                        deviceId = device.device_id ?: "",
                                                        deviceName = device.device_name.orEmpty(),
                                                        serialNumber = device.device_serial,
                                                        productId = device.template_id ?: "",
                                                        permissionType = device.permission_type
                                                    )
                                                )
                                            },
                                            onDelete = { /* Xử lý xóa nếu cần */ },
                                            onEdit = { /* Xử lý chỉnh sửa nếu cần */ }
                                        )
                                    }
                                }
                            } else if (sharedDevicesState !is SharedDeviceState.Error) {
                                Box(
                                    modifier = Modifier.fillMaxSize().padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Không tìm thấy thiết bị")
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}