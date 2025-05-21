package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.sns.homeconnect_v2.R
import com.sns.homeconnect_v2.data.remote.dto.request.AttributeRequest
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.presentation.component.CustomSwitch
import com.sns.homeconnect_v2.presentation.component.DayPicker
import com.sns.homeconnect_v2.presentation.component.EdgeToEdgeSlider
import com.sns.homeconnect_v2.presentation.component.EndlessRollingPadlockTimePicker
import com.sns.homeconnect_v2.presentation.component.FancyColorSlider
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
import kotlin.math.roundToInt

/**
 * Đại diện cho Màn hình Chi tiết Thiết bị (DeviceDetailScreen).
 *
 * -----------------------------------------
 * Tác giả: Nguyễn Thanh Sang
 * Ngày: 6/12/2024
 * Cập nhật lần cuối: 10/12/2024
 * -----------------------------------------
 *
 * @param navController NavController để điều hướng giữa các màn hình.
 *
 * @return Một Scaffold đại diện cho màn hình chi tiết thiết bị.
 *
 * -----------------------------------------
 * Cập nhật bởi: Phạm Anh Tuấn
 * Ngày: 30/12/2024
 * -----------------------------------------
 * Điều chỉnh giao diện người dùng.
 * ---------------------------------------
 *
 * Cập nhật bởi: Nguyễn Thanh Sang
 * Ngày: 31/12/2024
 * ---------------------
 * Thêm chức năng điều hướng.
 *
 * -----------------------------------------
 * Cập nhật bởi: Nguyễn Thanh Sang
 * Ngày: 20/05/2025
 * ---------------------
 * Thêm các Nút Hành động.
 */

@Composable
fun DeviceDetailScreen(
    navController: NavHostController,
//    infoDevice: DeviceResponse?,
//    viewModel: DeviceDetailViewModel = hiltViewModel()
) {
    var rowWidth by remember { mutableIntStateOf(0) }
//    var selectedTimeBegin by remember { mutableStateOf("12:00 AM") }
//    var selectedTimeEnd by remember { mutableStateOf("12:00 AM") }
//    var showDialogTimePickerBegin by remember { mutableStateOf(false) }
//    var showDialogTimePickerEnd by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isCheck by remember { mutableStateOf(false) }
    var toggleDevice by remember { mutableStateOf<ToggleResponse?>(null) }
    var sliderValue by remember { mutableFloatStateOf(128f) }   // 0‥255

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

    var attribute by remember {
        mutableStateOf(
            AttributeRequest(
                brightness = 80,
                color = "#ffffff"
            )
        )
    }

    var safeDevice =
//        infoDevice ?:
        DeviceResponse(
            DeviceID = 0,
            TypeID = 0,
            Name = "Dining Room",
            PowerStatus = false,
            SpaceID = 0,
            Attribute = ""
        )

    Log.e("safeDevice", safeDevice.toString())
    LaunchedEffect(toggleDevice) {
        safeDevice =
//            infoDevice ?:
            DeviceResponse(
                DeviceID = 0,
                TypeID = 0,
                Name = "Dining room",
                PowerStatus = false,
                SpaceID = 0,
                Attribute = ""
            )
    }

    LaunchedEffect(safeDevice.Attribute) {
        val attributeJson = if (safeDevice.Attribute.isEmpty()) {
            """{"brightness":80, "color":"#ffffff"}""" // Giá trị mặc định
        } else {
            safeDevice.Attribute
        }

        Log.e("attributeJson", attributeJson)

        val gson = Gson()
        attribute = gson.fromJson(attributeJson, AttributeRequest::class.java)

        Log.e("attributeJson", attribute.toString())
    }

    var powerStatus by remember { mutableStateOf(false) }

    LaunchedEffect(safeDevice) {
        powerStatus = safeDevice.PowerStatus
    }

    Log.e("powerStatus", powerStatus.toString())
    // Khởi tạo toggle
//    var toggle by remember {
//        mutableStateOf(ToggleRequest(powerStatus = powerStatus))
//    }

//    val attributeState by viewModel.attributeState.collectAsState()

//    when (attributeState) {
//        is AttributeState.Error -> {
//            Log.e("Error", (attributeState as AttributeState.Error).error)
//        }
//
//        is AttributeState.Success -> {
//            val successState = attributeState as AttributeState.Success
//            safeDevice = successState.device
//            Log.d("Attribute Device", (attributeState as AttributeState.Success).message)
//        }
//
//        else -> {
//            /* Do nothing */
//        }
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

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
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
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    item {
                        ColoredCornerBox(
                            cornerRadius = 40.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize() // Chiếm toàn bộ kích thước của Box
                            ) {
                                // Phần đầu tiên: Hiển thị thông tin "Dining Room"
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp
                                    ) // Canh lề trên và phải
                                ) {
                                    // Cột chứa các thông tin của phòng
                                    Column(
                                        modifier = Modifier
                                            .padding(
                                                end = 12.dp
                                            ) // Canh lề hai bên
                                            .fillMaxWidth() // Chiều rộng đầy đủ
                                            .background(color = colorScheme.primary) // Nền màu xanh dương
                                            .weight(0.2f), // Chiếm 20% trọng lượng của Row
                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.SpaceBetween // Các thành phần cách đều nhau
                                    ) {
                                        Text(
                                            text = safeDevice.Name,
                                            color = colorScheme.onPrimary, // Màu chữ trắng
                                            lineHeight = 32.sp,
                                            fontSize = 30.sp
                                        ) // Tiêu đề
                                        Spacer(modifier = Modifier.height(4.dp)) // Khoảng cách giữa các thành phần

                                        CustomSwitch(isCheck = isCheck, onCheckedChange = { isCheck = it })
                                        // Switch bật/tắt đèn

                                        // Hiển thị phần trăm độ sáng
                                        Row(
                                            modifier = Modifier.fillMaxWidth(), // Chiều rộng đầy đủ
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.Bottom // Canh các thành phần theo đáy
                                        ) {
                                            Text(
                                                text = ((sliderValue * 100 / 255).roundToInt()).toString(),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 50.sp,
                                                color = colorScheme.onPrimary
                                            ) // Số phần trăm
                                            Text(
                                                "%",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 25.sp,
                                                modifier = Modifier.offset(y = (-8).dp), // Đẩy lên trên một chút
                                                color = colorScheme.onPrimary
                                            )
                                        }

                                        Text(
                                            "Độ sáng",
                                            color = colorScheme.onPrimary,
                                            fontSize = 20.sp
                                        ) // Nhãn cho độ sáng
                                        Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách dưới cùng
                                    }

                                    // Hình ảnh đèn
                                    Image(
                                        painter = painterResource(id = R.drawable.lamp),
                                        modifier = Modifier.size(200.dp), // Kích thước hình ảnh
                                        contentDescription = "",// Mô tả cho hình ảnh
                                        colorFilter = ColorFilter.tint(colorScheme.onPrimary) // Màu chữ trắng
                                    )
                                }

                                // Thanh trượt để điều chỉnh cường độ sáng
                                Column(
                                    modifier = Modifier
                                        .width(rowWidth.dp)
                                        .background(color = colorScheme.primary) // Nền màu xanh dương
                                        .clickable(enabled = false) {}
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Text(
                                        "Cường độ",
                                        color = colorScheme.onPrimary
                                    ) // Tiêu đề cường độ sáng

                                    EdgeToEdgeSlider(
                                        value = sliderValue,
                                        onValueChange = { sliderValue = it },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Đường kẻ phân cách mỏng
                                Box(
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp)
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(colorScheme.onPrimary) // Màu xám nhạt cho đường kẻ
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                FancyColorSlider(
                                    attribute = attribute,
                                    onColorChange = {}
                                )
                            }
                        }

                        InvertedCornerHeader(
                            backgroundColor = colorScheme.surface,
                            overlayColor = colorScheme.primary
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth() // Đảm bảo Row chiếm toàn bộ chiều rộng
                                    .padding(
                                        horizontal = 16.dp, vertical = 8.dp
                                    ), // Khoảng cách bên trong Row
                                horizontalArrangement = Arrangement.SpaceBetween, // Đẩy các phần tử ra hai bên
                                verticalAlignment = Alignment.CenterVertically // Căn giữa theo chiều dọc
                            ) {
                                // Nội dung bên phải (Icon trong Box)
                                /*  Nút Info  */
                                IconButton(
                                    onClick = { showDialog = true },
                                    modifier = Modifier.size(32.dp)   // 32 dp để dễ chạm hơn 24 dp
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        tint = colorScheme.primary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                /*  Nút Wi-Fi  */
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

                                if (showDialog) {
                                    fun getIconForType(typeId: Int): String {
                                        return when (typeId) {
                                            1 -> "Fire Alarm" // Light
                                            2, 3 -> "LED Light" // Fire
                                            else -> ""         // Biểu tượng mặc định
                                        }
                                    }

                                    AlertDialog(
                                        onDismissRequest = {
                                            showDialog = false
                                        }, // Đóng Dialog khi chạm ngoài
                                        title = { Text(text = "Thông tin thiết bị") },
                                        text = {
                                            Column {
                                                Text("ID Thiết bị: ${safeDevice.DeviceID}")
                                                Text("Tên thiết bị: ${safeDevice.Name}")
                                                Text(
                                                    "Loại thiết bị: ${
                                                        getIconForType(
                                                            safeDevice.TypeID
                                                        )
                                                    }"
                                                )
                                            }
                                        },
                                        confirmButton = {
                                            Button(onClick = {
                                                showDialog = false
                                            }) {
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
                                .wrapContentWidth()
                                .onSizeChanged { size ->
                                    rowWidth = size.width // Lấy kích thước của Row
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
//                            Text(
//                                text = "Select Time",
//                                fontSize = 24.sp,
//                                color = colorScheme.onBackground,
//                                modifier = Modifier.padding(bottom = 16.dp)
//                            )
//                            Row(
//                                horizontalArrangement = Arrangement.Center,
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Text(
//                                    text = selectedTimeBegin,
//                                    fontSize = 24.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    color = colorScheme.onBackground,
//                                    modifier = Modifier
//                                        .clickable { showDialogTimePickerBegin = true }
//                                )
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text(
//                                    text = "To",
//                                    fontSize = 24.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    color = colorScheme.onBackground,
//                                )
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text(
//                                    text = selectedTimeEnd,
//                                    fontSize = 24.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    color = colorScheme.onBackground,
//                                    modifier = Modifier
//                                        .clickable { showDialogTimePickerEnd = true }
//                                )
//                            }
//
//                            if (showDialogTimePickerBegin) {
//                                Dialog(onDismissRequest = { showDialogTimePickerBegin = false }) {
//                                    Box(
//                                        modifier = Modifier
//                                            .background(
//                                                colorScheme.onBackground,
//                                                shape = RoundedCornerShape(12.dp)
//                                            )
//                                            .padding(16.dp)
//                                            .wrapContentSize()
//                                    ) {
//                                        EndlessRollingPadlockTimePicker { hour, minute, amPm ->
//                                            selectedTimeBegin =
//                                                "$hour:${minute.toString().padStart(2, '0')} $amPm"
//                                            showDialog = false // Đóng dialog sau khi chọn xong
//                                        }
//                                    }
//                                }
//                            }
//                            if (showDialogTimePickerEnd) {
//                                Dialog(onDismissRequest = { showDialogTimePickerEnd = false }) {
//                                    Box(
//                                        modifier = Modifier
//                                            .background(
//                                                colorScheme.background,
//                                                shape = RoundedCornerShape(12.dp)
//                                            )
//                                            .padding(16.dp)
//                                            .wrapContentSize()
//                                    ) {
//                                        EndlessRollingPadlockTimePicker { hour, minute, amPm ->
//                                            selectedTimeEnd =
//                                                "$hour:${minute.toString().padStart(2, '0')} $amPm"
//                                            showDialog = false // Đóng dialog sau khi chọn xong
//                                        }
//                                    }
//                                }
//                            }
//                            DayPicker()

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

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "DeviceDetailScreen Preview - Phone")
@Composable
fun DeviceDetailScreenPreview() {
    IoTHomeConnectAppTheme {
        DeviceDetailScreen(navController = rememberNavController())
    }
}

@Composable
fun DeviceDetailTabletScreen(
    navController: NavHostController,
//    infoDevice: DeviceResponse?,
//    viewModel: DeviceDetailViewModel = hiltViewModel()
) {
    var rowWidth by remember { mutableStateOf<Int?>(null) }
    var selectedTimeBegin by remember { mutableStateOf("12:00 AM") }
    var selectedTimeEnd by remember { mutableStateOf("12:00 AM") }
    var showDialogTimePickerBegin by remember { mutableStateOf(false) }
    var showDialogTimePickerEnd by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val switchState by remember { mutableStateOf(true) }

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

    var attribute by remember {
        mutableStateOf(
            AttributeRequest(
                brightness = 0,
                color = "#000000"
            )
        )
    }

    var safeDevice =
//        infoDevice ?:
        DeviceResponse(
            DeviceID = 0,
            TypeID = 0,
            Name = "",
            PowerStatus = false,
            SpaceID = 0,
            Attribute = ""
        )

    Log.e("safeDevice", safeDevice.toString())

    LaunchedEffect(toggleDevice) {
        safeDevice =
//            infoDevice ?:
            DeviceResponse(
                DeviceID = 0,
                TypeID = 0,
                Name = "",
                PowerStatus = false,
                SpaceID = 0,
                Attribute = ""
            )
    }

    LaunchedEffect(safeDevice.Attribute) {
        val attributeJson = if (safeDevice.Attribute.isEmpty()) {
            """{"brightness":0, "color":"#000000"}""" // Giá trị mặc định
        } else {
            safeDevice.Attribute
        }

        Log.e("attributeJson", attributeJson)

        val gson = Gson()
        attribute = gson.fromJson(attributeJson, AttributeRequest::class.java)

        Log.e("attributeJson", attribute.toString())
    }


    var powerStatus by remember { mutableStateOf(false) }

    LaunchedEffect(safeDevice) {
        powerStatus = safeDevice.PowerStatus
    }

    // Khởi tạo toggle
    var toggle by remember {
        mutableStateOf(ToggleRequest(powerStatus = powerStatus))
    }

//    val attributeState by viewModel.attributeState.collectAsState()

//    when (attributeState) {
//        is AttributeState.Error -> {
//            Log.e("Error", (attributeState as AttributeState.Error).error)
//        }
//
//        is AttributeState.Success -> {
//            Log.d("Attribute Device", (attributeState as AttributeState.Success).message)
//        }
//
//        else -> {
//            /* Do nothing */
//        }
//    }

//    val unlinkState by viewModel.unlinkState.collectAsState()

//    when (unlinkState) {
//        is UnlinkState.Error -> {
//            Log.e("Error", (unlinkState as UnlinkState.Error).error)
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

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
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

                var showAlertDialog by remember { mutableStateOf(false) }
                if (showAlertDialog) {
                    WarningDialog(
                        title = "Gỡ kết nối",
                        text = "Bạn có chắc chắn muốn gỡ kết nối thiết bị này không?",
                        onConfirm = {
//                            viewModel.unlinkDevice(safeDevice.DeviceID)
                            showAlertDialog = false
                            navController.popBackStack()
                        },
                        onDismiss = {
                            showAlertDialog = false
                        }
                    )
                }
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize() // Đảm bảo chiếm toàn bộ không gian
                            .fillMaxHeight()
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
                        ) {
                            Column {
                                // Hộp màu xanh dương
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .background(
                                            color = colorScheme.primary,
                                            shape = RoundedCornerShape(bottomStart = 40.dp)
                                        )
                                        .zIndex(1f)
                                ) {
                                    // Box chính bao quanh toàn bộ giao diện
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize() // Chiếm toàn bộ màn hình
                                            .clip(RoundedCornerShape(bottomStart = 40.dp)) // Bo góc phía dưới bên trái
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize() // Chiếm toàn bộ kích thước của Box
                                        ) {
                                            // Phần Header: Hiển thị thông tin phòng và đèn
                                            Row(
                                                modifier = Modifier.padding(
                                                    top = 8.dp,
                                                    end = 12.dp
                                                ) // Canh lề trên và phải
                                            ) {
                                                // Cột bên trái: Thông tin về phòng và độ sáng
                                                Column(
                                                    modifier = Modifier
                                                        .padding(
                                                            start = 12.dp,
                                                            end = 12.dp
                                                        ) // Canh lề trái và phải
                                                        .fillMaxWidth() // Chiều rộng đầy đủ
                                                        .background(color = colorScheme.primary) // Nền xanh
                                                        .weight(0.2f), // Trọng lượng chiếm 20% của Row
                                                    horizontalAlignment = Alignment.Start,
                                                    verticalArrangement = Arrangement.SpaceBetween // Phân bố các thành phần cách đều nhau
                                                ) {
                                                    Text(
                                                        text = safeDevice.Name,
                                                        color = colorScheme.onPrimary,
                                                        lineHeight = 32.sp,
                                                        fontSize = 30.sp
                                                    ) // Tiêu đề
                                                    Spacer(modifier = Modifier.height(4.dp)) // Khoảng cách

                                                    // Switch bật/tắt đèn với icon
                                                    Switch(
                                                        checked = powerStatus,
                                                        onCheckedChange = {
                                                            //Todo: Xử lý khi tắt mở
                                                            powerStatus = !powerStatus
                                                            toggle =
                                                                ToggleRequest(powerStatus = powerStatus) // Cập nhật toggl
//                                                            viewModel.toggleDevice(
//                                                                safeDevice.DeviceID,
//                                                                toggle
//                                                            )

                                                        }, // Hàm xử lý khi thay đổi trạng thái (để trống)
                                                        thumbContent = {
                                                            Icon(
                                                                imageVector = if (switchState) Icons.Filled.Check else Icons.Filled.Close,
                                                                contentDescription = "On/Off Switch",
                                                                tint = if (switchState) colorScheme.onPrimary else colorScheme.onSecondary.copy(
                                                                    alpha = 0.8f
                                                                )
                                                            )
                                                        },
                                                        colors = SwitchDefaults.colors(
                                                            checkedThumbColor = colorScheme.primary,
                                                            checkedTrackColor = colorScheme.onPrimary,
                                                            uncheckedThumbColor = colorScheme.secondary,
                                                            uncheckedTrackColor = colorScheme.onSecondary.copy(
                                                                alpha = 0.8f
                                                            ),
                                                        )
                                                    )

                                                    // Hiển thị mức độ sáng (80%)
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.Start, // Căn trái
                                                        verticalAlignment = Alignment.Bottom // Căn dưới
                                                    ) {
                                                        Text(
                                                            text = attribute.brightness.toString(),
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 50.sp,
                                                            color = colorScheme.onPrimary
                                                        ) // Số phần trăm
                                                        Text(
                                                            "%",
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 25.sp,
                                                            modifier = Modifier.offset(y = (-8).dp), // Đẩy chữ % lên trên
                                                            color = colorScheme.onPrimary
                                                        )
                                                    }
                                                    Text(
                                                        "Độ sanng",
                                                        color = colorScheme.onPrimary,
                                                        fontSize = 20.sp
                                                    ) // Nhãn Brightness
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                }

                                                // Hình ảnh chiếc đèn ở cột bên phải
                                                Image(
                                                    painter = painterResource(id = R.drawable.lamp),
                                                    modifier = Modifier.size(150.dp), // Kích thước hình
                                                    contentDescription = "", // Không có mô tả
                                                    colorFilter = ColorFilter.tint(colorScheme.onPrimary) // Màu chữ trắng
                                                )
                                            }

                                            // Thanh Slider điều chỉnh cường độ sáng và các ngày trong tuần
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(start = 12.dp, end = 12.dp)
                                                    .background(color = colorScheme.primary) // Nền xanh
                                            ) {
                                                Text(
                                                    "Cường độ",
                                                    color = colorScheme.onPrimary,
                                                ) // Tiêu đề cường độ

                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    // Hàng chứa thanh trượt và biểu tượng bóng đèn
                                                    Row(
                                                        modifier = Modifier
                                                            .width(500.dp)
                                                            .onSizeChanged { size ->
                                                                rowWidth = size.width
                                                            },
                                                        horizontalArrangement = Arrangement.Center, // Căn cách đều các thành phần
                                                        verticalAlignment = Alignment.CenterVertically // Căn giữa theo chiều dọc
                                                    ) {
                                                        // Hình bóng đèn tắt
                                                        Image(
                                                            painter = painterResource(id = R.drawable.bulboff),
                                                            modifier = Modifier
                                                                .padding(end = 8.dp)
                                                                .size(24.dp),
                                                            contentDescription = ""
                                                        )

                                                        // Thanh trượt (Slider) giá trị 80
                                                        Slider(
                                                            value = attribute.brightness.toFloat(),
                                                            onValueChange = {

                                                            }, // Thanh trượt giá trị mặc định là 80
                                                            onValueChangeFinished = {
                                                                // Gửi dữ liệu lên server khi người dùng dừng thao tác kéo thanh trượt
                                                            },
                                                            steps = 10,
                                                            valueRange = 0f..255f,
                                                            modifier = Modifier.fillMaxWidth(),
                                                            colors = SliderDefaults.colors(
                                                                thumbColor = colorScheme.onPrimary,
                                                                activeTrackColor = colorScheme.onPrimary,
                                                                activeTickColor = colorScheme.secondary,
                                                                inactiveTrackColor = colorScheme.secondary,
                                                                inactiveTickColor = colorScheme.onSecondary
                                                            )
                                                        )

                                                        // Hình bóng đèn bật
                                                        Image(
                                                            painter = painterResource(id = R.drawable.bulb),
                                                            modifier = Modifier
                                                                .padding(start = 8.dp)
                                                                .size(24.dp),
                                                            contentDescription = ""
                                                        )
                                                    }
                                                }
                                            }

                                            // Dòng kẻ phân cách
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Box(
                                                modifier = Modifier
                                                    .padding(start = 12.dp, end = 12.dp)
                                                    .fillMaxWidth()
                                                    .height(1.dp)
                                                    .background(colorScheme.onPrimary)
                                            )

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .padding(
                                                            top = 12.dp,
                                                            start = 12.dp,
                                                            end = 12.dp
                                                        )
                                                        .width(500.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    FancyColorSlider(
                                                        attribute = attribute,
                                                        onColorChange = {}
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                // Hộp màu xanh lá cây với góc lõm
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                ) {

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
                                                shape = RoundedCornerShape(topEndPercent = 50)
                                            )
                                            .width(50.dp)
                                            .height(50.dp)
                                            .zIndex(2f) // Z-index cao hơn
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(topEndPercent = 100)) // Clip nội dung ScrollableTabRow
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth() // Đảm bảo Row chiếm toàn bộ chiều rộng
                                                    .padding(
                                                        top = 12.dp,
                                                        start = 12.dp,
                                                        end = 8.dp
                                                    ), // Khoảng cách bên trong Row
                                                horizontalArrangement = Arrangement.SpaceBetween, // Đẩy các phần tử ra hai bên
                                                verticalAlignment = Alignment.CenterVertically // Căn giữa theo chiều dọc
                                            ) {
                                                // Nội dung bên phải (Icon trong Box)
                                                Button(
                                                    onClick = {
                                                        showDialog = true
                                                    },
                                                    modifier = Modifier
                                                        .size(36.dp), // Kích thước tổng thể của Button
                                                    shape = CircleShape, // Đảm bảo Button có dạng hình tròn
                                                    contentPadding = PaddingValues(0.dp), // Loại bỏ padding mặc định
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = colorScheme.onPrimary,
                                                        contentColor = colorScheme.primary
                                                    )
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Info,
                                                        contentDescription = "Info",
                                                        modifier = Modifier.size(36.dp), // Kích thước của Icon,
                                                        tint = colorScheme.primary
                                                    )
                                                }
                                                Button(
                                                    onClick = {
                                                        navController.navigate(Screens.AccessPoint.route + "?id=${safeDevice.DeviceID}&name=${safeDevice.Name}")
                                                    },
                                                    modifier = Modifier
                                                        .size(36.dp), // Kích thước tổng thể của Button
                                                    shape = CircleShape, // Đảm bảo Button có dạng hình tròn
                                                    contentPadding = PaddingValues(0.dp), // Loại bỏ padding mặc định
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = colorScheme.onPrimary,
                                                        contentColor = colorScheme.primary
                                                    )
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Wifi,
                                                        contentDescription = "Wifi",
                                                        modifier = Modifier.size(36.dp),// Kích thước của Icon
                                                        tint = colorScheme.primary
                                                    )
                                                }
                                                if (showDialog) {
                                                    fun getIconForType(typeId: Int): String {
                                                        return when (typeId) {
                                                            1 -> "Fire Alarm" // Light
                                                            2 -> "LED Light" // Fire
                                                            else -> ""         // Biểu tượng mặc định
                                                        }
                                                    }
                                                    AlertDialog(
                                                        onDismissRequest = {
                                                            showDialog = false
                                                        }, // Đóng Dialog khi chạm ngoài
                                                        title = { Text(text = "Thông tin thiết bị") },
                                                        text = {
                                                            Column {
                                                                Text("ID Thiết bị: ${safeDevice.DeviceID}")
                                                                Text("Tên thiết bị: ${safeDevice.Name}")
                                                                Text(
                                                                    "Loại thiết bị: ${
                                                                        getIconForType(
                                                                            safeDevice.TypeID
                                                                        )
                                                                    }"
                                                                )
                                                            }
                                                        },
                                                        confirmButton = {
                                                            Button(onClick = {
                                                                showDialog = false
                                                            }) {
                                                                Text("Đóng")
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(500.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Select Time",
                                    fontSize = 24.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedTimeBegin,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        modifier = Modifier
                                            .clickable { showDialogTimePickerBegin = true }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "To",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = selectedTimeEnd,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        modifier = Modifier
                                            .clickable { showDialogTimePickerEnd = true }
                                    )
                                }

                                if (showDialogTimePickerBegin) {
                                    Dialog(onDismissRequest = {
                                        showDialogTimePickerBegin = false
                                    }) {
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    Color.Black,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .padding(16.dp)
                                                .wrapContentSize()
                                        ) {
                                            EndlessRollingPadlockTimePicker { hour, minute, amPm ->
                                                selectedTimeBegin = "$hour:${
                                                    minute.toString().padStart(2, '0')
                                                } $amPm"
                                                showDialog = false // Đóng dialog sau khi chọn xong
                                            }
                                        }
                                    }
                                }
                                if (showDialogTimePickerEnd) {
                                    Dialog(onDismissRequest = { showDialogTimePickerEnd = false }) {
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    Color.Black,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .padding(16.dp)
                                                .wrapContentSize()
                                        ) {
                                            EndlessRollingPadlockTimePicker { hour, minute, amPm ->
                                                selectedTimeEnd = "$hour:${
                                                    minute.toString().padStart(2, '0')
                                                } $amPm"
                                                showDialog = false // Đóng dialog sau khi chọn xong
                                            }
                                        }
                                    }
                                }
                                DayPicker()
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .then(Modifier.width(rowWidth!!.dp)),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        8.dp,
                                        alignment = Alignment.CenterHorizontally
                                    ), // Khoảng cách giữa các nút
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            //Todo: Xử lý khi nhấn nút Lịch sử
                                            navController.navigate(Screens.ActivityHistory.route)
                                        },
                                        modifier = Modifier
                                            .weight(1f) // Chia đều không gian
                                            .width(200.dp)
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Text(
                                            text = "Lịch sử hoạt động",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            showAlertDialog = true
                                        },
                                        modifier = Modifier
                                            .weight(1f) // Chia đều không gian
                                            .width(200.dp)
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.error),
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Text(
                                            text = "Gỡ kết nối",
                                            fontWeight = FontWeight.Bold,
                                            color = colorScheme.onError
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .then(if (rowWidth != null) Modifier.width(rowWidth!!.dp) else Modifier.fillMaxWidth()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp), // Khoảng cách giữa các nút
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            navController.navigate(Screens.SharedUsers.route + "?id=${safeDevice.DeviceID}")
                                        },
                                        modifier = Modifier
                                            .weight(1f) // Chia đều không gian
                                            .width(300.dp)
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Text(
                                            text = "Chia sẻ quyền",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = colorScheme.onPrimary
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            showAlertDialog = true
                                        },
                                        modifier = Modifier
                                            .weight(1f) // Chia đều không gian
                                            .width(300.dp)
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Text(
                                            text = "Reset thiết bị",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.Black
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

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "DeviceDetailScreen Preview - Phone")
@Composable
fun DeviceDetailTabletScreenPreview() {
    IoTHomeConnectAppTheme {
        DeviceDetailTabletScreen(navController = rememberNavController())
    }
}