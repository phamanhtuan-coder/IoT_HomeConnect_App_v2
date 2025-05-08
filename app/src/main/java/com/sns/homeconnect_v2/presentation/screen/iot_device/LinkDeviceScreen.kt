@file:Suppress("DEPRECATION")

package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Room
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.viewmodel.component.SharedViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.AddDeviceViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.LinkDeviceState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.SpaceState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel = hiltViewModel(),
    deviceViewModel : DeviceViewModel = hiltViewModel(),
    addDeviceViewModel : AddDeviceViewModel = hiltViewModel()
) {
    // Lấy houseId từ SharedViewModel
    val houseId by sharedViewModel.houseId.collectAsState()

    // Gọi hàm lấy danh sách Spaces khi houseId != null
    LaunchedEffect(houseId) {
        houseId?.let {
            deviceViewModel.getSpacesByHomeId(it)
        }
    }

    // Lắng nghe luồng State của danh sách space
    val spacesState by deviceViewModel.spacesListState.collectAsState()

    val deviceLinkState by addDeviceViewModel.linkDeviceState.collectAsState()

    // Biến Compose
    val coroutineScope = rememberCoroutineScope()

    var deviceId by remember { mutableStateOf("") }
    var deviceName by remember { mutableStateOf("") }

    var deviceIdError by remember { mutableStateOf("") }
    var deviceNameError by remember { mutableStateOf("") }

    // Những biến cho dropdown:
    var expanded by remember { mutableStateOf(false) }
    var selectedSpaceName by remember { mutableStateOf("Chọn phòng") }
    var selectedSpaceId by remember { mutableStateOf<Int?>(null) }

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    // Lắng nghe DeviceLinkState để xử lý side-effect (nếu cần)
    LaunchedEffect(deviceLinkState) {
        when (deviceLinkState) {
            is LinkDeviceState.LinkSuccess -> {
                // Xử lý thành công
                val successMsg = (deviceLinkState as LinkDeviceState.LinkSuccess).message
                Log.d("AddDeviceScreen", "LinkSuccess: $successMsg")
                // Có thể hiển thị Toast hoặc điều hướng màn khác
            }
            is LinkDeviceState.Error -> {
                // Xử lý lỗi
                val errMsg = (deviceLinkState as LinkDeviceState.Error).error
                Log.e("AddDeviceScreen", "Error linkDevice: $errMsg")
            }
            else -> {
                // Idle hoặc Loading, chưa cần gì thêm
            }
        }
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Scaffold(
            containerColor = colorScheme.background,
            topBar = {
                Header(navController, "Back", "Liên kết thiết bị")
            },
            bottomBar = {
                MenuBottom(navController)
            },
            content = { innerPadding ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colorScheme.background)
                                .padding(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        colorScheme.background,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    // Cột chứa các ô nhập liệu và nút "Liên kết"
                                    Column(
                                        modifier = Modifier.width(400.dp)
                                    ) {
                                        // Ô nhập ID thiết bị
                                        OutlinedTextField(
                                            value = deviceId,
                                            onValueChange = {
                                                deviceId = it
                                                deviceIdError = ValidationUtils.validateDeviceId(it)
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Filled.Person,
                                                    contentDescription = null
                                                )
                                            },
                                            shape = RoundedCornerShape(25),
                                            singleLine = true,
                                            placeholder = { Text("ID Thiết bị") },
                                            modifier = Modifier
                                                .width(if (isTablet) 500.dp else 400.dp)
                                                .height(if (isTablet) 80.dp else 70.dp),
                                            colors = TextFieldDefaults.colors(
                                                focusedTextColor = colorScheme.onBackground,
                                                unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),
                                                focusedContainerColor = colorScheme.onPrimary,
                                                unfocusedContainerColor = colorScheme.onPrimary,
                                                focusedIndicatorColor = colorScheme.primary,
                                                unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                                            ),
                                            isError = deviceIdError.isNotBlank()
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Ô nhập Tên thiết bị
                                        OutlinedTextField(
                                            value = deviceName,
                                            onValueChange = {
                                                deviceName = it
                                                deviceNameError = ValidationUtils.validateDeviceName(it)
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.Devices,
                                                    contentDescription = null
                                                )
                                            },
                                            singleLine = true,
                                            shape = RoundedCornerShape(25),
                                            placeholder = { Text("Tên thiết bị") },
                                            modifier = Modifier
                                                .width(if (isTablet) 500.dp else 400.dp)
                                                .height(if (isTablet) 80.dp else 70.dp),
                                            colors = TextFieldDefaults.colors(
                                                focusedTextColor = colorScheme.onBackground,
                                                unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),
                                                focusedContainerColor = colorScheme.onPrimary,
                                                unfocusedContainerColor = colorScheme.onPrimary,
                                                focusedIndicatorColor = colorScheme.primary,
                                                unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                                            ),
                                            isError = deviceNameError.isNotBlank()
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Dropdown Spaces
                                        // Nếu bạn không muốn dùng ExposedDropdownMenuBox
                                        // có thể tùy chỉnh DropdownMenuItem thủ công, nhưng dưới đây là ví dụ M3.
                                       ExposedDropdownMenuBox(
                                            expanded = expanded,
                                            onExpandedChange = { expanded = !expanded }
                                        ) {
                                            OutlinedTextField(
                                                value = selectedSpaceName,
                                                onValueChange = {},
                                                readOnly = true, // Chỉ chọn từ dropdown
                                                singleLine = true,
                                                shape = RoundedCornerShape(25),
                                                leadingIcon = {
                                                    Icon(
                                                        Icons.Default.Room,
                                                        contentDescription = null
                                                    )
                                                },
                                                placeholder = { Text("Chọn phòng") },
                                                modifier = Modifier
                                                    .menuAnchor() // Bắt buộc khi dùng ExposedDropdownMenuBox
                                                    .width(if (isTablet) 500.dp else 400.dp)
                                                    .height(if (isTablet) 80.dp else 70.dp),
                                                trailingIcon = {
                                                    Icon(
                                                        imageVector = Icons.Default.KeyboardArrowDown,
                                                        contentDescription = null
                                                    )
                                                },
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = colorScheme.onBackground,
                                                    unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),
                                                )
                                            )

                                            ExposedDropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = { expanded = false },
                                            ) {
                                                when (spacesState) {
                                                    is SpaceState.Loading -> {
                                                        // Loading menu item
                                                        Box(
                                                            modifier = Modifier
                                                                .padding(8.dp)
                                                                .fillMaxSize()
                                                        ) {
                                                            Text("Đang tải danh sách phòng...")
                                                        }
                                                    }
                                                    is SpaceState.Success -> {
                                                        val spaces = (spacesState as SpaceState.Success).spacesList
                                                        // Duyệt qua danh sách phòng và tạo item
                                                        spaces.forEach { space ->
                                                            DropdownMenuItem(
                                                                text = { Text(space.Name) },
                                                                onClick = {
                                                                    selectedSpaceName = space.Name
                                                                    selectedSpaceId = space.SpaceID
                                                                    expanded = false
                                                                }
                                                            )
                                                        }
                                                    }
                                                    is SpaceState.Error -> {
                                                        val errorMsg = (spacesState as SpaceState.Error).error
                                                        Box(
                                                            modifier = Modifier
                                                                .padding(8.dp)
                                                                .fillMaxWidth()
                                                        ) {
                                                            Text(
                                                                text = "Lỗi khi tải phòng: $errorMsg",
                                                                color = MaterialTheme.colorScheme.error
                                                            )
                                                        }
                                                    }
                                                    else -> {
                                                        /* Do nothing */
                                                    }
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Nút liên kết
                                        Button(
                                            onClick = {
                                                // Validate lần cuối
                                                deviceIdError = ValidationUtils.validateDeviceId(deviceId)
                                                deviceNameError = ValidationUtils.validateDeviceName(deviceName)

                                                // Nếu không có lỗi và đã chọn room
                                                coroutineScope.launch {
                                                    addDeviceViewModel.linkDevice(
                                                        deviceId = deviceId,
                                                        spaceId = selectedSpaceId.toString(),
                                                        deviceName = deviceName
                                                    )
                                                }
                                            },
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .width(if (isTablet) 300.dp else 200.dp)
                                                .height(if (isTablet) 56.dp else 48.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = colorScheme.primary
                                            ),
                                            shape = RoundedCornerShape(50)
                                        ) {
                                            Text(
                                                "Liên kết",
                                                color = colorScheme.onPrimary,
                                            )
                                        }
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