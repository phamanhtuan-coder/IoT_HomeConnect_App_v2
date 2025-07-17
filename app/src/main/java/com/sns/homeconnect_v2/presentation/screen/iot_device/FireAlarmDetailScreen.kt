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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ProductData
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.presentation.component.CustomSwitch
import com.sns.homeconnect_v2.presentation.component.InfoRow
import com.sns.homeconnect_v2.presentation.component.SingleColorCircleWithDividers
import com.sns.homeconnect_v2.presentation.component.dialog.WarningDialog
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.button.DeviceQuickActions
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceDisplayInfoState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceDisplayViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceStateUiState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.UnlinkState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.UpdateDeviceStateBulkUiState
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Enum class đại diện cho các hành động khác nhau có thể được thực hiện trên một thiết bị.
 *
 * Enum này được sử dụng để quản lý và xác định các hoạt động khác nhau của thiết bị trong ứng dụng,
 * đặc biệt trong bối cảnh tương tác người dùng và các lệnh gọi API.
 *
 * - **LOCK**: Đại diện cho hành động khóa thiết bị, ngăn chặn việc sử dụng hoặc thay đổi trái phép.
 * - **UNLINK**: Đại diện cho hành động xóa thiết bị khỏi tài khoản hoặc mạng của người dùng.
 * - **RESET**: Đại diện cho hành động đặt lại thiết bị về cài đặt gốc mặc định.
 * - **SHARE**: Đại diện cho hành động chia sẻ quyền kiểm soát hoặc truy cập thiết bị với người dùng khác.
 * - **TRANSFER**: Đại diện cho hành động chuyển quyền sở hữu thiết bị cho người dùng khác.
 * - **VERSION**: Đại diện cho hành động kiểm tra phiên bản firmware hoặc phần mềm hiện tại của thiết bị.
 * - **REPORT_LOST**: Đại diện cho hành động báo cáo thiết bị bị mất hoặc bị đánh cắp.
 *
 * @author Nguyễn Thanh Sang
 * @since 20-05-2025
 */
enum class DeviceAction {
    LOCK, UNLINK, RESET,
    SHARE, TRANSFER, VERSION, REPORT_LOST
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FireAlarmDetailScreen(
    navController: NavHostController,
    deviceId: String,
    deviceName: String,
    deviceTypeName: String,
    serialNumber: String,
    groupId: Int ,
    product: ProductData,
    controls: Map<String, String>,
    isViewOnly: Boolean = true, // Thêm biến này để xác định chế độ xem chỉ
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
) {
    // ngay trên cùng của DeviceDetailScreen (cùng nhóm với showConfirm, loadingAction …)
    var showFullScreenLoading by remember { mutableStateOf(false) }
    // Khởi tạo ViewModel
    val deviceViewModel: DeviceViewModel = hiltViewModel()
    // Khởi tạo ViewModel để lấy trạng thái thiết bị
    val deviceDisplayViewModel: DeviceDisplayViewModel = hiltViewModel()
    // Lấy trạng thái của thiết bị từ ViewModel
    val deviceStateUiState by deviceDisplayViewModel.deviceState.collectAsState()
    // Lấy thông tin thiết bị từ ViewModel
    val updateBulkUiState by deviceDisplayViewModel.updateBulk.collectAsState()

    // Lấy dữ liệu sensor từ ViewModel
    val sensorJson = deviceViewModel.sensorData.value

    var pendingPowerStatus by remember { mutableStateOf<Boolean?>(null) }

    var isPowerUpdating by remember { mutableStateOf(false) }

    var pendingOnSuccess by remember { mutableStateOf<((String) -> Unit)?>(null) }
    var pendingOnError   by remember { mutableStateOf<((String) -> Unit)?>(null) }
    var loadingAction    by remember { mutableStateOf<DeviceAction?>(null) }

    val unlinkState by deviceDisplayViewModel.unlinkState.collectAsState()

    LaunchedEffect(unlinkState) {
        when (val state = unlinkState) {
            is UnlinkState.Success -> {
                showFullScreenLoading = false
                snackbarViewModel.showSnackbar("Gỡ thiết bị thành cộng", SnackbarVariant.SUCCESS)
                pendingOnSuccess?.invoke(state.message)    // ✅ báo nút dừng
                pendingOnSuccess = null
                loadingAction     = null               // ✅ tắt spinner
                navController.popBackStack()
            }
            is UnlinkState.Error -> {
                showFullScreenLoading = false
                snackbarViewModel.showSnackbar("Gỡ thiết bị thất bại", SnackbarVariant.ERROR)
                pendingOnError?.invoke(state.error)
                pendingOnError  = null
                loadingAction   = null
            }
            is UnlinkState.Loading -> { /* optionally show loading */ }
            is UnlinkState.Idle    -> { /* optionally do nothing */ }
        }
    }

    // Lấy context
    LaunchedEffect(Unit) {
        deviceViewModel.initSocket(deviceId, serialNumber)
        deviceDisplayViewModel.fetchDeviceState(serialNumber)
    }

    // State để giữ power từ API (lấy từ deviceState)
    var powerStatusUI by remember { mutableStateOf(false) }

    // Khi deviceStateUiState thành công thì cập nhật lại biến isCheck
    LaunchedEffect(deviceStateUiState) {
        val s = deviceStateUiState
        if (s is DeviceStateUiState.Success) {
            val serverPower = s.state.power_status == true

            // chỉ đồng bộ UI khi: (1) không có lệnh chờ, hoặc (2) server đã phản hồi đúng
            if (pendingPowerStatus == null || pendingPowerStatus == serverPower) {
                powerStatusUI      = serverPower
                pendingPowerStatus = null
                isPowerUpdating    = false
            }
        } else if (s is DeviceStateUiState.Error) {
            isPowerUpdating    = false
            pendingPowerStatus = null
            snackbarViewModel.showSnackbar(s.message, SnackbarVariant.ERROR)
        }
    }

    LaunchedEffect(updateBulkUiState) {
        when (val st = updateBulkUiState) {
            is UpdateDeviceStateBulkUiState.Success -> {
                snackbarViewModel.showSnackbar("Cập nhật thành cộng", SnackbarVariant.SUCCESS)
                isPowerUpdating = false                // mở khóa Switch
                deviceDisplayViewModel.fetchDeviceState(serialNumber)
            }
            is UpdateDeviceStateBulkUiState.Error -> {
                snackbarViewModel.showSnackbar("Cập nhật thất bại", SnackbarVariant.ERROR)
                isPowerUpdating  = false               // mở khóa
                pendingPowerStatus = null              // hủy lệnh chờ
            }
            else -> {}
        }
    }

    // Biến trạng thái cho các thanh trượt
    var gasSliderValue by remember { mutableFloatStateOf(0f) }
    var tempSliderValue by remember { mutableFloatStateOf(0f) }
    var humiditySliderValue by remember { mutableFloatStateOf(0f) }

    // Lắng nghe sự thay đổi của sensorJson và cập nhật giá trị thanh trượt
    LaunchedEffect(sensorJson) {
        sensorJson?.let { json ->
            val gas = json.optInt("gas", -1)
            val temp = json.optInt("temperature", -1) // ✅ fix key
            val hum = json.optInt("humidity", -1)     // ✅ fix key

            if (gas != -1) gasSliderValue = gas.toFloat()
            if (temp != -1) tempSliderValue = temp.toFloat()
            if (hum != -1) humiditySliderValue = hum.toFloat()
        }
    }

    // Biến trạng thái khác
    var rowWidth by remember { mutableIntStateOf(0) }
    val isTablet = isTablet(LocalContext.current)

    /* state giữ hàm onSuccess / onError tạm thời */
    val scope = rememberCoroutineScope()

    /* ---------- 2. STATE DÙNG CHUNG ---------- */
    var pendingAction      by remember { mutableStateOf<DeviceAction?>(null) }

    var confirmTitle       by remember { mutableStateOf("") }
    var confirmMessage     by remember { mutableStateOf("") }
    var showConfirm        by remember { mutableStateOf(false) }

    var infoDevice by remember { mutableStateOf<DeviceResponse?>(null) } // Lắng nghe danh sách thiết bị

//    val infoDeviceState by viewModel.infoDeviceState.collectAsState()

//    when (infoDeviceState) {
//        is GetInfoDeviceState.Error -> {
//            Log.d("Error", (infoDeviceState as GetInfoDeviceState.Error).error)
//        }
//
//        is GetInfoDeviceState.Success -> {
//            infoDevice = (infoDeviceState as GetInfoDeviceState.Success).device
//            Log.d("List Device", (infoDeviceState as GetInfoDeviceState.Success).device.toString())
//        }
//
//        else -> {
//            /* Do nothing */
//        }
//    }

    var toggleDevice by remember { mutableStateOf<ToggleResponse?>(null) } // Lắng nghe danh sách thiết bị
//    val toggleDeviceState by viewModel.toggleState.collectAsState()

//    when (toggleDeviceState) {
//        is ToggleState.Error -> {
//            Log.e("Error", (toggleDeviceState as ToggleState.Error).error)
//        }
//
//        is ToggleState.Success -> {
//            val successState = toggleDeviceState as ToggleState.Success
//            toggleDevice = successState.toggle
//            Log.e("toggle Device", toggleDevice.toString())
//        }
//
//        else -> {
//            /* Do nothing */
//        }
//    }

    val showBottomSheet = remember { mutableStateOf(false) }

    var safeDevice = infoDevice ?: DeviceResponse(
        DeviceID = 0,
        TypeID = 0,
        Name = "Dinning room",
        PowerStatus = false,
        SpaceID = 0,
        Attribute = ""
    )

    Log.e("safeDevice", safeDevice.toString())

    LaunchedEffect(toggleDevice) {
        safeDevice = infoDevice ?: DeviceResponse(
            DeviceID = 0,
            TypeID = 0,
            Name = "Dinning room",
            PowerStatus = false,
            SpaceID = 0,
            Attribute = ""
        )
    }

//    var toggle by remember {
//        mutableStateOf(ToggleRequest(powerStatus = powerStatus))
//    }
//    val unlinkState by viewModel.unlinkState.collectAsState()

//    when (unlinkState) {
//        is UnlinkState.Error -> {
//            Log.e("Error Unlink Device", (unlinkState as UnlinkState.Error).error)
//        }
//
//        is UnlinkState.Success -> {
//            Log.d("Unlink Device", (unlinkState as UnlinkState.Success).message)
//        }
//
//        else -> {
//            /* Do nothing */
//        }
//    }

//    val deviceStatusJson = deviceViewModel.deviceStatus.value
//    LaunchedEffect(deviceStatusJson) {
//        deviceStatusJson?.let {
//            val status = it.optString("status", "unknown")
//            Log.d("SocketStatus", "Thiết bị đang $status")
//        }
//    }

    var showAlertDialog by remember { mutableStateOf(false) }
    if (showAlertDialog) {
        WarningDialog(
            title = "Gỡ kết nối",
            text = "Bạn có chắc chắn muốn gỡ kết nối thiết bị này không?",
            onConfirm = {
//                viewModel.unlinkDevice(safeDevice.DeviceID)
                showAlertDialog = false
                navController.popBackStack()
            },
            onDismiss = {
                showAlertDialog = false
            }
        )
    }

    val sliderKeys = listOf(
        "gas" to "Khí gas",
        "temp" to "Nhiệt độ",
        "hum" to "Độ ẩm"
    )

    val sliderValues = mapOf(
        "gas" to gasSliderValue.toInt(),
        "temp" to tempSliderValue.toInt(),
        "hum" to humiditySliderValue.toInt()
    )

    // Khi công tắc OFF → trả về 0, còn ON → giá trị thật
    val displayedValues = sliderValues.mapValues { (k, v) ->
        if (powerStatusUI) v            // thiết bị đang bật
        else 0                          // thiết bị tắt → 0
    }


    val selectedStatus = when {
        sliderValues["gas"] ?: 0 > 600 -> "Báo động"
        sliderValues["temp"] ?: 0 > 40 -> "Báo động"
        else -> "Bình thường"
    }

    val sliderUnits = mapOf(
        "gas" to "ppm",
        "temp" to "°C",
        "hum" to "%"
    )

    val displayViewModel: DeviceDisplayViewModel = hiltViewModel()
    val displayState by displayViewModel.displayState.collectAsState()

    val colorScheme = MaterialTheme.colorScheme
    IoTHomeConnectAppTheme {
        Box(Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    /*
                * Hiển thị Header
                 */
                    Header(
                        navController = navController,
                        type = "Back",
                        title = "Chi tiết thiết bị"
                    )
                },
                bottomBar = {
                    /*
                * Hiển thị Thanh Menu dưới cùng
                 */
                    MenuBottom(navController)
                },
                containerColor = colorScheme.background,
                modifier = Modifier.fillMaxSize(),
                content = { innerPadding ->
                    LazyColumn (
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        item {
                            ColoredCornerBox(
                                cornerRadius = 40.dp
                            ) {
                                // Phần đầu tiên: Hiển thị thông tin "Dining Room"
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    ) // Canh lề trên và phải
                                ) {
                                    // Cột chứa các thông tin của phòng
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth() // Chiều rộng đầy đủ
                                            //.background(color = colorScheme.primary) // Nền màu xanh dương
                                            .weight(0.2f), // Chiếm 20% của Row
                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.SpaceBetween // Các thành phần cách đều nhau
                                    ) {
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = deviceName,
                                            color = colorScheme.onPrimary, // Màu chữ trắng
                                            lineHeight = 27.sp,
                                            fontSize = 25.sp
                                        ) // Tiêu đề

                                        // Switch bật/tắt đèn
                                        CustomSwitch(
                                            isCheck  = powerStatusUI,
                                            enabled  = !isPowerUpdating,          // chỉ khóa khi đang chờ server
                                            onCheckedChange = { newStatus ->
                                                pendingPowerStatus = newStatus
                                                isPowerUpdating   = true          // khóa ngay

                                                Log.d("SwitchAction", "Gửi bulk power_status = $newStatus")

                                                deviceDisplayViewModel.updateDeviceStateBulk(
                                                    serial_number = serialNumber,
                                                    serial   = serialNumber,
                                                    updates  = listOf(mapOf("power_status" to newStatus))
                                                )
                                            }
                                        )

                                        Text(
                                            text = "Trạng thái hiện tại: ",
                                            color = Color(0xFF222222),
                                            fontSize = 16.sp
                                        )

                                        Text(
                                            text = selectedStatus,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = when (selectedStatus) {
                                                "Báo động" -> Color.Red
                                                "Lỗi" -> Color.Yellow
                                                else -> Color(0xFF228B22)
                                            }
                                        )
                                    }

                                    SingleColorCircleWithDividers(
                                        selectedStatus = selectedStatus,
                                        dividerCount = 12
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            InvertedCornerHeader(
                                backgroundColor = colorScheme.surface,
                                overlayColor = colorScheme.primary
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    /* ---------- INFO ---------- */
                                    IconButton(
                                        onClick = {
                                            showBottomSheet.value = true
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "Info",
                                            tint = colorScheme.primary,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }

                                    /* ---------- WIFI ---------- */
                                    IconButton(
                                        onClick = {
                                            navController.navigate(
                                                Screens.AccessPoint.route +
                                                        "?id=${deviceId}&name=${"Lamp"}"
                                            )
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Wifi,
                                            contentDescription = "Wi-Fi",
                                            tint = colorScheme.primary,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onSizeChanged { size ->
                                        rowWidth = size.width // Lấy kích thước của Row
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .widthIn(max = if (isTablet) 700.dp else 400.dp)
                                        .padding(horizontal = 12.dp)
                                        .wrapContentWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    sliderKeys.forEach { (key, label) ->
                                        if (controls[key] == "slider") { // Đúng kiểu slider trong controls
                                            InfoRow(
                                                label  = "$label:",
                                                value  = "${displayedValues[key] ?: 0}",   // ⬅️ dùng displayedValues
                                                unit   = sliderUnits[key] ?: "",

                                                // Màu cảnh báo chỉ khi thiết bị bật, còn tắt thì xám
                                                stateColor = if (!powerStatusUI) Color.Gray else when (key) {
                                                    "gas"  -> if (sliderValues["gas"]  ?: 0 > 600) Color.Red else Color.Green
                                                    "temp" -> if (sliderValues["temp"] ?: 0 > 40 ) Color.Red else Color.Green
                                                    "hum"  -> Color.Green
                                                    else   -> Color.Gray
                                                },

                                                // Ký hiệu “✓/!” chỉ khi bật, tắt thì “–”
                                                stateText  = if (!powerStatusUI) "–" else when (key) {
                                                    "gas"  -> if (sliderValues["gas"]  ?: 0 > 600) "!" else "✓"
                                                    "temp" -> if (sliderValues["temp"] ?: 0 > 40 ) "!" else "✓"
                                                    "hum"  -> "✓"
                                                    else   -> "?"
                                                }
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))
//                                        EdgeToEdgeSlider(
//                                            value = sliderStates[key] ?: 0f,
//                                            onValueChange = sliderSetters[key] ?: {},
//                                            activeTrackColor = Color.Red,
//                                            inactiveTrackColor = Color.Red.copy(alpha = 0.3f),
//                                            thumbColor = Color.Red,
//                                            thumbBorderColor = Color.DarkGray,
//                                            modifier = Modifier.fillMaxWidth()
//                                        )
//                                        Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                }

                                /* ------------------ LAYOUT NÚT HÀNH ĐỘNG ------------------ */
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(Modifier.height(16.dp))

                                    DeviceQuickActions(
                                        serialNumber = serialNumber,
                                        navController = navController,
                                        snackbarVM   = snackbarViewModel,
                                        onRequestConfirm = { action, title, msg ->
                                            confirmTitle  = title
                                            confirmMessage= msg
                                            pendingAction = action
                                            showConfirm   = true
                                        }
                                    )
                                }

                                // WarningDialog xác nhận
                                if (showConfirm) {
                                    WarningDialog(
                                        title = confirmTitle,
                                        text = confirmMessage,
                                        confirmText = "Đồng ý",
                                        dismissText = "Huỷ",
                                        onConfirm = {
                                            showConfirm = false
                                            loadingAction = pendingAction
                                            val action = pendingAction
                                            pendingAction = null

                                            scope.launch {
                                                when (action) {
                                                    DeviceAction.UNLINK -> {
                                                        showFullScreenLoading = true
                                                        delay(5_000)
                                                        displayViewModel.unlinkDevice(
                                                            serialNumber = serialNumber,
                                                            groupId      = groupId
                                                        )
                                                    }

                                                    DeviceAction.LOCK -> {
                                                        // TODO: Gọi API khoá thiết bị nếu có
                                                        delay(1000)
                                                        pendingOnSuccess?.invoke("Đã khoá thiết bị!")
                                                    }

                                                    DeviceAction.RESET -> {
                                                        // TODO: Gọi API reset thiết bị nếu có
                                                        delay(1000)
                                                        pendingOnSuccess?.invoke("Đã reset thiết bị!")
                                                    }

                                                    DeviceAction.TRANSFER -> {
                                                        // TODO: Gọi API chuyển quyền nếu có
                                                        delay(1000)
                                                        pendingOnSuccess?.invoke("Đã chuyển quyền!")
                                                    }

                                                    DeviceAction.REPORT_LOST -> {
                                                        // TODO: Gọi API báo mất nếu có
                                                        delay(1000)
                                                        pendingOnSuccess?.invoke("Đã báo mất thiết bị!")
                                                    }

                                                    else -> {
                                                        pendingOnError?.invoke("Chưa hỗ trợ thao tác này.")
                                                    }
                                                }

                                                loadingAction = null
                                            }
                                        },
                                        onDismiss = {
                                            showConfirm = false
                                            pendingAction = null
                                        }
                                    )
                                }

                                if (showBottomSheet.value) {
                                ModalBottomSheet(
                                    onDismissRequest = { showBottomSheet.value = false }
                                ) {
                                    when (displayState) {
                                        is DeviceDisplayInfoState.Success -> {
                                            val product = (displayState as DeviceDisplayInfoState.Success).product
                                            val category = (displayState as DeviceDisplayInfoState.Success).category

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                horizontalAlignment = Alignment.Start,
                                                verticalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                // Tên sản phẩm
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.Start,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Info,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = "Tên sản phẩm: ${product.name ?: "Không rõ"}",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                }

                                                // Danh mục
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.Start,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Category,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = "Danh mục: ${category.name ?: "Không rõ"}",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                }

                                                // Giá
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.Start,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.MonetizationOn,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.error,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = "Giá: ${product.selling_price ?: "Không rõ"}",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = MaterialTheme.colorScheme.error
                                                    )
                                                }

                                                // Mô tả
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.Start,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Description,
                                                        contentDescription = null,
                                                        tint = Color(0xFFFFC107),
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = "Mô tả: ${product.description_normal ?: "Không có mô tả"}",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = Color(0xFFFFC107),
                                                        fontWeight = FontWeight.SemiBold
//                                                        modifier = Modifier
//                                                            .weight(1f)
//                                                            .wrapContentWidth(Alignment.CenterHorizontally)
                                                    )
                                                }
                                            }
                                        }

                                        is DeviceDisplayInfoState.Loading -> {
                                            Text("Đang tải thông tin sản phẩm...")
                                        }
                                        is DeviceDisplayInfoState.Error -> {
                                            Text("Lỗi: ${(displayState as DeviceDisplayInfoState.Error).error}")
                                        }
                                        else -> {
                                    }
                                }
                                }
                            }
                            }
                        }
                    }
                }
            )
            if (showFullScreenLoading) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color(0x88000000)),      // mờ toàn màn hình
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }
    }
}

// Thêm extension function để parse toggle log
//fun String.parseToggleCommand(): Boolean? {
//    return try {
//        val command = JSONObject(this)
//        if (command.has("command")) {
//            val toggleCommand = command.getJSONObject("command")
//            toggleCommand.getBoolean("powerStatus")
//        } else {
//            null
//        }
//    } catch (e: Exception) {
//        Log.e("ParseLog", "Error parsing toggle command", e)
//        null
//    }
//}

// Thêm data class để giữ giá trị sensor
//data class SensorData(
//    val gas: Int = 0,
//    val temperature: Double = 0.0,
//    val humidity: Int = 0
//)

// Extension function để parse sensor data
//fun String.parseSensorData(): SensorData? {
//    return try {
//        val parts = this.split("}")
//        // Lấy phần JSON cuối cùng chứa dữ liệu sensor
//        val sensorJson = parts.last { it.contains("sensorData") } + "}"
//        val data = JSONObject(sensorJson)
//
//        SensorData(
//            gas = data.optInt("gas", 0),
//            temperature = data.optDouble("temperature", 0.0),
//            humidity = data.optInt("humidity", 0)
//        )
//    } catch (e: Exception) {
//        Log.e("ParseLog", "Error parsing sensor data", e)
//        null
//    }
//}
