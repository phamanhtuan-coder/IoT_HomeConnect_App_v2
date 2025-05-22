package com.sns.homeconnect_v2.presentation.component

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sns.homeconnect_v2.core.util.rememberResponsiveLayoutConfig
import com.sns.homeconnect_v2.presentation.component.dialog.WarningDialog
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Một hàm Composable hiển thị một thẻ thông tin về người dùng được chia sẻ.
 *
 * Thẻ này bao gồm ảnh đại diện của người dùng, tên, email, ngày họ được chia sẻ
 * và các nút "Sửa quyền" và "Gỡ quyền".
 *
 * Nút "Gỡ quyền" sẽ kích hoạt một hộp thoại xác nhận trước khi thực hiện hành động.
 *
 * @param userAvt URL hoặc mã định danh tài nguyên cho ảnh đại diện của người dùng.
 * @param userName Tên của người dùng được chia sẻ.
 * @param userEmail Địa chỉ email của người dùng được chia sẻ.
 * @param sharedDate Ngày mà mục được chia sẻ với người dùng (ví dụ: "20/05/2025").
 * @param modifier [Modifier] tùy chọn để tùy chỉnh bố cục và giao diện của thẻ.
 *
 * @see UserAvatar
 * @see ActionButtonWithFeedback
 * @see WarningDialog
 *
 * @author Nguyễn Thanh Sang
 * @since 21-05-2025
 */
@Composable
fun SharedUserCard(
    userAvt: String,
    userName: String,
    userEmail: String,
    sharedDate: String,
//    permissionId: Int,
//    onRevokeClick: (Int) -> Unit
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    /* state hiển thị dialog xác nhận */
    var showConfirm by remember { mutableStateOf(false) }
    var isButtonLoading by remember { mutableStateOf(false) }
    var pendingOnSuccess by remember { mutableStateOf<((String) -> Unit)?>(null) }
    var pendingOnError   by remember { mutableStateOf<((String) -> Unit)?>(null) }

    IoTHomeConnectAppTheme {
        // Lấy thông tin layout responsive từ config
        val colorScheme = MaterialTheme.colorScheme
        val layoutConfig = rememberResponsiveLayoutConfig()
        Card(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFD8E4E8)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Hàng đầu tiên: Hiển thị tên và email người dùng
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Vùng hiển thị Avatar hoặc Icon
                    UserAvatar(userAvt)

                    Spacer(modifier = Modifier.width(12.dp))

                    Column (
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Tên người dùng
                        Text(
                            text = userName,
                            fontSize = layoutConfig.textFontSize,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                        // Email người dùng
                        Text(
                            text = userEmail,
                            fontSize = layoutConfig.textFontSize * 0.9f,
                            color = colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }

                // Ngày chia sẻ quyền
                Text(
                    text = "Chia sẻ ngày: $sharedDate",
                    fontSize = layoutConfig.textFontSize * 0.9f,
                    color = colorScheme.onSurfaceVariant
                )

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ActionButtonWithFeedback(
                        label = "Sửa quyền",
                        style = HCButtonStyle.SECONDARY,
                        onAction = { _, _ -> },
                        modifier = Modifier.weight(1f),
                        textSize = 20.sp
                    )

                    ActionButtonWithFeedback(
                        label  = "Gỡ quyền",
                        style  = HCButtonStyle.PRIMARY,
                        // 1) Hành động thật của bạn
                        onAction = { onSuccessCallback, onErrorCallback ->
                            /* 1. Lưu callback → chờ xác nhận */
                            pendingOnSuccess = onSuccessCallback
                            pendingOnError   = onErrorCallback
                            showConfirm      = true
                        },
                        // 2) Tùy chỉnh tiêu đề dialog
                        successDialogTitle = "✅ Thành công",
                        errorDialogTitle   = "⚠️ Lỗi",
                        isLoadingFromParent = isButtonLoading,
                        modifier = Modifier.weight(1f),
                        textSize = 20.sp
                    )
                }
                if (showConfirm) {
                    WarningDialog(
                        title    = "Xác nhận gỡ quyền",
                        text  = "Bạn có chắc muốn gỡ quyền của người dùng này?",
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
                                if (isActionSuccessful) onSuccessCallback?.invoke("Quyền đã được gỡ thành công!")
                                else      onErrorCallback?.invoke("Thao tác thất bại, vui lòng thử lại.")
                            }
                            isButtonLoading = true
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

@Preview(showBackground = true)
@Composable
fun PreviewSharedUserCard() {
    SharedUserCard(
        userAvt = "https://i.pravatar.cc/150?img=8",
        userName = "Nguyễn Văn A",
        userEmail = "vana@example.com",
        sharedDate = "20/05/2025",
//        permissionId = 123,
//        onRevokeClick = { id ->
//            // Chức năng xử lý gỡ quyền (mock)
//            println("Gỡ quyền với ID: $id")
//        }
    )
}