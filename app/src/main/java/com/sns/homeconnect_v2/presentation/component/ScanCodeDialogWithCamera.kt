package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
    code: String,
    onDismiss: () -> Unit,
    onOk: () -> Unit,
) = Dialog(onDismissRequest = onDismiss) {

    /* Khung dialog – bạn kiểm soát padding / màu / bo góc */
    Surface(
        shape  = RoundedCornerShape(20.dp),
        color  = Color(0xFFF4F0F8),           // nền tím nhạt
        tonalElevation = 8.dp,                // đổ bóng nhẹ
        modifier = Modifier                   // 16 dp sát viền như bạn muốn
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)              // <-- padding bên trong tuỳ ý
        ) {

            /* Camera preview */
            QrCameraPreview(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) { /* handle scan */ }

            Spacer(Modifier.height(8.dp))

            /* Mã quét */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFDDDDDD), RoundedCornerShape(12.dp))
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(code, fontSize = 18.sp)
            }

            Spacer(Modifier.height(8.dp))

            /* Nút OK dùng ActionButtonWithFeedback */
            ActionButtonWithFeedback(
                label  = "OK",
                style  = HCButtonStyle.PRIMARY,
                height = 56.dp,
                width  = 200.dp,
                onAction = { onSuccess, _ ->
                    // Loading 2 s
                    delay(2000)
                    onSuccess("Thành công")
                    onOk()         // gọi callback OK
                    onDismiss()    // đóng dialog
                },
                snackbarViewModel = SnackbarViewModel() // Mock ViewModel cho snackbar
            )
        }
    }
}

/* -------- Preview: dùng previewMode = true để mock camera -------- */
@Preview(showBackground = true, widthDp = 360)
@Composable
fun ScanCodeDialogPreview() {
    /* Khi preview, Compose set LocalInspectionMode = true  */
    CompositionLocalProvider(LocalInspectionMode provides true) {
        ScanCodeDialog(
            code      = "1234-5678-6565-3333",
            onDismiss = {},
            onOk      = {}
        )
    }
}
