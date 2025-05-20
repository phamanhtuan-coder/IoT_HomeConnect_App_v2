package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.presentation.component.CustomSwitch
import com.sns.homeconnect_v2.presentation.component.EdgeToEdgeSlider
import com.sns.homeconnect_v2.presentation.component.InfoRow
import com.sns.homeconnect_v2.presentation.component.SingleColorCircleWithDividers
import com.sns.homeconnect_v2.presentation.component.dialog.WarningDialog
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.navigation.Screens
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

@Composable
fun FireAlarmDetailScreen(
    navController: NavHostController,
//    deviceID: Int?,
//    viewModel: FireAlarmDetailViewModel = hiltViewModel(),
) {
    var rowWidth by remember { mutableIntStateOf(0) }
    val smokeLevel by remember { mutableIntStateOf(20) }
    val temperature by remember { mutableIntStateOf(50) }
    val coLevel by remember { mutableIntStateOf(-1) }
    var showDialog by remember { mutableStateOf(false) }
    var isCheck by remember { mutableStateOf(false) }
    val statusList = listOf("Bình thường", "Báo động", "Lỗi")// Trạng thái
    val status by remember { mutableIntStateOf(0) }
    val isTablet = isTablet(LocalContext.current)

    /* state giữ hàm onSuccess / onError tạm thời */
    val scope = rememberCoroutineScope()

    /* ---------- 2. STATE DÙNG CHUNG ---------- */
    var pendingAction      by remember { mutableStateOf<DeviceAction?>(null) }
    var loadingAction      by remember { mutableStateOf<DeviceAction?>(null) }

    var pendingOnSuccess   by remember { mutableStateOf<((String) -> Unit)?>(null) }
    var pendingOnError     by remember { mutableStateOf<((String) -> Unit)?>(null) }

    var confirmTitle       by remember { mutableStateOf("") }
    var confirmMessage     by remember { mutableStateOf("") }
    var showConfirm        by remember { mutableStateOf(false) }

    var infoDevice by remember { mutableStateOf<DeviceResponse?>(null) } // Lắng nghe danh sách thiết bị

    /* ---------- STATE CHO MỖI SLIDER ---------- */
    var gasSliderValue      by remember { mutableFloatStateOf(smokeLevel.toFloat()) }  // slider khí gas
    var tempSliderValue     by remember { mutableFloatStateOf(temperature.toFloat()) } // slider nhiệt độ
    var humiditySliderValue by remember { mutableFloatStateOf(coLevel.toFloat()) }     // slider độ ẩm

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

    LaunchedEffect(1) {
//        viewModel.getInfoDevice(deviceID!!)
    }

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

    var powerStatus by remember { mutableStateOf(false) }
//    var toggle by remember {
//        mutableStateOf(ToggleRequest(powerStatus = powerStatus))
//    }
    LaunchedEffect(safeDevice) {
        powerStatus = safeDevice.PowerStatus
    }

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

    val colorScheme = MaterialTheme.colorScheme
    IoTHomeConnectAppTheme {
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
                                        .background(color = colorScheme.primary) // Nền màu xanh dương
                                        .weight(0.2f), // Chiếm 20% của Row
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.SpaceBetween // Các thành phần cách đều nhau
                                ) {
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = safeDevice.Name,
                                        color = colorScheme.onPrimary, // Màu chữ trắng
                                        lineHeight = 27.sp,
                                        fontSize = 25.sp
                                    ) // Tiêu đề

                                    // Switch bật/tắt đèn
                                    CustomSwitch(isCheck = isCheck, onCheckedChange = { isCheck = it })

                                    Text(
                                        "Trạng thái hiện tại: ",
                                        color = colorScheme.onPrimary,
                                        fontSize = 16.sp
                                    )

                                    Text(
                                        statusList[status],
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 25.sp,
                                        color = colorScheme.onPrimary
                                    )
                                }

                                SingleColorCircleWithDividers(
                                    selectedStatus = statusList[status],
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
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                /* ---------- INFO ---------- */
                                IconButton(
                                    onClick = { showDialog = true },
                                    modifier = Modifier.size(32.dp)           // toàn bộ IconButton = 32 × 32
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        tint = colorScheme.primary,
                                        modifier = Modifier.size(32.dp)       // icon bên trong = 32
                                    )
                                }

                                /* ---------- WIFI ---------- */
                                IconButton(
                                    onClick = {
                                        navController.navigate(
                                            Screens.AccessPoint.route +
                                                    "?id=${safeDevice.DeviceID}&name=${safeDevice.Name}"
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

                                /* ---------- DIALOG ---------- */
                                if (showDialog) {
                                    val typeName = when (safeDevice.TypeID) {
                                        1 -> "Fire Alarm"
                                        2, 3 -> "LED Light"
                                        else -> ""
                                    }
                                    AlertDialog(
                                        onDismissRequest = { showDialog = false },
                                        title = { Text("Thông tin thiết bị") },
                                        text = {
                                            Column {
                                                Text("ID Thiết bị: ${safeDevice.DeviceID}")
                                                Text("Tên thiết bị: ${safeDevice.Name}")
                                                Text("Loại thiết bị: $typeName")
                                            }
                                        },
                                        confirmButton = {
                                            TextButton(onClick = { showDialog = false }) {
                                                Text("Đóng")
                                            }
                                        }
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

                                // Sử dụng hàm InfoRow để hiển thị thông tin
                                InfoRow(
                                    label = "Khí gas:",
                                    value = "$smokeLevel",
                                    unit = "ppm",
                                    stateColor = when {
                                        smokeLevel > 50 -> Color.Red
                                        smokeLevel < 0 -> Color.Yellow
                                        else -> Color.Green
                                    },
                                    stateText = when {
                                        smokeLevel > 50 -> "!"
                                        smokeLevel < 0 -> "?"
                                        else -> "✓"
                                    }
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                // TODO: Thêm Slider bên nhánh lamp detail
                                EdgeToEdgeSlider(
                                    value         = gasSliderValue,
                                    onValueChange = { gasSliderValue = it },
                                    activeTrackColor = Color.Red,
                                    inactiveTrackColor = Color.Red.copy(alpha = 0.3f),
                                    thumbColor = Color.Red,
                                    thumbBorderColor = Color.DarkGray,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                InfoRow(
                                    label = "Nhiệt độ:",
                                    value = "$temperature",
                                    unit = "°C",
                                    stateColor = when {
                                        temperature > 40 -> Color.Red
                                        temperature < 0 -> Color.Yellow
                                        else -> Color.Green
                                    },
                                    stateText = when {
                                        temperature > 40 -> "!"
                                        temperature < 0 -> "?"
                                        else -> "✓"
                                    }
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                // TODO: Thêm Slider bên nhánh lamp detail
                                EdgeToEdgeSlider(
                                    value         = tempSliderValue,
                                    onValueChange = { tempSliderValue = it },
                                    activeTrackColor = Color.Red,
                                    inactiveTrackColor = Color.Red.copy(alpha = 0.3f),
                                    thumbColor = Color.Red,
                                    thumbBorderColor = Color.DarkGray,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                InfoRow(
                                    label = "Độ ẩm:",
                                    value = "$coLevel",
                                    unit = "%",
                                    stateColor = when {
                                        coLevel > 40 -> Color.Red
                                        coLevel < 0 -> Color.Yellow
                                        else -> Color.Green
                                    },
                                    stateText = when {
                                        coLevel > 40 -> "!"
                                        coLevel < 0 -> "?"
                                        else -> "✓"
                                    }

                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                // TODO: Thêm Slider bên nhánh lamp detail
                                EdgeToEdgeSlider(
                                    value         = humiditySliderValue,
                                    onValueChange = { humiditySliderValue = it },
                                    activeTrackColor = Color.Red,
                                    inactiveTrackColor = Color.Red.copy(alpha = 0.3f),
                                    thumbColor = Color.Red,
                                    thumbBorderColor = Color.DarkGray,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            /* ------------------ LAYOUT NÚT HÀNH ĐỘNG ------------------ */
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                /* ===== HÀNG 1 ===== */
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    /* KHÓA THIẾT BỊ – có dialog */
                                    ActionButtonWithFeedback(
                                        label  = "Khóa thiết bị",
                                        onAction = { onS, onE ->
                                            pendingOnSuccess = onS
                                            pendingOnError   = onE
                                            confirmTitle     = "Khóa thiết bị"
                                            confirmMessage   = "Bạn có chắc muốn khoá thiết bị này?"
                                            pendingAction    = DeviceAction.LOCK
                                            showConfirm      = true
                                        },
                                        style  = HCButtonStyle.PRIMARY,
                                        height = 62.dp,
                                        textSize = 20.sp,
                                        modifier = Modifier.weight(1f),
                                        isLoadingFromParent = loadingAction == DeviceAction.LOCK
                                    )

                                    /* GỠ KẾT NỐI – có dialog */
                                    ActionButtonWithFeedback(
                                        label  = "Gỡ kết nối",
                                        onAction = { onS, onE ->
                                            pendingOnSuccess = onS
                                            pendingOnError   = onE
                                            confirmTitle     = "Gỡ kết nối"
                                            confirmMessage   = "Bạn muốn gỡ kết nối!"
                                            pendingAction    = DeviceAction.UNLINK
                                            showConfirm      = true
                                        },
                                        style  = HCButtonStyle.PRIMARY,
                                        height = 62.dp,
                                        textSize = 20.sp,
                                        modifier = Modifier.weight(1f),
                                        isLoadingFromParent = loadingAction == DeviceAction.UNLINK
                                    )
                                }

                                Spacer(Modifier.height(12.dp))

                                /* ===== HÀNG 2 ===== */
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    /* CHIA SẺ QUYỀN – KHÔNG dialog, quay ngay */
                                    ActionButtonWithFeedback(
                                        label  = "Chia sẻ quyền",
                                        onAction = { onS, _ ->
                                            loadingAction = DeviceAction.SHARE          // bật spinner
                                            scope.launch {
                                                // Giả lập xử lý
                                                delay(1000)
                                                onS("Đã chia sẻ")
                                                loadingAction = null                    // tắt spinner
                                            }
                                        },
                                        style  = HCButtonStyle.PRIMARY,
                                        height = 62.dp,
                                        textSize = 20.sp,
                                        modifier = Modifier.weight(1f),
                                        isLoadingFromParent = loadingAction == DeviceAction.SHARE
                                    )

                                    /* RESET THIẾT BỊ – có dialog */
                                    ActionButtonWithFeedback(
                                        label  = "Reset thiết bị",
                                        onAction = { onS, onE ->
                                            pendingOnSuccess = onS
                                            pendingOnError   = onE
                                            confirmTitle     = "Reset thiết bị"
                                            confirmMessage   = "Bạn muốn reset thiết bị này!"
                                            pendingAction    = DeviceAction.RESET
                                            showConfirm      = true
                                        },
                                        style  = HCButtonStyle.PRIMARY,
                                        height = 62.dp,
                                        textSize = 20.sp,
                                        modifier = Modifier.weight(1f),
                                        isLoadingFromParent = loadingAction == DeviceAction.RESET
                                    )
                                }

                                Spacer(Modifier.height(12.dp))

                                /* ===== HÀNG 3 ===== */
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    /* CHUYỂN QUYỀN SỞ HỮU – KHÔNG dialog */
                                    ActionButtonWithFeedback(
                                        label  = "Chuyển quyền sở hữu",
                                        onAction = { onS, _ ->
                                            loadingAction = DeviceAction.TRANSFER
                                            scope.launch {
                                                delay(1000)
                                                onS("Đã chuyển")
                                                loadingAction = null
                                            }
                                        },
                                        style  = HCButtonStyle.PRIMARY,
                                        height = 62.dp,
                                        textSize = 20.sp,
                                        modifier = Modifier.weight(1f),
                                        isLoadingFromParent = loadingAction == DeviceAction.TRANSFER
                                    )

                                    /* XEM PHIÊN BẢN – KHÔNG dialog */
                                    ActionButtonWithFeedback(
                                        label  = "Xem phiên bản",
                                        onAction = { onS, _ ->
                                            loadingAction = DeviceAction.VERSION
                                            scope.launch {
                                                delay(1000)
                                                onS("v1.0")
                                                loadingAction = null
                                            }
                                        },
                                        style  = HCButtonStyle.PRIMARY,
                                        height = 62.dp,
                                        textSize = 20.sp,
                                        modifier = Modifier.weight(1f),
                                        isLoadingFromParent = loadingAction == DeviceAction.VERSION
                                    )
                                }

                                Spacer(Modifier.height(16.dp))

                                /* ===== NÚT CUỐI (BÁO MẤT) ===== */
                                ActionButtonWithFeedback(
                                    label  = "Báo mất thiết bị",
                                    onAction = { onS, _ ->
                                        loadingAction = DeviceAction.REPORT_LOST
                                        scope.launch {
                                            delay(1000)
                                            onS("Đã báo mất")
                                            loadingAction = null
                                        }
                                    },
                                    style  = HCButtonStyle.PRIMARY,
                                    height = 62.dp,
                                    textSize = 20.sp,
                                    modifier = Modifier.fillMaxWidth(0.8f),
                                    isLoadingFromParent = loadingAction == DeviceAction.REPORT_LOST
                                )
                            }

                            // WarningDialog xác nhận
                            if (showConfirm) {
                                WarningDialog(
                                    title       = confirmTitle,
                                    text        = confirmMessage,
                                    confirmText = "Đồng ý",
                                    dismissText = "Huỷ",
                                    onConfirm = {
                                        showConfirm = false

                                        /* Bật spinner đúng nút */
                                        loadingAction = pendingAction
//                                        val act = pendingAction          // copy ra để dùng trong coroutine
                                        pendingAction = null

                                        scope.launch {
                                            delay(1000)                  // gọi API thật tại đây
                                            val ok = true
                                            if (ok)  pendingOnSuccess?.invoke("Thành công!")
                                            else     pendingOnError?.invoke("Thất bại!")

                                            loadingAction = null         // tắt spinner
                                        }
                                    },
                                    onDismiss = {
                                        showConfirm   = false
                                        pendingAction = null
                                    }
                                )
                            }

                        }
                    }
                }
            }
        )
    }
}

/* ---------- PREVIEWS ---------- */
@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    name = "FireAlarmDetail – Phone"
)

@Composable
fun FireAlarmDetailPhonePreview() {
    IoTHomeConnectAppTheme {
        FireAlarmDetailScreen(
            navController = rememberNavController(),
//            deviceID = 0            // dummy id
        )
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
