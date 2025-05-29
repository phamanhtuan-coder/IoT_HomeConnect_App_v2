package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Hàm Composable để hiển thị một widget thời tiết.
 *
 * Widget này hiển thị một biểu tượng, một giá trị và một nhãn, thường đại diện
 * cho một thông tin thời tiết (ví dụ: nhiệt độ, độ ẩm).
 * Giao diện và kích thước của widget có thể được điều chỉnh dựa trên việc
 * nó được hiển thị trên máy tính bảng hay thiết bị nhỏ hơn.
 *
 * @param icon [ImageVector] sẽ được hiển thị làm biểu tượng cho widget.
 * @param value [String] giá trị sẽ được hiển thị (ví dụ: "25°C", "70%").
 * @param label [String] nhãn mô tả giá trị (ví dụ: "Nhiệt độ", "Độ ẩm").
 * @param isTablet [Boolean] cho biết widget có đang được hiển thị trên máy tính bảng hay không.
 *                 Điều này ảnh hưởng đến padding, chiều rộng, chiều cao và kích thước phông chữ.
 *
 */
@Composable
fun WeatherWidget(
    icon: ImageVector,
    value: String,
    label: String,
    isTablet: Boolean
) {
    Card(
        modifier = Modifier
            .padding(if (isTablet) 12.dp else 5.dp)
            .width(if (isTablet) 150.dp else 100.dp)
            .height(if (isTablet) 80.dp else 45.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = Color.White.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(15.dp)
                )
                .fillMaxSize()
                .padding(horizontal = if (isTablet) 16.dp else 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .size(if (isTablet) 45.dp else 22.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF424242),
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = label,
                    color = Color(0xFF424242),
                    fontSize = if (isTablet) 24.sp else 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = value,
                    color = Color(0xFF424242),
                    fontSize = if (isTablet) 20.sp else 12.sp
                )
            }
        }
    }
}