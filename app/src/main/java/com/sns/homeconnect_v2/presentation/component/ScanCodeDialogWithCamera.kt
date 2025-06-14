package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import kotlinx.coroutines.delay

/**
 * Một dialog hiển thị camera preview để quét mã QR, mã đã quét và nút OK.
 *
 * @param code Mã đã quét để hiển thị.
 * @param onDismiss Callback được gọi khi dialog bị đóng.
 * @param onOk Callback được gọi khi nút OK được nhấp.
 * @author Nguyễn Thanh Sang
 * @since 19-05-2025
 */
@Composable
fun ScanCodeDialog(
    onDismiss: () -> Unit,
    onOk: (String) -> Unit
) = Dialog(onDismissRequest = onDismiss) {
    var scannedCode by remember { mutableStateOf("") }
    var hasScanned by remember { mutableStateOf(false) }

    // Tự động xử lý sau khi quét
    LaunchedEffect(scannedCode) {
        if (scannedCode.isNotBlank() && !hasScanned) {
            hasScanned = true
            delay(300)
            onOk(scannedCode)
            onDismiss()
        }
    }

    // Kích thước cố định cho dialog
    Surface(
        modifier = Modifier
            .width(320.dp)         // 👈 đảm bảo vừa với màn hình nhỏ
            .height(480.dp),       // 👈 FIXED chiều cao tránh lố
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF4F0F8),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Camera với tỷ lệ chính xác (VD: 3:4)
            QrCameraPreview(
                modifier = Modifier
                    .width(280.dp)
                    .height(210.dp) // 👈 ~3:4, giữ cứng
                    .clip(RoundedCornerShape(12.dp)),
                onCodeScanned = { code -> scannedCode = code }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFDDDDDD), RoundedCornerShape(12.dp))
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (scannedCode.isNotBlank()) scannedCode else "Đang quét...",
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            ActionButtonWithFeedback(
                label = "OK",
                style = HCButtonStyle.PRIMARY,
                height = 56.dp,
                width = 200.dp,
                onAction = { onSuccess, _ ->
                    delay(2000)
                    onSuccess("Thành công")
                    onOk(scannedCode)
                    onDismiss()
                },
                snackbarViewModel = SnackbarViewModel()
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun ScanCodeDialogPreview() {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        ScanCodeDialog(
            onDismiss = {},
            onOk = { /* Handle scanned code */ }
        )
    }
}

