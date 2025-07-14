    package com.sns.homeconnect_v2.presentation.component.widget

import IoTHomeConnectAppTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.presentation.component.TopSnackbar

import com.sns.homeconnect_v2.presentation.component.dialog.WarningDialog
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



/**
 * Xác định kiểu dáng của nút hành động.
 * - [PRIMARY]: Kiểu chính, thường được sử dụng cho hành động quan trọng nhất.
 * - [SECONDARY]: Kiểu phụ, thường được sử dụng cho các hành động ít quan trọng hơn hoặc hành động thay thế.
 * - [DISABLED]: Kiểu không khả dụng, nút sẽ bị vô hiệu hóa và không thể tương tác.
 */
enum class HCButtonStyle { PRIMARY, SECONDARY, DISABLED }

/**
 * Một nút hành động 3‑trạng‑thái (Primary, Secondary, Disabled) kèm loading + dialog phản hồi.
 *
 * @author Nguyễn Thanh Sang
 * @since 20-05-2025
 * @param label Nhãn hiển thị trên nút.
 * @param onAction Hàm lambda được gọi khi nút được nhấn. Hàm này nhận hai callback:
 *                 `onSuccess` (để truyền thông điệp thành công) và `onError` (để truyền thông điệp lỗi).
 *                 Hàm này được thực thi trong một coroutine scope.
 * @param style Kiểu của nút, mặc định là [HCButtonStyle.PRIMARY].
 * @param onSuccess Callback được gọi khi hành động thành công và người dùng nhấn "OK" trên dialog thành công.
 * @param modifier Modifier để tùy chỉnh giao diện và hành vi của nút.
 * @param height Chiều cao của nút, mặc định là `56.dp`.
 * @param width Chiều rộng của nút, mặc định là [Dp.Unspecified] (sẽ fill chiều rộng của container).
 * @param textSize Kích thước chữ của nhãn nút, mặc định là `26.sp`.
 * @param shape Hình dạng của nút, mặc định là bo góc `12.dp`.
 * @param successDialogTitle Tiêu đề của dialog hiển thị khi hành động thành công.
 * @param errorDialogTitle Tiêu đề của dialog hiển thị khi hành động thất bại.
 * @param isLoadingFromParent Cờ để kiểm soát trạng thái loading của nút từ bên ngoài (component cha).
 *                            Khi `true`, nút sẽ hiển thị trạng thái loading.
 *                            Trạng thái loading nội bộ của nút sẽ được cập nhật theo giá trị này.
 */
@Composable
fun ActionButtonWithFeedback(
    label: String,
    onAction: suspend (onSuccess: (String) -> Unit, onError: (String) -> Unit) -> Unit,
    style: HCButtonStyle = HCButtonStyle.PRIMARY,
    onSuccess: () -> Unit = {},
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
    width: Dp = 200.dp,
    textSize: TextUnit = 18.sp,
    shape: RoundedCornerShape = RoundedCornerShape(50),
    isLoadingFromParent: Boolean = false,
    snackbarViewModel: SnackbarViewModel // Thêm tham số này!
) {
    IoTHomeConnectAppTheme {
        var isLoading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        // Nếu cha ép loading
        LaunchedEffect(isLoadingFromParent) { isLoading = isLoadingFromParent }

        // Màu sắc button
        val cs = MaterialTheme.colorScheme
        val (bg, fg, border) = when (style) {
            HCButtonStyle.PRIMARY   -> Triple(cs.primary, cs.onPrimary, null)
            HCButtonStyle.SECONDARY -> Triple(cs.onPrimary, cs.primary, BorderStroke(2.dp, cs.primary))
            HCButtonStyle.DISABLED  -> Triple(Color(0xFFE0E0E0), Color(0xFF9E9E9E), null)
        }
        val enabled = style != HCButtonStyle.DISABLED && !isLoading

        Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        delay(2000)
                        onAction(
                            { msg ->
                                isLoading = false
                                snackbarViewModel.showSnackbar(msg, SnackbarVariant.SUCCESS)
                                onSuccess()
                            },
                            { err ->
                                isLoading = false
                                snackbarViewModel.showSnackbar(err, SnackbarVariant.ERROR)
                            }
                        )
                    }
                },
                enabled = enabled,
                shape = shape,
                border = border,
                colors = ButtonDefaults.buttonColors(
                    containerColor = bg,
                    contentColor = fg,
                    disabledContainerColor = bg,
                    disabledContentColor = fg
                ),
                modifier = Modifier
                    .height(height)
                    .fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = fg,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Đang xử lý…", fontSize = textSize, fontWeight = FontWeight.Medium)
                } else {
                    Text(label, fontSize = textSize, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionButtonStylesPreview() {
    val coroutineScope = rememberCoroutineScope()
    /* state hiển thị dialog xác nhận */
    var showConfirm by remember { mutableStateOf(false) }

    /* state giữ hàm onSuccess / onError tạm thời */
    var isButtonLoading by remember { mutableStateOf(false) }
    var pendingOnSuccess by remember { mutableStateOf<((String) -> Unit)?>(null) }
    var pendingOnError   by remember { mutableStateOf<((String) -> Unit)?>(null) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
//        ActionButtonWithFeedback(
//            label  = "Hoàn tất",
//            style  = HCButtonStyle.PRIMARY,
//            // 1) Hành động thật của bạn
//            onAction = { onSuccessCallback, onErrorCallback ->
//                /* 1. Lưu callback → chờ xác nhận */
//                pendingOnSuccess = onSuccessCallback
//                pendingOnError   = onErrorCallback
//                showConfirm      = true
//            },
//            // 2) Tùy chỉnh tiêu đề dialog
//            successDialogTitle = "✅ Thành công",
//            errorDialogTitle   = "⚠️ Lỗi",
//            isLoadingFromParent = isButtonLoading
//        )

        /* 2. ConfirmationDialog — hỏi người dùng */
        if (showConfirm) {
            WarningDialog(
                title    = "Xác nhận hành động",
                text  = "Bạn có chắc muốn thực hiện thao tác này?",
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
                        delay(1000)               // mô phỏng API
                        val isActionSuccessful = true           // ← kết quả thật
                        if (isActionSuccessful) onSuccessCallback?.invoke("Thiết bị đã thêm thành công!")
                        else      onErrorCallback?.invoke("Thao tác thất bại, vui lòng thử lại.")
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

//        ActionButtonWithFeedback(
//            label  = "Hoàn tất",
//            style  = HCButtonStyle.PRIMARY,
//
//            // 1) Hành động thật của bạn
//            onAction = { onSuccess, onError ->
//
//                coroutineScope.launch {
//                    // mô phỏng call API → delay 1 giây
//                    delay(1000)
//
//                    val isActionSuccessful = true // ← kết quả thật ở đây
//                    if (isActionSuccessful)   onSuccess("Thiết bị đã thêm thành công!")
//                    else        onError("Thao tác thất bại, vui lòng thử lại.")
//                }
//            },
//        )
//        ActionButtonWithFeedback(
//            label = "Yes",
//            style = HCButtonStyle.SECONDARY,
//            onAction = { _, _ -> }
//        )
//
//        ActionButtonWithFeedback(
//            label = "Yes",
//            style = HCButtonStyle.DISABLED,
//            onAction = { _, _ -> }
//        )
    }
}
