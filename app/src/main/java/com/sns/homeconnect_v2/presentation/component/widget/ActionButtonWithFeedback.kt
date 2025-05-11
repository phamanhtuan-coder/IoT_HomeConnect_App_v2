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
 * Một nút hành động 3‑trạng‑thái (Primary, Secondary, Disabled) kèm loading + dialog phản hồi.
 */

enum class HCButtonStyle { PRIMARY, SECONDARY, DISABLED }

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
