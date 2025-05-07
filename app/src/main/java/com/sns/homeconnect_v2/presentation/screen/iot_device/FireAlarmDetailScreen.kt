package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import android.app.Application
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.R
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.presentation.component.dialog.WarningDialog
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_fire.FireAlarmDetailViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_fire.GetInfoDeviceState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_fire.ToggleState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_fire.UnlinkState
import org.json.JSONObject

@Composable
fun FireAlarmDetailScreen (
    navController: NavHostController,
    deviceID: Int?,
    viewModel: FireAlarmDetailViewModel = hiltViewModel(),
) {
    var rowWidth by remember { mutableStateOf(0) }
    val smokeLevel by remember { mutableStateOf(20) }
    val  temperature by remember { mutableStateOf(50) }
    val  coLevel by remember { mutableStateOf(-1) }
    var showDialog by remember { mutableStateOf(false) }
    val  switchState by remember { mutableStateOf(true) }
    val statusList = listOf("Bình thường", "Báo động", "Lỗi")// Trạng thái
    val  status by remember { mutableStateOf(0) }
    val isTablet = isTablet(LocalContext.current)


    var infoDevice by remember { mutableStateOf<DeviceResponse?>(null) } // Lắng nghe danh sách thiết bị
    val infoDeviceState by viewModel.infoDeviceState.collectAsState()

    when(infoDeviceState){
        is GetInfoDeviceState.Error ->{
            Log.d("Error",  (infoDeviceState as GetInfoDeviceState.Error).error)
        }
        is GetInfoDeviceState.Success -> {
            infoDevice = (infoDeviceState as GetInfoDeviceState.Success).device
            Log.d("List Device", (infoDeviceState as GetInfoDeviceState.Success).device.toString())
        }
        else ->
        {
            /* Do nothing */
        }
    }

    LaunchedEffect(1) {
        viewModel.getInfoDevice(deviceID!!)
    }

    var toggleDevice by remember { mutableStateOf<ToggleResponse?>(null) } // Lắng nghe danh sách thiết bị
    val toggleDeviceState by viewModel.toggleState.collectAsState()

    when(toggleDeviceState){
        is ToggleState.Error ->{
            Log.e("Error", (toggleDeviceState as ToggleState.Error).error)
        }
        is ToggleState.Success -> {
            val successState = toggleDeviceState as ToggleState.Success
            toggleDevice = successState.toggle
            Log.e("toggle Device", toggleDevice.toString())
        }
        else -> {
            /* Do nothing */
        }
    }


    var safeDevice = infoDevice ?: DeviceResponse(
        DeviceID = 0,
        TypeID = 0,
        Name = "",
        PowerStatus = false,
        SpaceID = 0,
        Attribute = ""
    )

    Log.e("safeDevice", safeDevice.toString())



    LaunchedEffect(toggleDevice) {
        safeDevice = infoDevice ?: DeviceResponse(
            DeviceID = 0,
            TypeID = 0,
            Name = "",
            PowerStatus = false,
            SpaceID = 0,
            Attribute = ""
        )
    }

    var powerStatus by remember { mutableStateOf(false)}
    var toggle by remember {
        mutableStateOf(ToggleRequest(powerStatus = powerStatus))
    }
    LaunchedEffect(safeDevice) {
        powerStatus = safeDevice.PowerStatus
    }

    val unlinkState by viewModel.unlinkState.collectAsState()

    when(unlinkState){
        is UnlinkState.Error ->{
            Log.e("Error Unlink Device",  (unlinkState as UnlinkState.Error).error)
        }
        is UnlinkState.Success -> {
            Log.d("Unlink Device", (unlinkState as UnlinkState.Success).message)
        }
        else -> {
            /* Do nothing */
        }
    }


    var showAlertDialog by remember { mutableStateOf(false) }
    if (showAlertDialog) {
        WarningDialog(
            title = "Gỡ kết nối",
            text = "Bạn có chắc chắn muốn gỡ kết nối thiết bị này không?",
            onConfirm = {
                viewModel.unlinkDevice(safeDevice.DeviceID)
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
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
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(bottomStart = 40.dp)) // Làm bo tròn góc dưới bên trái của Box
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize() // Chiếm toàn bộ kích thước của Box
                                        ) {
                                            // Phần đầu tiên: Hiển thị thông tin "Dining Room"
                                            Row(
                                                modifier = Modifier.padding(
                                                    top = 8.dp,
                                                    end = 12.dp
                                                ) // Canh lề trên và phải
                                            ) {
                                                // Cột chứa các thông tin của phòng
                                                Column(
                                                    modifier = Modifier
                                                        .padding(
                                                            start = 12.dp,
                                                            end = 12.dp
                                                        ) // Canh lề hai bên
                                                        .fillMaxWidth() // Chiều rộng đầy đủ
                                                        .background(color = colorScheme.primary) // Nền màu xanh dương
                                                        .weight(0.2f), // Chiếm 20% của Row
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

                                                    // Switch bật/tắt đèn
                                                    Switch(
                                                        checked = powerStatus,
                                                        onCheckedChange = {
                                                            //Todo: Xử lý tắt mở thiết bị
                                                            powerStatus = !powerStatus
                                                            toggle = ToggleRequest(powerStatus = powerStatus) // Cập nhật toggl
                                                            viewModel.toggleDevice(safeDevice.DeviceID, toggle)
                                                        },
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
                                                    Text(
                                                        "Trạng thái hiện tại: ",
                                                        color = colorScheme.onPrimary,
                                                        fontSize = 16.sp
                                                    ) // Nhãn cho độ sáng
                                                    // Hiển thị phần trăm độ sáng
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(), // Chiều rộng đầy đủ
                                                        horizontalArrangement = Arrangement.Start,
                                                        verticalAlignment = Alignment.Bottom // Canh các thành phần theo đáy
                                                    ) {
                                                        Text(
                                                            statusList[status],
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 25.sp,
                                                            color = colorScheme.onPrimary
                                                        ) // Số phần trăm
                                                    }
                                                    Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách dưới cùng
                                                }

                                                SingleColorCircleWithDividers(selectedStatus = statusList[status], dividerCount = 12)
                                            }
                                            Spacer(modifier = Modifier.height(16.dp))
                                        }
                                    }
                                }
                                // Hộp màu xanh lá cây với góc lõm
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
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

                                    // Box màu xanh lá cây (ở trên)
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
                                                        .size(24.dp), // Kích thước tổng thể của Button
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
                                                        modifier = Modifier.size(24.dp), // Kích thước của Icon
                                                        tint = colorScheme.primary
                                                    )
                                                }
                                                Button(
                                                    onClick = {
                                                        navController.navigate(Screens.AccessPoint.route + "?id=${safeDevice.DeviceID}&name=${safeDevice.Name}")
                                                    },
                                                    modifier = Modifier
                                                        .size(24.dp), // Kích thước tổng thể của Button
                                                    shape = CircleShape, // Đảm bảo Button có dạng hình tròn
                                                    contentPadding = PaddingValues(0.dp), // Loại bỏ padding mặc định,
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = colorScheme.onPrimary,
                                                        contentColor = colorScheme.primary

                                                    )
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Wifi,
                                                        contentDescription = "Wifi",
                                                        modifier = Modifier.size(24.dp), // Kích thước của Icon
                                                        tint = colorScheme.primary

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
                                }
                            }
                        }

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

                                Slider(
                                    value = smokeLevel.toFloat(),
                                    onValueChange = {
                                        //Todo: Xử lý khi thay đổi giá trị
                                    }, // Thanh trượt giá trị mặc định là 80
                                    steps = 50,
                                    valueRange = 0f..1000f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = colorScheme.onPrimary,
                                        activeTrackColor = colorScheme.onPrimary,
                                        activeTickColor = colorScheme.onBackground,
                                        inactiveTrackColor = colorScheme.onBackground,
                                        inactiveTickColor = colorScheme.onSecondary
                                    )
                                )

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
                                Slider(
                                    value = temperature.toFloat(),
                                    onValueChange = {
                                        //Todo: Xử lý khi thay đổi giá trị
                                    }, // Thanh trượt giá trị mặc định là 80
                                    steps = 5,
                                    valueRange = 0f..100f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = colorScheme.onPrimary,
                                        activeTrackColor = colorScheme.onPrimary,
                                        activeTickColor = colorScheme.onBackground,
                                        inactiveTrackColor = colorScheme.onBackground,
                                        inactiveTickColor = colorScheme.onSecondary
                                    )
                                )
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
                                Slider(
                                    value = coLevel.toFloat(),
                                    onValueChange = {
                                        //Todo: Xử lý khi thay đổi giá trị
                                    }, // Thanh trượt giá trị mặc định là 80
                                    steps = 5,
                                    valueRange = 0f..100f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = colorScheme.onPrimary,
                                        activeTrackColor = colorScheme.onPrimary,
                                        activeTickColor = colorScheme.onBackground,
                                        inactiveTrackColor = colorScheme.onBackground,
                                        inactiveTickColor = colorScheme.onSecondary
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .then(Modifier.width(rowWidth.dp)),
                                horizontalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    alignment = Alignment.CenterHorizontally
                                ), // Khoảng cách giữa các nút
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        //Todo: Xử lý khi nhấn nút Lịch sử
                                        navController.navigate(Screens.ActivityHistory.createRoute(safeDevice.DeviceID))
                                    },
                                    modifier = Modifier
                                        .weight(0.5f) // Chia đều không gian
                                        .width(if (isTablet) 300.dp else 200.dp)
                                        .height(if (isTablet) 56.dp else 48.dp),
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
                                        .weight(0.5f) // Chia đều không gian
                                        .width(if (isTablet) 300.dp else 200.dp)
                                        .height(if (isTablet) 56.dp else 48.dp),
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
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun SingleColorCircleWithDividers(selectedStatus: String, dividerCount: Int) {
    Box(
        modifier = Modifier.size(200.dp),
        contentAlignment = Alignment.Center // Đảm bảo nội dung bên trong căn giữa
    ) {
        // Xác định màu dựa theo trạng thái
        val color = when (selectedStatus) {
            "Bình thường" -> Color.Green
            "Báo động" -> Color.Red
            "Lỗi" -> Color.Yellow
            else -> Color.Gray
        }
        Canvas(modifier = Modifier.size(200.dp)) {
            val radius = size.minDimension / 2 - 20f // Trừ bớt để tránh bị cắt
            val center = Offset(size.width / 2, size.height / 2)



            // Vẽ vòng tròn với màu trạng thái
            drawCircle(
                color = color,
                radius = radius,
                center = center,
                style = Stroke(width = 40f) // Độ dày của vòng tròn
            )

            // Vẽ các đường gạch phân cách
            val lineAngleStep = 360f / dividerCount
            for (i in 0 until dividerCount) {
                val angle = Math.toRadians((-90 + i * lineAngleStep).toDouble())
                val startX = center.x + (radius - 20f) * Math.cos(angle).toFloat()
                val startY = center.y + (radius - 20f) * Math.sin(angle).toFloat()
                val endX = center.x + (radius + 20f) * Math.cos(angle).toFloat()
                val endY = center.y + (radius + 20f) * Math.sin(angle).toFloat()

                drawLine(
                    color = Color.Black,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 4f
                )
            }
        }

        // Icon trung tâm (nếu cần)
        Image(
            painter = painterResource(id = R.drawable.fire),
            colorFilter = ColorFilter.tint(color),
            contentDescription = null,
            modifier = Modifier.size(100.dp) // Kích thước biểu tượng
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SegmentedCirclePreview() {
    val statusList = listOf("Bình thường", "Báo động", "Lỗi")
    SingleColorCircleWithDividers(selectedStatus = statusList[0], dividerCount = 12)
}

@Composable
// Hàm hiển thị từng hàng thông tin
fun InfoRow(label: String, value: String, unit: String, stateColor: Color, stateText: String) {
   IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Row(
            modifier = Modifier
                .wrapContentWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                color = colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.weight(2f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = unit,
                    fontSize = 12.sp,
                    modifier = Modifier.offset(y = 3.dp),
                    color = colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(stateColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stateText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// Thêm extension function để parse toggle log
fun String.parseToggleCommand(): Boolean? {
    return try {
        val command = JSONObject(this)
        if (command.has("command")) {
            val toggleCommand = command.getJSONObject("command")
            toggleCommand.getBoolean("powerStatus")
        } else {
            null
        }
    } catch (e: Exception) {
        Log.e("ParseLog", "Error parsing toggle command", e)
        null
    }
}

// Thêm data class để giữ giá trị sensor
data class SensorData(
    val gas: Int = 0,
    val temperature: Double = 0.0,
    val humidity: Int = 0
)

// Extension function để parse sensor data
fun String.parseSensorData(): SensorData? {
    return try {
        val parts = this.split("}")
        // Lấy phần JSON cuối cùng chứa dữ liệu sensor
        val sensorJson = parts.last { it.contains("sensorData") } + "}"
        val data = JSONObject(sensorJson)

        SensorData(
            gas = data.optInt("gas", 0),
            temperature = data.optDouble("temperature", 0.0),
            humidity = data.optInt("humidity", 0)
        )
    } catch (e: Exception) {
        Log.e("ParseLog", "Error parsing sensor data", e)
        null
    }
}