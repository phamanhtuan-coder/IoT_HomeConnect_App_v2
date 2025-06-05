package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

/**
 * Một Composable function hiển thị thông tin chi tiết về một thiết bị dưới dạng thẻ.
 *
 * Thẻ này bao gồm loại thiết bị, trình duyệt, địa chỉ IP, vị trí và thời gian truy cập cuối cùng.
 * Nó cũng cung cấp một nút "Đăng xuất".
 *
 * @param deviceType Loại thiết bị (ví dụ: "Android", "iOS").
 * @param browser Trình duyệt được sử dụng trên thiết bị (ví dụ: "Chrome", "Safari").
 * @param ip Địa chỉ IP của thiết bị.
 * @param location Vị trí địa lý của thiết bị.
 * @param lastAccess Một chuỗi biểu thị thời gian cuối cùng thiết bị truy cập dịch vụ.
 * @param modifier [Modifier] tùy chọn để áp dụng cho thẻ.
 *
 * @author Nguyễn Thanh Sang
 * @since 27-05-2025
 */
@Composable
fun DeviceDetailCard(
    deviceType: String,
    browser: String,
    ip: String,
    location: String,
    lastAccess: String,
    modifier: Modifier = Modifier,
    snackbarViewModel: SnackbarViewModel
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Chi tiết thiết bị",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(12.dp))

            InfoLine(label = "Loại:", value = deviceType)
            InfoLine(label = "Trình duyệt:", value = browser)
            InfoLine(label = "Địa chỉ IP:", value = ip)
            InfoLine(label = "Vị trí:", value = location)
            InfoLine(label = "Lần truy cập gần nhất:", value = lastAccess)

            Spacer(Modifier.height(18.dp))

            ActionButtonWithFeedback(
                label = "Đăng xuất",
                style = HCButtonStyle.PRIMARY,
                onAction = { _, _ -> },
                snackbarViewModel = snackbarViewModel
            )
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF424242),
            fontSize = 16.sp
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = value,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF757575),
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 350, heightDp = 380)
@Composable
fun DeviceDetailCardPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFFF6F6F8))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DeviceDetailCard(
            deviceType = "Androd",
            browser = "Chrome",
            ip = "192.168.1.1",
            location = "Hà Nội, VN",
            lastAccess = "2 giờ trước",
            snackbarViewModel = SnackbarViewModel() // Giả lập ViewModel cho preview
        )
    }
}
