package com.sns.homeconnect_v2.presentation.screen.iot_device.sharing

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.sns.homeconnect_v2.presentation.component.dialog.WarningDialog
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.GenericDropdown
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField
import kotlinx.coroutines.delay
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
//    navController: NavHostController,
    id: Int,
//    viewModel: DeviceSharingViewModel = hiltViewModel(),
) {
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

    val coroutineScope = rememberCoroutineScope()

    var current by remember { mutableStateOf<String?>(null) }
    val deviceIdState = remember { mutableIntStateOf(id) }
//    val deviceIdErrorState = remember { mutableStateOf("") }

    val emailState = remember { mutableStateOf("") }
//    val emailErrorState = remember { mutableStateOf("") }

    /* state hiển thị dialog xác nhận */
    var showConfirm by remember { mutableStateOf(false) }

    /* state giữ hàm onSuccess / onError tạm thời */
    var isButtonLoading by remember { mutableStateOf(false) }
    var pendingOnSuccess by remember { mutableStateOf<((String) -> Unit)?>(null) }
    var pendingOnError   by remember { mutableStateOf<((String) -> Unit)?>(null) }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

//        val isTablet = isTablet(LocalContext.current)
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background),
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), // padding quanh Text
                        contentAlignment = Alignment.Center // 👉 Canh giữa bên trong box
                    ) {
                        Text(
                            "CHIA SẺ THIẾT BỊ",
                            style = TextStyle(
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }


                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ){}

                Box(
                    modifier = Modifier
                        .fillMaxSize()                 // chiếm trọn màn hình
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)   // căn giữa trong Box
                            .padding(16.dp)            // padding 16dp bốn phía
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement  = Arrangement.Center,
                        horizontalAlignment  = Alignment.CenterHorizontally
                    ) {
                        // Ô nhập ID thiết bị
                        StyledTextField(
                            value = deviceIdState.intValue.toString(),
                            onValueChange = {
                            },
                            placeholderText = "ID Thiết bị",
                            leadingIcon = Icons.Default.Devices
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Ô nhập Tên thiết bị
                        StyledTextField(
                            value = emailState.value,
                            onValueChange = {
                                emailState.value = it
                            },
                            placeholderText = "Email tài khoản",
                            leadingIcon = Icons.Default.Email
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Dropdown Spaces
                        // Nếu bạn không muốn dùng ExposedDropdownMenuBox
                        // có thể tùy chỉnh DropdownMenuItem thủ công, nhưng dưới đây là ví dụ M3.

                        GenericDropdown(
                            items = listOf("Đóng/mở", "Điều khiển độ sáng", "điều khiển màu"),
                            selectedItem = current,
                            onItemSelected = { current = it },
                            isTablet = false,
                            leadingIcon = Icons.Default.AssignmentTurnedIn // 👈 truyền icon vào
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ActionButtonWithFeedback(
                            label  = "Gửi yêu cầu",
                            style  = HCButtonStyle.PRIMARY,
                            // 1) Hành động thật của bạn
                            onAction = { onSuccessCallback, onErrorCallback ->
                                /* 1. Lưu callback → chờ xác nhận */
                                pendingOnSuccess = onSuccessCallback
                                pendingOnError   = onErrorCallback
                                showConfirm      = true
                            },
                            // 2) Tùy chỉnh tiêu đề dialog
                            successDialogTitle = "Thành công",
                            errorDialogTitle   = "Lỗi",
                            isLoadingFromParent = isButtonLoading
                        )

                        /* 2. ConfirmationDialog — hỏi người dùng */
                        if (showConfirm) {
                            WarningDialog(
                                title    = "Chia sẽ thiết bị",
                                text  = "Bạn có chắc muốn chia sẽ thiết bị này với người dùng này không?",
                                confirmText = "Đồng ý",
                                dismissText = "Huỷ",
                                onConfirm = {
                                    showConfirm = false        // đóng dialog
                                    /* 3. Sau khi đồng ý → thực thi hành động thật */
                                    val onSuccessCallback = pendingOnSuccess    // copy local
                                    val onErrorCallback = pendingOnError
                                    pendingOnSuccess = null
                                    pendingOnError   = null
                                    coroutineScope.launch {
                                        isButtonLoading = true
                                        delay(1000)               // mô phỏng API
                                        val isActionSuccessful = true           // ← kết quả thật
                                        if (isActionSuccessful) onSuccessCallback?.invoke("Thiết bị đã thêm thành công!")
                                        else      onErrorCallback?.invoke("Thao tác thất bại, vui lòng thử lại.")
                                        isButtonLoading = false
                                    }
                                },
                                onDismiss = {
                                    showConfirm = false          // huỷ, không làm gì
                                    pendingOnSuccess = null
                                    pendingOnError   = null
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShareDeviceScreenPreview() {
    ShareDeviceScreen(1)
}