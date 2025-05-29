package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một composable function hiển thị một modal bottom sheet để chọn khoảng thời gian.
 *
 * Bottom sheet này cung cấp một tập hợp các tùy chọn khoảng thời gian đặt trước và cho phép người dùng
 * chọn một khoảng ngày tùy chỉnh bằng cách sử dụng date pickers.
 *
 * @param open Cờ Boolean để kiểm soát khả năng hiển thị của bottom sheet.
 * @param onDismiss Hàm callback được gọi khi bottom sheet bị loại bỏ.
 * @param selected Tùy chọn khoảng thời gian hiện được chọn (chuỗi).
 * @param onSelect Hàm callback được gọi khi một tùy chọn khoảng thời gian đặt trước được chọn.
 *                 Nó truyền chuỗi tùy chọn đã chọn làm đối số.
 * @param onCustomDate Hàm callback được gọi khi một khoảng ngày tùy chỉnh được chọn và áp dụng.
 *                     Nó truyền chuỗi ngày bắt đầu và ngày kết thúc làm đối số.
 * @param presetOptions Một danh sách các chuỗi đại diện cho các tùy chọn khoảng thời gian đặt trước.
 *                      Mặc định là một danh sách được xác định trước: "Hôm nay", "Hôm qua", "7 ngày qua", "30 ngày qua", "Tùy chỉnh".
 *
 * @author Nguyễn Thanh Sang
 * @since 29-05-2025
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickTimeRangeBottomSheet(
    open: Boolean,
    onDismiss: () -> Unit,
    selected: String,
    onSelect: (String) -> Unit,
    onCustomDate: (String, String) -> Unit,
    presetOptions: List<String> = listOf("Hôm nay", "Hôm qua", "7 ngày qua", "30 ngày qua", "Tùy chỉnh")
) {
    var isCustom by remember { mutableStateOf(selected == "Tùy chỉnh") }
    var customStart by remember { mutableStateOf("") }
    var customEnd by remember { mutableStateOf("") }

    if (open) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = null
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 0.dp, vertical = 0.dp)
            ) {
                // Header
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 20.dp, end = 12.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chọn thời gian",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Đóng")
                    }
                }

                HorizontalDivider()

                // Preset chọn nhanh, scroll ngang
                Row(
                    Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 16.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    presetOptions.forEach { option ->
                        val active = (selected == option) || (isCustom && option == "Tùy chỉnh")
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (active) Color(0xFF2979FF) else Color.White)
                                .border(
                                    2.dp,
                                    if (active) Color(0xFF2979FF) else Color(0xFFDEE1E6),
                                    shape = CircleShape
                                )
                                .clickable {
                                    if (option == "Tùy chỉnh") {
                                        isCustom = true
                                    } else {
                                        isCustom = false
                                        onSelect(option)
                                        onDismiss()
                                    }
                                }
                                .padding(horizontal = 22.dp, vertical = 13.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                color = if (active) Color.White else Color(0xFF353A40),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                // Tùy chỉnh ngày
                if (isCustom) {
                    HorizontalDivider()
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Ngày bắt đầu",
                        color = Color(0xFF6B7280),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 20.dp, bottom = 6.dp)
                    )
                    DatePickerTextField(
                        label = "Ngày bắt đầu",
                        value = customStart,
                        onValueChange = { customStart = it },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "Ngày kết thúc",
                        color = Color(0xFF6B7280),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 20.dp, bottom = 6.dp)
                    )
                    DatePickerTextField(
                        label = "Ngày kết thúc",
                        value = customEnd,
                        onValueChange = { customEnd = it },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = {
                            if (customStart.isNotBlank() && customEnd.isNotBlank()) {
                                onCustomDate(customStart, customEnd)
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2979FF),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 12.dp
                        )
                    ) {
                        Text(
                            "Áp dụng",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.1.sp,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuickTimeRangeBottomSheet() {
    var date by remember { mutableStateOf("29/05/2025") }

    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            DatePickerTextField(
                label = "Chọn ngày",
                value = date,
                onValueChange = { date = it }
            )
        }
    }
}
