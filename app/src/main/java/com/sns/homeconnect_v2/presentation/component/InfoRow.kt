package com.sns.homeconnect_v2.presentation.component

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một hàm có thể kết hợp (composable function) hiển thị một hàng thông tin với nhãn, giá trị, đơn vị và chỉ báo trạng thái.
 * Thành phần này được thiết kế để trình bày dữ liệu một cách rõ ràng và ngắn gọn, phù hợp cho bảng điều khiển hoặc màn hình thông tin.
 *
 * Bố cục bao gồm ba phần chính:
 * 1. **Nhãn (Label)**: Hiển thị ở bên trái, mô tả dữ liệu đang được hiển thị.
 * 2. **Giá trị và Đơn vị (Value and Unit)**: Hiển thị ở giữa, với giá trị nổi bật hơn và đơn vị hơi lệch và nhỏ hơn.
 * 3. **Chỉ báo trạng thái (State Indicator)**: Một vòng tròn màu ở bên phải, chứa văn bản cho biết trạng thái liên quan đến dữ liệu.
 *
 * `InfoRow` sử dụng `IoTHomeConnectAppTheme` để tạo kiểu nhất quán với phần còn lại của ứng dụng.
 * Nó sử dụng các composable `Row` và `Box` để bố cục, và các composable `Text` để hiển thị thông tin.
 * `MaterialTheme.colorScheme` được sử dụng để đảm bảo rằng màu văn bản thích ứng với chủ đề hiện tại (sáng/tối).
 *
 * @param label Văn bản cho nhãn, được định vị ở phía bên trái của hàng.
 * @param value Giá trị dữ liệu chính, được hiển thị nổi bật ở giữa.
 * @param unit Đơn vị tương ứng với giá trị, được hiển thị bên cạnh nó với phông chữ nhỏ hơn.
 * @param stateColor Màu nền cho chỉ báo trạng thái hình tròn ở bên phải.
 * @param stateText Văn bản được hiển thị bên trong chỉ báo trạng thái hình tròn (ví dụ: viết tắt trạng thái hoặc biểu tượng).
 */
@Composable
fun InfoRow(label: String, value: String, unit: String, stateColor: Color, stateText: String)
{
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                color = colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.weight(2f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = unit,
                    fontSize = 12.sp,
                    modifier = Modifier.offset(y = 3.dp),
                    color = colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(stateColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stateText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}