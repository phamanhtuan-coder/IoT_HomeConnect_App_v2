package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.dialog.WarningDialog
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Composable function cho màn hình "Chuyển quyền sở hữu" của một thiết bị IoT.
 * Màn hình này cho phép chủ sở hữu hiện tại chuyển quyền sở hữu cho người dùng khác bằng cách nhập email của họ.
 *
 * Bố cục màn hình bao gồm:
 * - Một thanh ứng dụng trên cùng với nút quay lại và tiêu đề "Space Details".
 * - Một khu vực nội dung chính bao gồm:
 *     - Một `ColoredCornerBox` (hiện tại trống, có thể dùng cho các yếu tố trực quan trong tương lai).
 *     - Một `InvertedCornerHeader` để tạo sự tách biệt trực quan.
 *     - Một `Column` chứa:
 *         - Một `StyledTextField` để người dùng nhập địa chỉ email của người nhận.
 *         - Một `ActionButtonWithFeedback` có nhãn "Chuyển quyền".
 * - Một thanh điều hướng dưới cùng sử dụng `MenuBottom`.
 *
 * **Chức năng:**
 * - `StyledTextField` cho phép người dùng nhập email của người mà họ muốn chuyển quyền sở hữu.
 * - Nhấp vào nút "Chuyển quyền" sẽ kích hoạt một hộp thoại xác nhận (`WarningDialog`).
 * - Nếu người dùng xác nhận trong hộp thoại:
 *     - Một vòng xoay tải sẽ hiển thị trên nút.
 *     - Một lệnh gọi API mô phỏng (trì hoãn 1 giây) được thực hiện.
 *     - Sau khi hoàn thành (mô phỏng) thành công, một thông báo thành công "Đã chuyển quyền thành công!" sẽ được hiển thị.
 *     - Nếu lệnh gọi API (mô phỏng) thất bại, một thông báo lỗi "Chuyển quyền thất bại!" sẽ được hiển thị.
 *     - Vòng xoay tải sẽ bị ẩn sau khi thao tác hoàn tất.
 * - Nếu người dùng hủy hộp thoại xác nhận, quá trình chuyển quyền sẽ bị hủy bỏ.
 *
 * @param navController `NavHostController` được sử dụng cho các hành động điều hướng, chẳng hạn như điều hướng quay lại.
 * @author Nguyễn Thanh Sang
 * @since 19-05-24
 * @updated Nguyễn Thanh Sang - 20/05/25: Bổ sung dialog xác nhận khi chuyển quyền.
 */

@Composable
fun TransferOwnershipScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()

    var pendingOnSuccess by remember { mutableStateOf<((String) -> Unit)?>(null) }
    var pendingOnError   by remember { mutableStateOf<((String) -> Unit)?>(null) }
    var showConfirm      by remember { mutableStateOf(false) }
    var isLoading        by remember { mutableStateOf(false) }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        var text by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type          = "Back",
                    title         = "Space Details"
                )
            },
            containerColor = Color.White,
            bottomBar = {
                MenuBottom(navController)
            }
        ) { scaffoldPadding ->
            Column (
                modifier= Modifier.padding(scaffoldPadding)
            ) {
                ColoredCornerBox(
                    cornerRadius = 40.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {}
                }

                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {}

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StyledTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholderText = "Nhập email",
                        leadingIcon = Icons.Default.Email
                    )

                    ActionButtonWithFeedback(
                        label  = "Chuyển quyền",
                        style  = HCButtonStyle.PRIMARY,
                        isLoadingFromParent = isLoading,
                        onAction = { onS, onE ->
                            /* ⇢ chỉ lưu callback, CHƯA gọi API */
                            pendingOnSuccess = onS
                            pendingOnError   = onE
                            showConfirm      = true
                        }
                    )

                    if (showConfirm) {
                        WarningDialog(
                            title       = "Xác nhận chuyển quyền",
                            text        = "Bạn có chắc muốn chuyển quyền sử dụng không?",
                            confirmText = "Đồng ý",
                            dismissText = "Huỷ",
                            onConfirm = {
                                showConfirm = false
                                isLoading   = true                // spinner ngay lập tức

                                scope.launch {
                                    delay(1000)                   // giả lập API
                                    val ok = true                 // kết quả thật
                                    if (ok) pendingOnSuccess?.invoke("Đã chuyển quyền thành công!")
                                    else     pendingOnError?.invoke("Chuyển quyền thất bại!")
                                    isLoading = false             // tắt spinner
                                    pendingOnSuccess = null
                                    pendingOnError   = null
                                }
                            },
                            onDismiss = {
                                showConfirm      = false
                                pendingOnSuccess = null
                                pendingOnError   = null
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "GroupScreen - Phone")
@Composable
fun TransferOwnershipScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        TransferOwnershipScreen(navController = rememberNavController())
    }
}