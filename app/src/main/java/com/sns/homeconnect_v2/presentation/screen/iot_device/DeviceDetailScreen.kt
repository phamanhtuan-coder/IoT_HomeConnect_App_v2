
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.sns.homeconnect_v2.R
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.data.remote.dto.request.AttributeRequest
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.response.ProductData
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
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceCapabilitiesViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceDisplayInfoState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceDisplayViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceStateUiState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.UpdateDeviceStateUiState
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(
    navController: NavHostController,
    deviceId: String,
    deviceName: String,
    serialNumber: String,
    product: ProductData,
    controls: Map<String, String>,
    snackbarViewModel: SnackbarViewModel
) {

    val displayViewModel: DeviceDisplayViewModel = hiltViewModel()
    val showBottomSheet = remember { mutableStateOf(false) }
    val isDataFetched = remember { mutableStateOf(false) } // <- tránh gọi lại API liên tục

    val displayState by displayViewModel.displayState.collectAsState()
    val deviceState  by displayViewModel.deviceState.collectAsState()
    val updateState  by displayViewModel.updateDeviceState.collectAsState()

    LaunchedEffect(showBottomSheet.value) {
        if (showBottomSheet.value && !isDataFetched.value && product.id.isNotEmpty()) {
            displayViewModel.fetchDisplayInfo(product.id)   // <- hàm mới
            isDataFetched.value = true
        }
    }

    var rowWidth by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var isCheck by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableFloatStateOf(128f) }
    var currentColor by remember { mutableStateOf("#ffffff") }

    LaunchedEffect(Unit) {
        if (serialNumber != null) {
            displayViewModel.fetchDeviceState(deviceId, serialNumber)
        }
        isCheck = when (deviceState) {
            is DeviceStateUiState.Success -> (deviceState as DeviceStateUiState.Success).state.power_status
            else -> false
        }
    }

    LaunchedEffect(deviceState) {
        val successState = deviceState as? DeviceStateUiState.Success
        successState?.let {
            isCheck = it.state.power_status
//            sliderValue = it.state.brightness.toFloat()
//            currentColor = it.state.color
        }
    }

    LaunchedEffect(updateState) {
        when (val state = updateState) {
            is UpdateDeviceStateUiState.Success -> snackbarViewModel.showSnackbar(state.message)
            is UpdateDeviceStateUiState.Error -> snackbarViewModel.showSnackbar(state.error, SnackbarVariant.ERROR)
            else -> Unit
        }
    }


    val scope = rememberCoroutineScope()

    var pendingAction by remember { mutableStateOf<DeviceAction?>(null) }
    var loadingAction by remember { mutableStateOf<DeviceAction?>(null) }

    var pendingOnSuccess by remember { mutableStateOf<((String) -> Unit)?>(null) }
    var pendingOnError by remember { mutableStateOf<((String) -> Unit)?>(null) }

    var confirmTitle by remember { mutableStateOf("") }
    var confirmMessage by remember { mutableStateOf("") }
    var showConfirm by remember { mutableStateOf(false) }

    var attribute by remember {
        mutableStateOf(
            AttributeRequest(
                brightness = 80,
                color = "#ffffff"
            )
        )
    }

    var safeDevice = DeviceResponse(
        DeviceID = 1,
        TypeID = 2,
        Name = "Đèn LED thông minh",
        PowerStatus = true,
        SpaceID = 1,
        Attribute = """{"brightness":80, "color":"#ffffff"}""" // Dữ liệu mẫu
    )

    Log.e("safeDevice", safeDevice.toString())
    /*
    LaunchedEffect(Unit) {
        // TODO: Re-enable API call when new API is ready
        // viewModel.loadDeviceDetail()

        // Use demo data for now
    }
    */

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

    var showAlertDialog by remember { mutableStateOf(false) }
    if (showAlertDialog) {
        WarningDialog(
            title = "Gỡ kết nối",
            text = "Bạn có chắc chắn muốn gỡ kết nối thiết bị này không?",
            onConfirm = {
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
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Chi tiết thiết bị"
                )
            },
            bottomBar = {
                MenuBottom(navController)
            },
            containerColor = colorScheme.background,
            modifier = Modifier.fillMaxSize(),
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    item {
                        ColoredCornerBox(
                            cornerRadius = 40.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(
                                                end = 12.dp
                                            )
                                            .fillMaxWidth()
                                            .background(color = colorScheme.primary)
                                            .weight(0.2f),
                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = safeDevice.Name,
                                            color = colorScheme.onPrimary,
                                            lineHeight = 32.sp,
                                            fontSize = 30.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))

                                        if ("power_status" in controls) {
                                            CustomSwitch(
                                                isCheck = isCheck,
                                                onCheckedChange = {
                                                    isCheck = it
                                                    Log.d("Switch", "serial=$serialNumber, deviceId=$deviceId, power=$it")
                                                    displayViewModel.updateDeviceState(
                                                        deviceId = deviceId,
                                                        serial = serialNumber,
                                                        power = it
                                                    )
                                                }
                                            )
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.Bottom
                                        ) {
                                            Text(
                                                text = ((sliderValue * 100 / 255).roundToInt()).toString(),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 50.sp,
                                                color = colorScheme.onPrimary
                                            )
                                            Text(
                                                "%",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 25.sp,
                                                modifier = Modifier.offset(y = (-8).dp),
                                                color = colorScheme.onPrimary
                                            )
                                        }

                                        Text(
                                            "Độ sáng",
                                            color = colorScheme.onPrimary,
                                            fontSize = 20.sp
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    Image(
                                        painter = painterResource(id = R.drawable.lamp),
                                        modifier = Modifier.size(200.dp),
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(colorScheme.onPrimary)
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .width(rowWidth.dp)
                                        .background(color = colorScheme.primary)
                                        .clickable(enabled = false) {}
                                        .padding(horizontal = 16.dp)
                                ) {
                                    if ("brightness" in controls) {
                                        Text("Độ sáng", color = colorScheme.onPrimary, fontSize = 20.sp)

                                        EdgeToEdgeSlider(
                                            value = sliderValue,
                                            onValueChange = {
                                                sliderValue = it
                                                displayViewModel.updateDeviceState(
                                                    deviceId = deviceId,
                                                    serial = serialNumber,
                                                    brightness = it.toInt()
                                                )
                                            }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp)
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(colorScheme.onPrimary)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                if ("color" in controls) {
                                    FancyColorSlider(
                                        attribute = attribute,
                                        onColorChange = { color ->
                                            displayViewModel.updateDeviceState(
                                                deviceId = deviceId,
                                                serial = serialNumber,
                                                color = color
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        InvertedCornerHeader(
                            backgroundColor = colorScheme.surface,
                            overlayColor = colorScheme.primary
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp, vertical = 8.dp
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
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

                                IconButton(
                                    onClick = {
                                        navController.navigate(
                                            Screens.AccessPoint.createRoute(deviceId, deviceName)
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

                                if (showBottomSheet.value) {
                                    ModalBottomSheet(
                                        onDismissRequest = { showBottomSheet.value = false }
                                    ) {
                                        when (displayState) {
                                            is DeviceDisplayInfoState.Success -> {
                                                val product = (displayState as DeviceDisplayInfoState.Success).product
                                                val category = (displayState as DeviceDisplayInfoState.Success).category
                                                Column(modifier = Modifier.padding(16.dp)) {
                                                    Text("Tên sản phẩm: ${product.name ?: "Không rõ"}")
                                                    Text("Danh mục: ${category.name ?: "Không rõ"}")
                                                    Text("Giá: ${product.selling_price  ?: "Không rõ"}")
                                                    Text("Mô tả: ${product.description_normal ?: "Không có mô tả"}")
                                                }
                                            }
                                            is DeviceDisplayInfoState.Loading -> {
                                                Text("Đang tải thông tin sản phẩm...")
                                            }
                                            is DeviceDisplayInfoState.Error -> {
                                                Text("Lỗi: ${(displayState as DeviceDisplayInfoState.Error).error}")
                                            }
                                            else -> {}
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .wrapContentWidth()
                                .onSizeChanged { size ->
                                    rowWidth = size.width
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    ActionButtonWithFeedback(
                                        label = "Khóa thiết bị",
                                        onAction = { onS, onE ->
                                            pendingOnSuccess = onS
                                            pendingOnError = onE
                                            confirmTitle = "Khóa thiết bị"
                                            confirmMessage = "Bạn có chắc muốn khoá thiết bị này?"
                                            pendingAction = DeviceAction.LOCK
                                            showConfirm = true
                                        },
                                        style = HCButtonStyle.PRIMARY,
                                        height = 62.dp,
                                        textSize = 20.sp,
                                        modifier = Modifier.weight(1f),
                                        isLoadingFromParent = loadingAction == DeviceAction.LOCK,
                                        snackbarViewModel = snackbarViewModel
                                    )

                                    ActionButtonWithFeedback(
                                        label = "Gỡ kết nối",
                                        onAction = { onS, onE ->
                                            pendingOnSuccess = onS
                                            pendingOnError = onE
                                            confirmTitle = "Gỡ kết nối"
                                            confirmMessage = "Bạn muốn gỡ kết nối!"
                                            pendingAction = DeviceAction.UNLINK
                                            showConfirm = true
                                        },
                                        style = HCButtonStyle.PRIMARY,
                                        height = 62.dp,
                                        textSize = 20.sp,
                                        modifier = Modifier.weight(1f),
                                        isLoadingFromParent = loadingAction == DeviceAction.UNLINK,
                                        snackbarViewModel = snackbarViewModel
                                    )
                                }

                                Spacer(Modifier.height(12.dp))

                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    ActionButtonWithFeedback(
                                        label = "Chia sẻ quyền",
                                        onAction = { onS, _ ->
                                            loadingAction = DeviceAction.SHARE
                                            scope.launch {
                                                delay(1000)
                                                onS("Đã chia sẻ")
                                                loadingAction = null
                                            }
                                        },
                                        style = HCButtonStyle.PRIMARY,
                                        height = 62.dp,
                                        textSize = 20.sp,
                                        modifier = Modifier.weight(1f),
                                        isLoadingFromParent = loadingAction == DeviceAction.SHARE,
                                        snackbarViewModel = snackbarViewModel
                                    )

                                    ActionButtonWithFeedback(
                                        label = "Reset thiết bị",
                                        onAction = { onS, onE ->
                                            pendingOnSuccess = onS
                                            pendingOnError = onE
                                            confirmTitle = "Reset thiết bị"
                                            confirmMessage = "Bạn muốn reset thiết bị này!"
                                            pendingAction = DeviceAction.RESET
                                            showConfirm = true
                                        },
                                        style = HCButtonStyle.PRIMARY,
                                        height = 62.dp,
                                        textSize = 20.sp,
                                        modifier = Modifier.weight(1f),
                                        isLoadingFromParent = loadingAction == DeviceAction.RESET,
                                        snackbarViewModel = snackbarViewModel
                                    )
                                }

                                Spacer(Modifier.height(12.dp))

                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    ActionButtonWithFeedback(
                                        label = "Chuyển quyền sở hữu",
                                        onAction = { onS, _ ->
                                            loadingAction = DeviceAction.TRANSFER
                                            scope.launch {
                                                delay(1000)
                                                onS("Đã chuyển")
                                                loadingAction = null
                                            }
                                        },
                                        style = HCButtonStyle.PRIMARY,
                                        height = 62.dp,
                                        textSize = 20.sp,
                                        modifier = Modifier.weight(1f),
                                        isLoadingFromParent = loadingAction == DeviceAction.TRANSFER,
                                        snackbarViewModel = snackbarViewModel
                                    )

                                    ActionButtonWithFeedback(
                                        label = "Xem phiên bản",
                                        onAction = { onS, _ ->
                                            loadingAction = DeviceAction.VERSION
                                            scope.launch {
                                                delay(1000)
                                                onS("v1.0")
                                                loadingAction = null
                                            }
                                        },
                                        style = HCButtonStyle.PRIMARY,
                                        height = 62.dp,
                                        textSize = 20.sp,
                                        modifier = Modifier.weight(1f),
                                        isLoadingFromParent = loadingAction == DeviceAction.VERSION,
                                        snackbarViewModel = snackbarViewModel
                                    )
                                }

                                Spacer(Modifier.height(16.dp))

                                ActionButtonWithFeedback(
                                    label = "Báo mất thiết bị",
                                    onAction = { onS, _ ->
                                        loadingAction = DeviceAction.REPORT_LOST
                                        scope.launch {
                                            delay(1000)
                                            onS("Đã báo mất")
                                            loadingAction = null
                                        }
                                    },
                                    style = HCButtonStyle.PRIMARY,
                                    height = 62.dp,
                                    textSize = 20.sp,
                                    modifier = Modifier.fillMaxWidth(0.8f),
                                    isLoadingFromParent = loadingAction == DeviceAction.REPORT_LOST,
                                    snackbarViewModel = snackbarViewModel
                                )
                            }

                            if (showConfirm) {
                                WarningDialog(
                                    title = confirmTitle,
                                    text = confirmMessage,
                                    confirmText = "Đồng ý",
                                    dismissText = "Huỷ",
                                    onConfirm = {
                                        showConfirm = false

                                        loadingAction = pendingAction
                                        pendingAction = null

                                        scope.launch {
                                            delay(1000)
                                            val ok = true
                                            if (ok) pendingOnSuccess?.invoke("Thành công!")
                                            else pendingOnError?.invoke("Thất bại!")

                                            loadingAction = null
                                        }
                                    },
                                    onDismiss = {
                                        showConfirm = false
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

//@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "DeviceDetailScreen Preview - Phone")
//@Composable
//fun DeviceDetailScreenPreview() {
//    IoTHomeConnectAppTheme {
//        DeviceDetailScreen(navController = rememberNavController())
//    }
//}

@Composable
fun DeviceDetailTabletScreen(
    navController: NavHostController
) {
    var rowWidth by remember { mutableStateOf<Int?>(null) }
    var selectedTimeBegin by remember { mutableStateOf("12:00 AM") }
    var selectedTimeEnd by remember { mutableStateOf("12:00 AM") }
    var showDialogTimePickerBegin by remember { mutableStateOf(false) }
    var showDialogTimePickerEnd by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val switchState by remember { mutableStateOf(true) }

    var attribute by remember {
        mutableStateOf(
            AttributeRequest(
                brightness = 0,
                color = "#000000"
            )
        )
    }

    var safeDevice = DeviceResponse(
        DeviceID = 0,
        TypeID = 0,
        Name = "",
        PowerStatus = false,
        SpaceID = 0,
        Attribute = ""
    )

    Log.e("safeDevice", safeDevice.toString())

    /*
    LaunchedEffect(Unit) {
        // TODO: Re-enable API call when new API is ready
        // viewModel.loadDeviceDetail()

        // Use demo data for now
    }
    */

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

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Chi tiết thiết bị"
                )
            },
            bottomBar = {
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
                            .fillMaxSize()
                            .fillMaxHeight()
                            .imePadding()
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = colorScheme.background)
                                .wrapContentHeight()
                        ) {
                            Column {
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
                                            .clip(RoundedCornerShape(bottomStart = 40.dp))
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(
                                                    top = 8.dp,
                                                    end = 12.dp
                                                )
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .padding(
                                                            start = 12.dp,
                                                            end = 12.dp
                                                        )
                                                        .fillMaxWidth()
                                                        .background(color = colorScheme.primary)
                                                        .weight(0.2f),
                                                    horizontalAlignment = Alignment.Start,
                                                    verticalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = safeDevice.Name,
                                                        color = colorScheme.onPrimary,
                                                        lineHeight = 32.sp,
                                                        fontSize = 30.sp
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))

                                                    Switch(
                                                        checked = powerStatus,
                                                        onCheckedChange = {
                                                            powerStatus = !powerStatus
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

                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.Start,
                                                        verticalAlignment = Alignment.Bottom
                                                    ) {
                                                        Text(
                                                            text = attribute.brightness.toString(),
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 50.sp,
                                                            color = colorScheme.onPrimary
                                                        )
                                                        Text(
                                                            "%",
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 25.sp,
                                                            modifier = Modifier.offset(y = (-8).dp),
                                                            color = colorScheme.onPrimary
                                                        )
                                                    }
                                                    Text(
                                                        "Độ sanng",
                                                        color = colorScheme.onPrimary,
                                                        fontSize = 20.sp
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                }

                                                Image(
                                                    painter = painterResource(id = R.drawable.lamp),
                                                    modifier = Modifier.size(150.dp),
                                                    contentDescription = "",
                                                    colorFilter = ColorFilter.tint(colorScheme.onPrimary)
                                                )
                                            }

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(start = 12.dp, end = 12.dp)
                                                    .background(color = colorScheme.primary)
                                            ) {
                                                Text(
                                                    "Cường độ",
                                                    color = colorScheme.onPrimary,
                                                )

                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .width(500.dp)
                                                            .onSizeChanged { size ->
                                                                rowWidth = size.width
                                                            },
                                                        horizontalArrangement = Arrangement.Center,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Image(
                                                            painter = painterResource(id = R.drawable.bulboff),
                                                            modifier = Modifier
                                                                .padding(end = 8.dp)
                                                                .size(24.dp),
                                                            contentDescription = ""
                                                        )

                                                        Slider(
                                                            value = attribute.brightness.toFloat(),
                                                            onValueChange = {

                                                            },
                                                            onValueChangeFinished = {
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
                                            .zIndex(1f)
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
                                            .zIndex(2f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(topEndPercent = 100))
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        top = 12.dp,
                                                        start = 12.dp,
                                                        end = 8.dp
                                                    ),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Button(
                                                    onClick = {
                                                        showDialog = true
                                                    },
                                                    modifier = Modifier
                                                        .size(36.dp),
                                                    shape = CircleShape,
                                                    contentPadding = PaddingValues(0.dp),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = colorScheme.onPrimary,
                                                        contentColor = colorScheme.primary
                                                    )
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Info,
                                                        contentDescription = "Info",
                                                        modifier = Modifier.size(36.dp),
                                                        tint = colorScheme.primary
                                                    )
                                                }
                                                Button(
                                                    onClick = {
                                                        navController.navigate(Screens.AccessPoint.route + "?id=${safeDevice.DeviceID}&name=${safeDevice.Name}")
                                                    },
                                                    modifier = Modifier
                                                        .size(36.dp),
                                                    shape = CircleShape,
                                                    contentPadding = PaddingValues(0.dp),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = colorScheme.onPrimary,
                                                        contentColor = colorScheme.primary
                                                    )
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Wifi,
                                                        contentDescription = "Wifi",
                                                        modifier = Modifier.size(36.dp),
                                                        tint = colorScheme.primary
                                                    )
                                                }
                                                if (showDialog) {
                                                    fun getIconForType(typeId: Int): String {
                                                        return when (typeId) {
                                                            1 -> "Fire Alarm"
                                                            2 -> "LED Light"
                                                            else -> ""
                                                        }
                                                    }
                                                    AlertDialog(
                                                        onDismissRequest = {
                                                            showDialog = false
                                                        },
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
                                                showDialog = false
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
                                                showDialog = false
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
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            navController.navigate(Screens.ActivityHistory.route)
                                        },
                                        modifier = Modifier
                                            .weight(1f)
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
                                            .weight(1f)
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
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            navController.navigate(Screens.SharedUsers.route + "?id=${safeDevice.DeviceID}")
                                        },
                                        modifier = Modifier
                                            .weight(1f)
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
                                            .weight(1f)
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