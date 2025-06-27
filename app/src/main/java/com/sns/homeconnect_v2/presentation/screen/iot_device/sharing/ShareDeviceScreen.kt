package com.sns.homeconnect_v2.presentation.screen.iot_device.sharing

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.presentation.component.dialog.WarningDialog
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.GenericDropdown
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.sharing.DeviceSharingActionState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.sharing.DeviceSharingViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import kotlinx.coroutines.launch


/**
 * Giao diện màn hình Share Device (ShareDeviceScreen).
 *
 * Màn hình này cho phép người dùng chia sẻ một thiết bị IoT với người dùng khác bằng cách nhập ID thiết bị,
 * email của người nhận và chọn quyền truy cập.
 *
 * -----------------------------------------
 * **Người viết:** Nguyễn Thanh Sang
 * **Ngày viết:** 21/05/2025
 * **Lần cập nhật cuối:** 21/05/2025
 * -----------------------------------------
 * **Input:**
 * - `id`: [Int] - ID của thiết bị cần chia sẻ. Thông tin này được truyền vào khi điều hướng đến màn hình.
 *
 * **Output:**
 * - Hiển thị giao diện người dùng cho phép nhập thông tin chia sẻ và thực hiện hành động chia sẻ.
 * - Sử dụng `ActionButtonWithFeedback` để xử lý hành động chia sẻ, bao gồm hiển thị dialog xác nhận
 *   và thông báo kết quả (thành công hoặc thất bại).
 *
 * **Luồng hoạt động:**
 * 1. Người dùng nhập email của người muốn chia sẻ vào trường `StyledTextField`.
 * 2. Người dùng chọn quyền truy cập cho người được chia sẻ từ `GenericDropdown`.
 * 3. Người dùng nhấn nút "Gửi yêu cầu" (`ActionButtonWithFeedback`).
 * 4. Một `WarningDialog` được hiển thị để xác nhận hành động chia sẻ.
 * 5. Nếu người dùng xác nhận:
 *    - Hàm `onAction` của `ActionButtonWithFeedback` được gọi.
 *    - Callback `onSuccess` và `onError` được lưu trữ tạm thời.
 *    - Trạng thái `isButtonLoading` được đặt thành `true` để hiển thị trạng thái tải.
 *    - Một coroutine được khởi chạy để mô phỏng yêu cầu API (trong ví dụ này là `delay(1000)`).
 *    - Dựa trên kết quả mô phỏng, callback `onSuccessCallback` hoặc `onErrorCallback` tương ứng được gọi
 *      để hiển thị thông báo kết quả.
 * 6. Nếu người dùng hủy bỏ, dialog sẽ đóng và không có hành động nào được thực hiện.
 *
 * **Thành phần chính:**
 */
@Composable
fun ShareDeviceScreen(
    navController: NavHostController,
    serialNumber: String,
    snackbarViewModel : SnackbarViewModel = hiltViewModel(),
    viewModel: DeviceSharingViewModel = hiltViewModel(),
) {
    val ticketState by viewModel.createTicketState.collectAsState()

    when (ticketState) {
        is DeviceSharingActionState.Success -> {
            val msg = (ticketState as DeviceSharingActionState.Success).success
            navController.popBackStack()
            snackbarViewModel.showSnackbar(msg, SnackbarVariant.SUCCESS)
        }
        is DeviceSharingActionState.Error -> {
            val msg = (ticketState as DeviceSharingActionState.Error).error
            snackbarViewModel.showSnackbar(msg, SnackbarVariant.ERROR)
        }
        else -> Unit
    }

//    val addSharedUserState by viewModel.addSharedUserState.collectAsState()
//    when (addSharedUserState) {
//        DeviceSharingActionState.Idle -> {
//            viewModel.getSharedUsers(id)
//        }
//
//        DeviceSharingActionState.Loading -> {
//            CircularProgressIndicator()
//        }
//
//        is DeviceSharingActionState.Success -> {
//            LaunchedEffect(Unit) {
//                navController.popBackStack(
//                    Screens.SharedUsers.route + "?id=${id}",
//                    inclusive = false
//                )
//            }
//        }
//
//        is DeviceSharingActionState.Error -> {
//            Log.e(
//                "ShareDeviceScreen",
//                "Error: ${(addSharedUserState as DeviceSharingState.Error).error}"
//            )
//        }
//    }
//    val deviceIdErrorState = remember { mutableStateOf("") }
//    val emailErrorState = remember { mutableStateOf("") }
//        val isTablet = isTablet(LocalContext.current)

    // state giữ hàm onSuccess / onError tạm thời
    val coroutineScope = rememberCoroutineScope()

    // state giữ giá trị hiện tại của dropdown
    var current by remember { mutableStateOf<String?>(null) }
    val deviceIdState = remember { mutableStateOf(serialNumber) }

    // state giữ giá trị nhập vào
    val usernameState = remember { mutableStateOf("") }

    /* state hiển thị dialog xác nhận */
    var showConfirm by remember { mutableStateOf(false) }

    /* state giữ hàm onSuccess / onError tạm thời */
    var isButtonLoading by remember { mutableStateOf(false) }
//    var pendingOnSuccess by remember { mutableStateOf<((String) -> Unit)?>(null) }
//    var pendingOnError   by remember { mutableStateOf<((String) -> Unit)?>(null) }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background),
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
            containerColor = colorScheme.background
        ) { paddingValues ->
            Column (
                modifier = Modifier
                    .padding(paddingValues )
                    .fillMaxSize()
            ) {
                ColoredCornerBox(
                    cornerRadius = 40.dp
                ) {
                }

                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {
                }

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
                        // Ô nhập Tên thiết bị
                        StyledTextField(
                            value = usernameState.value,
                            onValueChange = {
                                usernameState.value = it
                            },
                            placeholderText = "Usercase",
                            leadingIcon = Icons.Default.Email
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        GenericDropdown(
                            items = listOf("VIEW", "CONTROL"),
                            selectedItem = current,
                            onItemSelected = { current = it },
                            isTablet = false,
                            leadingIcon = Icons.Default.AssignmentTurnedIn // 👈 truyền icon vào
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ActionButtonWithFeedback(
                            label  = "Gửi yêu cầu",
                            style  = HCButtonStyle.PRIMARY,
                            onAction = { onSuccessCallback, onErrorCallback ->
                                /* 1. Lưu callback → chờ xác nhận */
//                                pendingOnSuccess = onSuccessCallback
//                                pendingOnError   = onErrorCallback
                                showConfirm      = true
                            },
                            isLoadingFromParent = isButtonLoading,
                            snackbarViewModel = snackbarViewModel
                        )

                        /* 2. ConfirmationDialog — hỏi người dùng */
                        if (showConfirm) {
                            WarningDialog(
                                title    = "Chia sẽ thiết bị",
                                text  = "Bạn có chắc muốn chia sẽ thiết bị này với người dùng này không?",
                                confirmText = " Đồng ý",
                                dismissText = "Huỷ",
                                onConfirm = {
                                    showConfirm = false        // đóng dialog
                                    /* 3. Sau khi đồng ý → thực thi hành động thật */
//                                    val onSuccessCallback = pendingOnSuccess    // copy local
//                                    val onErrorCallback = pendingOnError
//                                    pendingOnSuccess = null
//                                    pendingOnError   = null
                                    coroutineScope.launch {
                                        isButtonLoading = true

                                        viewModel.createSupportTicket(
                                            title = "Chia sẻ quyền thiết bị",
                                            description = current ?: "VIEW",
                                            ticketTypeId = 3,
                                            deviceSerial = deviceIdState.value,
                                            assignedTo = usernameState.value
                                        )
                                    }
                                },
                                onDismiss = {
                                    showConfirm = false          // huỷ, không làm gì
//                                    pendingOnSuccess = null
//                                    pendingOnError   = null
                                    isButtonLoading = false
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}