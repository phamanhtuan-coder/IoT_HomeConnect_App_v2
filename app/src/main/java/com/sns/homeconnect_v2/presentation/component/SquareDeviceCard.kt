package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.data.remote.dto.response.SharedWithResponse


/**
 * Giao diện Thẻ thiết bị
 * -----------------------------------------
 * - Người viết: Phạm Anh Tuấn
 * - Ngày viết: 29/11/2024
 * - Lần cập nhật cuối: 11/12/2024
 * -----------------------------------------
 *
 * @param devicePicture: Hình ảnh thiết bị
 * @param deviceName: Tên thiết bị
 * @param deviceType: Loại thiết bị
 * @param deviceLocation: Vị trí thiết bị
 * @param deviceStatus: Trạng thái thiết bị
 *
 * @return Card chứa thông tin thiết bị
 * ---------------------------------------
 */
@Composable
fun DeviceCard(
    device: SharedWithResponse,
    navigator: NavHostController,
    devicePicture: ImageVector = Icons.Filled.Devices,
    deviceName: String = "Tên thiết bị",
    deviceType: String = "Loại thiết bị",
//    deviceLocation: String = "Vị trí",
//    deviceStatus: Boolean = true
) {
   return Card(
        modifier = Modifier
            .padding(8.dp)
            .size(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                onClick = {
                    navigator.navigate("device/${device.Device.TypeID}/${device.Device.DeviceID}")
                }
            )
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5),
            contentColor = Color.Black
        ),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Device Icon
                Icon(
                    imageVector = devicePicture,
                    contentDescription = "Device Icon",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp)
                )

                // Device Name
                Text(
                    text = deviceName,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                // Device Type
                Text(
                    text = deviceType,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

//                // Spaces
//                Text(
//                    text = deviceLocation,
//                    color = Color.Gray,
//                    fontSize = 14.sp,
//                    textAlign = TextAlign.Center
//                )

                // Device Status Switch
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.End,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp)
//                ) {
////                    Text(
////                        text = if (deviceStatus) "Hoạt động" else "Tắt",
////                        color = if (deviceStatus) Color(0xFF4CAF50) else Color.Gray,
////                        fontSize = 12.sp,
////                        fontWeight = FontWeight.Medium,
////                        textAlign = TextAlign.Center,
////                        modifier = Modifier.padding(end = 18.dp) // Add padding to position the text
////                    )
////                    Switch(
////                        checked = deviceStatus,
////                        onCheckedChange = { /* TODO: Implement toggle functionality */ },
////                        colors = SwitchDefaults.colors(
////                            checkedThumbColor = Color(0xFF4CAF50),
////                            uncheckedThumbColor = Color.Gray,
////                            checkedTrackColor = Color(0xFFC8E6C9),
////                            uncheckedTrackColor = Color(0xFFE0E0E0)
////                        ),
////                        modifier = Modifier.size(30.dp)
////                    )
//                }

            }
        }
    )
}