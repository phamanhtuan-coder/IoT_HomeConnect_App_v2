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
 */
@Composable
fun ActionButtonWithFeedback(
    label: String,
    onAction: suspend (onSuccess: (String) -> Unit, onError: (String) -> Unit) -> Unit,
    style: HCButtonStyle = HCButtonStyle.PRIMARY,
    onSuccess: () -> Unit = {},
    modifier: Modifier = Modifier,
    height: Dp = 56.dp,
    width: Dp = Dp.Unspecified,
    textSize: TextUnit = 26.sp,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp)
) {
    IoTHomeConnectAppTheme {
        var isLoading by remember { mutableStateOf(false) }
        var successMessage by remember { mutableStateOf<String?>(null) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        val scope = rememberCoroutineScope()

        val containerColor: Color
        val contentColor: Color
        val border: BorderStroke?
        val enabled = style != HCButtonStyle.DISABLED && !isLoading

        when (style) {
            HCButtonStyle.PRIMARY -> {
                containerColor = MaterialTheme.colorScheme.error
                contentColor = MaterialTheme.colorScheme.onError
                border = null
            }
            HCButtonStyle.SECONDARY -> {
                containerColor = MaterialTheme.colorScheme.onError
                contentColor = MaterialTheme.colorScheme.error
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.error)
            }
            HCButtonStyle.DISABLED -> {
                containerColor = Color(0xFFE0E0E0)
                contentColor = Color(0xFF9E9E9E)
                border = null
            }
        }

        Box(
            modifier = modifier.then(
                Modifier
                    .fillMaxWidth()
                    .then(if (width != Dp.Unspecified) Modifier.width(width) else Modifier)
            ),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        successMessage = null
                        errorMessage = null
                        onAction(
                            { msg -> successMessage = msg; isLoading = false; onSuccess() },
                            { err -> errorMessage = err; isLoading = false }
                        )
                    }
                },
                enabled = enabled,
                shape = shape,
                border = border,
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = contentColor,
                    disabledContainerColor = containerColor,
                    disabledContentColor = contentColor
                ),
                modifier = Modifier
                    .height(height)
                    .fillMaxWidth()
            ) {
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = contentColor,
                            strokeWidth = 2.dp,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 8.dp)
                        )
                        Text("Đang xử lý…", fontSize = textSize, fontWeight = FontWeight.Medium)
                    }
                } else {
                    Text(label, fontSize = textSize, fontWeight = FontWeight.Medium)
                }
            }

            successMessage?.let {
                AlertDialog(
                    onDismissRequest = { successMessage = null },
                    confirmButton = {
                        TextButton(onClick = { successMessage = null; onSuccess() }) {
                            Text("OK")
                        }
                    },
                    title = { Text("🎉 Thành công", fontWeight = FontWeight.Bold) },
                    text = { Text(it) }
                )
            }

            errorMessage?.let {
                AlertDialog(
                    onDismissRequest = { errorMessage = null },
                    confirmButton = {
                        TextButton(onClick = { errorMessage = null }) {
                            Text("Đóng")
                        }
                    },
                    title = { Text("❌ Lỗi", fontWeight = FontWeight.Bold) },
                    text = { Text(it) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionButtonStylesPreview() {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(16.dp)) {
        ActionButtonWithFeedback(
            label = "Yes",
            style = HCButtonStyle.PRIMARY,
            onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } }
        )
        ActionButtonWithFeedback(
            label = "Yes",
            style = HCButtonStyle.SECONDARY,
            onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } }
        )
        ActionButtonWithFeedback(
            label = "Yes",
            style = HCButtonStyle.DISABLED,
            onAction = { _, _ -> }
        )
    }
}