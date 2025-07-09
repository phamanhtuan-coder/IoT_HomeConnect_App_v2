package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.data.remote.dto.request.LinkDeviceRequest
import com.sns.homeconnect_v2.presentation.component.ScanCodeDialog
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.GenericDropdown
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.group.GroupListViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.group.UpdateGroupViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.house.HouseSearchViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.LinkDeviceUiState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.LinkDeviceViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.space.SpaceScreenViewModel

/**
 * Composable function đại diện cho màn hình liên kết thiết bị IoT mới.
 * Màn hình này cho phép người dùng nhập ID thiết bị, tên thiết bị, chọn một không gian (phòng),
 * và liên kết thiết bị bằng cách nhập thủ công hoặc quét mã QR.
 *
 * Màn hình bao gồm các trường nhập liệu cho ID và tên thiết bị có kiểm tra hợp lệ,
 * một danh sách thả xuống để chọn không gian, một nút để bắt đầu quét mã QR (hiển thị `ScanCodeDialog`),
 * và một nút để thực hiện thao tác liên kết. Sau khi quét mã thành công, một `AlertDialog` xác nhận sẽ hiển thị.
 *
 * Nó sử dụng một số thành phần Material 3 và các thành phần tùy chỉnh như `StyledTextField`,
 * `GenericDropdown`, `ActionButtonWithFeedback`, `ScanCodeDialog` và `AlertDialog`.
 *
 * Các phần bị chú thích gợi ý việc tích hợp với các ViewModel (`SharedViewModel`,
 * `DeviceViewModel`, `AddDeviceViewModel`) để quản lý trạng thái và các hoạt động dữ liệu,
 * chẳng hạn như lấy danh sách không gian có sẵn và xử lý quá trình liên kết thiết bị.
 * Hiện tại, chúng không hoạt động trong đoạn mã được cung cấp.
 *
 * @author Nguyễn Thanh Sang
 * @since 19-05-2025
 * @see ScanCodeDialog
 * @see AlertDialog
 *
 * Cập nhật bởi Nguyễn Thanh Sang, ngày 20/05/2025: Bổ sung `ScanCodeDialog` và `AlertDialog` cho chức năng quét QR.
 *
 * @param navController [NavHostController] để thực hiện các hành động điều hướng, chẳng hạn như quay lại.
 *                      Nó được sử dụng bởi các thành phần `Header` và `MenuBottom`.
 *
 * Các tham số sau đây bị chú thích nhưng chỉ ra các phụ thuộc ViewModel tiềm năng:
 * @param sharedViewModel Một thể hiện của `SharedViewModel` (bị chú thích).
 *                        Có khả năng được sử dụng để chia sẻ dữ liệu như `houseId` giữa các màn hình khác nhau.
 * @param deviceViewModel Một thể hiện của `DeviceViewModel` (bị chú thích).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkDeviceScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
    linkDeviceViewModel : LinkDeviceViewModel = hiltViewModel(),
    groupListViewModel : GroupListViewModel = hiltViewModel(),
    houseSearchViewModel: HouseSearchViewModel = hiltViewModel(),
    spaceScreenViewModel: SpaceScreenViewModel = hiltViewModel(),
) {
    // Biến trạng thái để theo dõi ID và tên thiết bị
    var deviceId by remember { mutableStateOf("") }
    var deviceName by remember { mutableStateOf("") }

    // Biến trạng thái để theo dõi lỗi nhập liệu
    var deviceIdError by remember { mutableStateOf("") }
    var deviceNameError by remember { mutableStateOf("") }

    // Biến trạng thái để theo dõi việc hiển thị hộp thoại quét mã QR
    var showDialog by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    // Lấy trạng thái liên kết thiết bị từ ViewModel
    val linkDeviceUiState by linkDeviceViewModel.linkDeviceState.collectAsState()

    // Lấy danh sách nhóm, nhà và không gian từ các ViewModel tương ứng
    val groupList by groupListViewModel.groupList.collectAsState()
    val houseList by houseSearchViewModel.houses.collectAsState()
    val spaceList by spaceScreenViewModel.spaces.collectAsState()

   // Biến để theo dõi các lựa chọn trong dropdown
    var selectedGroupName by remember { mutableStateOf<String?>(null) }
    var selectedHouseName by remember { mutableStateOf<String?>(null) }
    var selectedSpaceName by remember { mutableStateOf<String?>(null) }

    // Biến để theo dõi ID đã chọn từ dropdown
    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    var selectedHouseId by remember { mutableStateOf<Int?>(null) }
    var selectedSpaceId by remember { mutableStateOf<Int?>(null) }

    // Theo dõi các thay đổi trong ID và tên thiết bị để cập nhật yêu cầu liên kết
    LaunchedEffect(linkDeviceUiState) {
        when (linkDeviceUiState) {
            is LinkDeviceUiState.Loading -> {
                // Hiển thị trạng thái đang tải nếu cần
            }
            is LinkDeviceUiState.Success -> {
                // Xử lý thành công liên kết thiết bị
                snackbarViewModel.showSnackbar(
                    msg = "Liên kết thiết bị thành công!",
                    variant = SnackbarVariant.SUCCESS
                )
                navController.navigate(Screens.ListDevices.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            is LinkDeviceUiState.Error -> {
                // Hiển thị thông báo lỗi nếu có
                snackbarViewModel.showSnackbar(
                    msg = (linkDeviceUiState as LinkDeviceUiState.Error).error,
                    variant = SnackbarVariant.ERROR
                )
            }
            else -> {}
        }
    }

    // Lấy danh sách nhóm
    LaunchedEffect(Unit) {
        groupListViewModel.fetchGroups()
    }

    IoTHomeConnectAppTheme {
        // Thiết lập màu sắc chủ đề
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
                Column (
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    ColoredCornerBox(
                        cornerRadius = 40.dp
                    ){}

                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ){}

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement  = Arrangement.Center,
                            horizontalAlignment  = Alignment.CenterHorizontally
                        ) {
                            // Nhập ID thiết bị
                            StyledTextField(
                                value = deviceId,
                                onValueChange = {
                                    deviceId = it
                                    deviceIdError = ValidationUtils.validateDeviceId(it)
                                },
                                placeholderText = "Serial number Thiết bị",
                                leadingIcon = Icons.Default.Devices,
                                modifier = Modifier
                                    .height(56.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Nhập tên thiết bị
                            StyledTextField(
                                value = deviceName,
                                onValueChange = {
                                    deviceName = it
                                    deviceNameError = ValidationUtils.validateDeviceName(it)
                                },
                                placeholderText = "Tên thiết bị",
                                leadingIcon = Icons.Default.Devices,
                                modifier = Modifier
                                    .height(56.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            //Chọn tên nhóm
                            GenericDropdown(
                                items = groupList.map { it.name ?: "Không tên" },
                                selectedItem = selectedGroupName,
                                onItemSelected = { selected ->
                                    selectedGroupName = selected
                                    val group = groupList.firstOrNull { it.name == selected }
                                    selectedGroupId = group?.id
                                    group?.id?.let { houseSearchViewModel.loadHousesByGroup(it) }
                                },
                                leadingIcon = Icons.Default.Group,
                                isTablet = false
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Chọn tên nhà
                            GenericDropdown(
                                items = houseList.map { it.house_name ?: "Không tên" },
                                selectedItem = selectedHouseName,
                                onItemSelected = { selected ->
                                    selectedHouseName = selected
                                    val house = houseList.firstOrNull { it.house_name == selected }
                                    selectedHouseId = house?.house_id
                                    house?.house_id?.let { spaceScreenViewModel.getSpaces(it) }
                                },
                                leadingIcon = Icons.Default.Home,
                                isTablet = false
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Chọn tên không gian
                            GenericDropdown(
                                items = spaceList.map { it.space_name ?: "Không tên" },
                                selectedItem = selectedSpaceName,
                                onItemSelected = { selected ->
                                    selectedSpaceName = selected
                                    val space = spaceList.firstOrNull { it.space_name == selected }
                                    selectedSpaceId = space?.space_id
                                },
                                leadingIcon = Icons.Default.MeetingRoom,
                                isTablet = false
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row {
                                // Nút quét mã QR và nút liên kết
                                ActionButtonWithFeedback(
                                    label = "Quét mã QR",
                                    style = HCButtonStyle.SECONDARY,
                                    onAction = { onS, _ -> onS("Mở camera"); showDialog = true },
                                    snackbarViewModel = snackbarViewModel,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(56.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                if (showDialog) {
                                    ScanCodeDialog(
                                        onDismiss = { showDialog = false },
                                        onOk = { scannedCode ->
                                            deviceId = scannedCode
                                            showSuccess = true
                                        }
                                    )
                                }

                                // Nút liên kết thiết bị
                                ActionButtonWithFeedback(
                                    label = "Liên kết",
                                    style = HCButtonStyle.PRIMARY,
                                    onAction = { msg, err ->
                                        val request = LinkDeviceRequest(
                                            serial_number = deviceId,
                                            name = deviceName,
                                            groupId = selectedGroupId ?: return@ActionButtonWithFeedback err("Chưa chọn nhóm"),
                                            spaceId = selectedSpaceId ?: return@ActionButtonWithFeedback err("Chưa chọn không gian")
                                        )
                                        linkDeviceViewModel.linkDevice(request)
                                    },
                                    snackbarViewModel = snackbarViewModel,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(56.dp) // Chiều cao nút
                                )
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
    val navController = rememberNavController()
    LinkDeviceScreen(
        navController = navController,
        snackbarViewModel = SnackbarViewModel()
    )
}
