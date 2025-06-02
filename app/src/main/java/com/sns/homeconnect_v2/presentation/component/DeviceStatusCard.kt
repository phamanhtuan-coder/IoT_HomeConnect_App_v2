package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Một hàm Composable hiển thị thẻ thông tin trạng thái thiết bị.
 *
 * @author Nguyễn Thanh Sang
 * @since 25-05-2025
 *
 * @param title Tiêu đề của thiết bị.
 * @param type Loại của thiết bị.
 * @param isOn Một giá trị boolean cho biết thiết bị đang bật hay tắt.
 * @param powerConsumption Một chuỗi đại diện cho mức tiêu thụ điện năng của thiết bị.
 * @param temperature Một chuỗi đại diện cho nhiệt độ của thiết bị.
 * @param onDetailsButtonClick Một hàm lambda sẽ được thực thi khi nút chi tiết được nhấp.
 * @param modifier Một Modifier sẽ được áp dụng cho thẻ.
 * @param icon Biểu tượng sẽ được hiển thị cho thiết bị. Mặc định là biểu tượng thiết bị chung.
 * @param iconBackgroundColor Màu nền của biểu tượng. Mặc định là màu xám đậm.
 */
@Composable
fun DeviceStatusCard(
    title: String,
    type: String,
    isOn: Boolean,
    powerConsumption: String,
    temperature: String,
    onDetailsButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Devices,
    iconBackgroundColor: Color = Color(0xFF23282F)
) {
    Box(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(9.dp))
            .background(Color(0xFFD8E4E8), RoundedCornerShape(18.dp))
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon thiết bị
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Thông tin
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF222B45)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Loại: " + type,
                        fontSize = 13.sp,
                        color = Color(0xFF8590A2),
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = if (isOn) "● Đang bật" else "● Đang tắt",
                        fontSize = 13.sp,
                        color = if (isOn) Color(0xFF00C853) else Color(0xFFD32F2F),
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Text(
                    text = "Điện: $powerConsumption | Nhiệt độ: $temperature",
                    fontSize = 13.sp,
                    color = Color(0xFF8E99A7),
                    maxLines = 1,
                )
            }

            // Nút chi tiết
            Button(
                onClick = onDetailsButtonClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text(
                    text = "Chi tiết",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewDeviceStatusCardd() {
    DeviceStatusCard(
        title = "MÁY BÁO KHÓI",
        type = "Báo cháy",
        isOn = true,
        powerConsumption = "0.0 W",
        temperature = "0.0 độ",
        onDetailsButtonClick = {}
    )
}

