@file:Suppress("DEPRECATION")

package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Room
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.presentation.component.ScanCodeDialog
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.GenericDropdown
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField

/**
 * Composable function đại diện cho màn hình liên kết thiết bị IoT mới.
 * Màn hình này cho phép người dùng nhập ID thiết bị, tên thiết bị, chọn một không gian (phòng),
 * và liên kết thiết bị bằng cách nhập thủ công hoặc quét mã QR.
 *
 * Màn hình bao gồm các trường nhập liệu cho ID và tên thiết bị có kiểm tra hợp lệ,
 * một danh sách thả xuống để chọn không gian, một nút để bắt đầu quét mã QR,
 * và một nút để thực hiện thao tác liên kết.
 *
 * Nó sử dụng một số thành phần Material 3 và các thành phần tùy chỉnh như `StyledTextField`,
 * `GenericDropdown`, `ActionButtonWithFeedback`, và `ScanCodeDialog`.
 *
 * Các phần bị chú thích gợi ý việc tích hợp với các ViewModel (`SharedViewModel`,
 * `DeviceViewModel`, `AddDeviceViewModel`) để quản lý trạng thái và các hoạt động dữ liệu,
 * chẳng hạn như lấy danh sách không gian có sẵn và xử lý quá trình liên kết thiết bị.
 * Hiện tại, chúng không hoạt động trong đoạn mã được cung cấp.
 *
 * @author Nguyễn Thanh Sang
 * @since 19-05-2025
 *
 * @param navController [NavHostController] để thực hiện các hành động điều hướng, chẳng hạn như quay lại.
 *                      Nó được sử dụng bởi các thành phần `Header` và `MenuBottom`.
 *
 * Các tham số sau đây bị chú thích nhưng chỉ ra các phụ thuộc ViewModel tiềm năng:
 * @param sharedViewModel Một thể hiện của `SharedViewModel` (bị chú thích).
 *                        Có khả năng được sử dụng để chia sẻ dữ liệu như `houseId` giữa các màn hình khác nhau.
 * @param deviceViewModel Một thể hiện của `DeviceViewModel` (bị chú thích).
 *                        Có khả năng được sử dụng để lấy dữ liệu liên quan đến thiết bị và không gian, ví dụ: `getSpacesByHomeId`.
 * @param addDeviceViewModel Một thể hiện của `AddDeviceViewModel` (bị chú thích).
 *                           Có khả năng được sử dụng để xử lý logic thêm/liên kết thiết bị mới,
 *                           quản lý các trạng thái như `linkDeviceState`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkDeviceScreen(
    navController: NavHostController,
//    sharedViewModel: SharedViewModel = hiltViewModel(),
//    deviceViewModel : DeviceViewModel = hiltViewModel(),
//    addDeviceViewModel : AddDeviceViewModel = hiltViewModel()
) {
    // Lấy houseId từ SharedViewModel
//    val houseId by sharedViewModel.houseId.collectAsState()

    // Gọi hàm lấy danh sách Spaces khi houseId != null
//    LaunchedEffect(houseId) {
//        houseId?.let {
//            deviceViewModel.getSpacesByHomeId(it)
//        }
//    }

    // Lắng nghe luồng State của danh sách space
//    val spacesState by deviceViewModel.spacesListState.collectAsState()

//    val deviceLinkState by addDeviceViewModel.linkDeviceState.collectAsState()

    // Biến Compose
//    val coroutineScope = rememberCoroutineScope()

    var current by remember { mutableStateOf<String?>(null) }

    var deviceId by remember { mutableStateOf("") }
    var deviceName by remember { mutableStateOf("") }

    var deviceIdError by remember { mutableStateOf("") }
    var deviceNameError by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    // Những biến cho dropdown:
//    var expanded by remember { mutableStateOf(false) }
//    var selectedSpaceName by remember { mutableStateOf("Chọn phòng") }
//    var selectedSpaceId by remember { mutableStateOf<Int?>(null) }

//    val configuration = LocalConfiguration.current
//    val isTablet = configuration.screenWidthDp >= 600

    // Lắng nghe DeviceLinkState để xử lý side-effect (nếu cần)
//    LaunchedEffect(deviceLinkState) {
//        when (deviceLinkState) {
//            is LinkDeviceState.LinkSuccess -> {
//                // Xử lý thành công
//                val successMsg = (deviceLinkState as LinkDeviceState.LinkSuccess).message
//                Log.d("AddDeviceScreen", "LinkSuccess: $successMsg")
//                // Có thể hiển thị Toast hoặc điều hướng màn khác
//            }
//            is LinkDeviceState.Error -> {
//                // Xử lý lỗi
//                val errMsg = (deviceLinkState as LinkDeviceState.Error).error
//                Log.e("AddDeviceScreen", "Error linkDevice: $errMsg")
//            }
//            else -> {
//                // Idle hoặc Loading, chưa cần gì thêm
//            }
//        }
//    }

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
                                        StyledTextField(
                                            value = deviceId,
                                            onValueChange = {
                                                deviceId = it
                                                deviceIdError = ValidationUtils.validateDeviceId(it)
                                            },
                                            placeholderText = "ID Thiết bị",
                                            leadingIcon = Icons.Default.Devices
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Ô nhập Tên thiết bị
                                        StyledTextField(
                                            value = deviceName,
                                            onValueChange = {
                                                deviceName = it
                                                deviceNameError = ValidationUtils.validateDeviceName(it)
                                            },
                                            placeholderText = "Tên thiết bị",
                                            leadingIcon = Icons.Default.Devices
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Dropdown Spaces
                                        // Nếu bạn không muốn dùng ExposedDropdownMenuBox
                                        // có thể tùy chỉnh DropdownMenuItem thủ công, nhưng dưới đây là ví dụ M3.

                                        GenericDropdown(
                                            items = listOf("Phòng khách", "Phòng ngủ", "Nhà bếp"),
                                            selectedItem = current,
                                            onItemSelected = { current = it },
                                            isTablet = false,
                                            leadingIcon = Icons.Default.Room // 👈 truyền icon vào
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        ActionButtonWithFeedback(
                                            label = "Quét mã QR",
                                            style = HCButtonStyle.PRIMARY,
                                            onAction = { onS, _ -> onS("Mở camera"); showDialog = true }
                                        )

                                        if (showDialog) {
                                            ScanCodeDialog(
                                                code = "1234-5678-6565-3333",
                                                onDismiss = { showDialog = false },
                                                onOk      = { showSuccess = true }
                                            )
                                        }

                                        if (showSuccess) {
                                            AlertDialog(
                                                onDismissRequest = { showSuccess = false },
                                                confirmButton = { TextButton(onClick = { showSuccess = false }) { Text("Đóng") } },
                                                title = { Text("🎉  Thành công!", fontSize = 20.sp) },
                                                text  = { Text("Thiết bị đã được xác nhận.") },
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        ActionButtonWithFeedback(
                                            label = "Liên kết",
                                            style = HCButtonStyle.SECONDARY,
                                            onAction = { onS, onE ->
                                                deviceIdError = ValidationUtils.validateDeviceId(deviceId)
                                                deviceNameError = ValidationUtils.validateDeviceName(deviceName)

                                                if (deviceIdError.isNotBlank() || deviceNameError.isNotBlank()) {
                                                    onE("Thông tin không hợp lệ")
                                                    return@ActionButtonWithFeedback
                                                }

                                                try {
                                                    val success =
                                                        addDeviceViewModel.linkDeviceSync(
                                                            deviceId = deviceId,
                                                            spaceId = selectedSpaceId.toString(),
                                                            deviceName = deviceName
                                                        ) // giả sử đây là suspend fun trả true/false

                                                    if (success) {
                                                        onS("Liên kết thành công")
                                                    } else {
                                                        onE("Liên kết thất bại")
                                                    }
                                                } catch (e: Exception) {
                                                    onE("Lỗi: ${e.message}")
                                                }
                                            }
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

@Preview(showBackground = true)
@Composable
fun LinkDeviceScreenPreview() {
    val navController = rememberNavController() // Fake NavController for preview
    LinkDeviceScreen(navController = navController)
}