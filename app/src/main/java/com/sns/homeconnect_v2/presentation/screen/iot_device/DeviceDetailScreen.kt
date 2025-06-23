
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
import androidx.compose.material3.CircularProgressIndicator
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
import com.sns.homeconnect_v2.core.util.validation.sliderToPercent
import com.sns.homeconnect_v2.core.util.validation.toComposeColor
import com.sns.homeconnect_v2.data.remote.dto.request.AttributeRequest
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.response.ProductData
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.presentation.component.CustomSwitch
import com.sns.homeconnect_v2.presentation.component.DayPicker
import com.sns.homeconnect_v2.presentation.component.EdgeToEdgeSlider
import com.sns.homeconnect_v2.presentation.component.EffectSelector
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
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.UpdateDeviceStateBulkUiState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.UpdateDeviceStateUiState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_led.LedEffectViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_led.LedUiState
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

    // Lấy thông tin chi tiết thiết bị
    val ledViewModel: LedEffectViewModel = hiltViewModel()
    val ledState by ledViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        displayViewModel.fetchDeviceState(serialNumber)
        ledViewModel.fetchEffects(serialNumber)
    }

    LaunchedEffect(ledState) {
        when (val s = ledState) {
            is LedUiState.Success -> snackbarViewModel.showSnackbar("Đã áp dụng hiệu ứng", SnackbarVariant.SUCCESS)
            is LedUiState.Error   -> snackbarViewModel.showSnackbar(s.message,        SnackbarVariant.ERROR)
            else -> {}
        }
    }

    val showEffectSheet = remember { mutableStateOf(false) }

    val showBottomSheet = remember { mutableStateOf(false) }
    val isDataFetched = remember { mutableStateOf(false) }

    val displayState by displayViewModel.displayState.collectAsState()

    LaunchedEffect(Unit) {
        displayViewModel.fetchDeviceState(serialNumber)
    }

    LaunchedEffect(showBottomSheet.value) {
        if (showBottomSheet.value && !isDataFetched.value && product.id.isNotEmpty()) {
            displayViewModel.fetchDisplayInfo(product.id)   // <- hàm mới
            isDataFetched.value = true
        }
    }

    var rowWidth by remember { mutableIntStateOf(0) }
    var sliderValue by remember { mutableFloatStateOf(128f) }

//    LaunchedEffect(updateState) {
//        when (val state = updateState) {
//            is UpdateDeviceStateUiState.Success -> snackbarViewModel.showSnackbar(state.message)
//            is UpdateDeviceStateUiState.Error -> snackbarViewModel.showSnackbar(state.error, SnackbarVariant.ERROR)
//            else -> Unit
//        }
//    }

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

//    var safeDevice = DeviceResponse(
//        DeviceID = 1,
//        TypeID = 2,
//        Name = "Đèn LED thông minh",
//        PowerStatus = true,
//        SpaceID = 1,
//        Attribute = """{"brightness":80, "color":"#ffffff"}""" // Dữ liệu mẫu
//    )

//    Log.e("safeDevice", safeDevice.toString())
    /*
    LaunchedEffect(Unit) {
        // TODO: Re-enable API call when new API is ready
        // viewModel.loadDeviceDetail()

        // Use demo data for now
    }
    */

//    LaunchedEffect(safeDevice.Attribute) {
//        val attributeJson = if (safeDevice.Attribute.isEmpty()) {
//            """{"brightness":80, "color":"#ffffff"}""" // Giá trị mặc định
//        } else {
//            safeDevice.Attribute
//        }
//
//        Log.e("attributeJson", attributeJson)
//
//        val gson = Gson()
//        attribute = gson.fromJson(attributeJson, AttributeRequest::class.java)
//
//        Log.e("attributeJson", attribute.toString())
//    }

    var powerStatus by remember { mutableStateOf(false) }

//    LaunchedEffect(safeDevice) {
//        powerStatus = safeDevice.PowerStatus
//    }

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

    // Biến trạng thái để quản lý công tắc và độ sáng
    var uiPower               by remember { mutableStateOf(false) }   // giá trị hiển thị trên UI
    var pendingToggle         by remember { mutableStateOf<Boolean?>(null) }
    var isSendingToggle       by remember { mutableStateOf(false) }

    var pendingPower by remember { mutableStateOf<Boolean?>(null) }
    var isUpdatingPower by remember { mutableStateOf(false) }

    // Xử lý sự kiện khi người dùng bật/tắt công tắc
    val deviceState by displayViewModel.deviceState.collectAsState()

    // 2. Đồng bộ UI khi server trả về
    LaunchedEffect(deviceState) {
        when (val s = deviceState) {
            is DeviceStateUiState.Success -> {
                val st = s.state
                uiPower = st.power_status
                st.brightness?.let {         // 0‒100  ↦ 0‒255 cho slider
                    sliderValue = it * 2.55f
                    attribute = attribute.copy(brightness = it)   // lưu %
                }
                st.color?.let { hex ->
                    attribute = attribute.copy(color = hex)
                }
                isUpdatingPower = false; pendingPower = null

                Log.d("✅ServerResponse", "State: ${s.state}")
            }
            is DeviceStateUiState.Error -> {
                snackbarViewModel.showSnackbar("Lỗi?", SnackbarVariant.ERROR)
                Log.e("❌DeviceStateError", s.message)
            }
            else -> {}
        }
    }

    val updateState by displayViewModel.updateBulk.collectAsState()

    LaunchedEffect(updateState) {
        when (val st = updateState) {
            is UpdateDeviceStateBulkUiState.Success -> {
                snackbarViewModel.showSnackbar(st.message, SnackbarVariant.SUCCESS)
                isSendingToggle = false
                // refresh lại trạng thái
                displayViewModel.fetchDeviceState(serialNumber)
            }
            is UpdateDeviceStateBulkUiState.Error -> {
                snackbarViewModel.showSnackbar(st.error, SnackbarVariant.ERROR)
                isSendingToggle = false
                pendingToggle   = null
            }
            else -> Unit
        }
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
                                            text = deviceName,
                                            color = colorScheme.onPrimary,
                                            lineHeight = 32.sp,
                                            fontSize = 30.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))

                                        CustomSwitch(
                                            isCheck  = uiPower,
                                            enabled  = !isUpdatingPower,
                                            onCheckedChange = { newValue ->
                                                pendingPower   = newValue
                                                isUpdatingPower = true
                                                displayViewModel.updateDeviceStateBulk(
                                                    serial_number = serialNumber,
                                                    serial   = serialNumber,
                                                    updates  = listOf(mapOf("power_status" to newValue))
                                                )
                                            }
                                        )

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
//                                    if ("brightness" in controls) {
                                        Text("Độ sáng", color = colorScheme.onPrimary, fontSize = 20.sp)

                                        EdgeToEdgeSlider(
                                            value = sliderValue,
                                            onValueChange = { v ->
                                                sliderValue = v
                                                if (uiPower) displayViewModel.updateDeviceStateBulk(
                                                    serial_number = serialNumber,
                                                    serial   = serialNumber,
                                                    updates  = listOf(mapOf("brightness" to sliderToPercent(v))) // <-- dùng 0-100
                                                )
                                            }
                                        )

//                                    }
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

//                                if ("color" in controls) {
                                /* ---------------------- Trong DeviceDetailScreen ---------------------- */

                                FancyColorSlider(
                                    attribute = attribute,
                                    onColorChange = { hex ->
                                        attribute = attribute.copy(color = hex)        // cập nhật state UI
                                        if (uiPower) {                                 // chỉ sync khi đèn đang bật
                                            displayViewModel.updateDeviceStateBulk(
                                                serial_number = serialNumber,
                                                serial   = serialNumber,
                                                updates  = listOf(mapOf("color" to hex))
                                            )
                                        }
                                    }
                                )
//                                }
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
                                .padding(horizontal = (16.dp))
                                .onSizeChanged { size ->
                                    rowWidth = size.width
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ActionButtonWithFeedback(
                                label  = "Hiệu ứng LED",
                                style  = HCButtonStyle.PRIMARY,
                                height = 62.dp,
                                textSize = 20.sp,
                                modifier = Modifier.fillMaxWidth(),
                                snackbarViewModel = snackbarViewModel,
                                onAction = { onS, _ ->
                                    onS("Đang mở…")
                                    showEffectSheet.value = true
                                }
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
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

                            /* ---------- SHEET: HIỆU ỨNG LED ---------- */
                            if (showEffectSheet.value) {
                                ModalBottomSheet(
                                    onDismissRequest = { showEffectSheet.value = false }
                                ) {
                                    if (ledState is LedUiState.Loading) {
                                        CircularProgressIndicator(
                                            Modifier
                                                .padding(24.dp)
                                                .align(Alignment.CenterHorizontally)
                                        )
                                    }

                                    EffectSelector(
                                        state = ledState,
                                        snackbarViewModel = snackbarViewModel,
                                        onApply = { effect, speed, count, c1, c2 ->
                                            ledViewModel.applyEffect(
                                                deviceId  = deviceId,
                                                serial    = serialNumber,
                                                effect    = effect,
                                                speed     = speed,
                                                count     = count,
                                                color1    = c1,
                                                color2    = c2
                                            )
                                            showEffectSheet.value = false
                                        }
                                    )

                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                    ) {
                                        ActionButtonWithFeedback(
                                            label  = "Dừng hiệu ứng",
                                            style  = HCButtonStyle.SECONDARY,
                                            height = 54.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            snackbarViewModel = snackbarViewModel,
                                            onAction = { onS, _ ->
                                                ledViewModel.stopEffect(deviceId, serialNumber)
                                                showEffectSheet.value = false
                                                onS("Đang dừng…")
                                            }
                                        )

                                        Spacer(Modifier.width(8.dp))

                                        ActionButtonWithFeedback(
                                            label  = "Chế độ mẫu",
                                            style  = HCButtonStyle.PRIMARY,
                                            height = 54.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            snackbarViewModel = snackbarViewModel,
                                            onAction = { onS, _ ->
                                                ledViewModel.applyPreset(
                                                    deviceId  = deviceId,
                                                    serial    = serialNumber,
                                                    preset    = "party_mode",
                                                    duration  = 30_000   // 30 giây
                                                )
                                                showEffectSheet.value = false
                                                onS("Đang áp dụng preset…")
                                            }
                                        )
                                    }

                                    Spacer(Modifier.height(12.dp))
                                }
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
