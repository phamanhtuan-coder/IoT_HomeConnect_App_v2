package com.sns.homeconnect_v2.presentation.component.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

/**
 * Một hộp thoại nhắc người dùng xác nhận hoặc từ chối yêu cầu chia sẻ quyền.
 *
 * @param userName Tên của người dùng đang chia sẻ thiết bị.
 * @param deviceId ID của thiết bị đang được chia sẻ.
 * @param onConfirm Một hàm callback được gọi khi người dùng xác nhận yêu cầu.
 * @param onDismiss Một hàm callback được gọi khi người dùng đóng hộp thoại.
 *
 * @author Nguyễn Thanh Sang
 * @since 21-05-2025
 */

@Composable
fun SharePermissionRequestDialog(
    userName: String,
    deviceId: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFD8E4E8),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Yêu cầu xác nhận quyền chia sẻ",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                // Message
                Text(
                    text = buildAnnotatedString {
                        append("Bạn được ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(userName)
                        }
                        append(" chia sẻ quyền sử dụng với ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(deviceId)
                        }
                        append(".\nBạn có muốn chấp nhận không?")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    lineHeight = 20.sp
                )

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionButtonWithFeedback(
                        label = "Từ chối",
                        style = HCButtonStyle.SECONDARY,
                        onAction = { _, _ -> onDismiss() },
                        modifier = Modifier.weight(1f),
                        textSize = 17.sp,
                        snackbarViewModel = snackbarViewModel
                    )
                    ActionButtonWithFeedback(
                        label = "Đồng ý",
                        style = HCButtonStyle.PRIMARY,
                        onAction = { _, _ -> onConfirm() },
                        modifier = Modifier.weight(1f),
                        textSize = 17.sp,
                        snackbarViewModel = snackbarViewModel
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewSharePermissionDialog() {
    MaterialTheme {
        SharePermissionRequestDialog(
            userName = "Nguyễn Văn A",
            deviceId = "ID12345",
            onConfirm = {},
            onDismiss = {}
        )
    }
}

