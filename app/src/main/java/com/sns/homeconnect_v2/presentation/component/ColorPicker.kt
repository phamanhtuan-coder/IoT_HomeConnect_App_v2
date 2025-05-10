package com.sns.homeconnect_v2.presentation.component

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Hiển thị danh sách màu để chọn
 * --------------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 10/05/2025
 * Lần cập nhật cuối: 10/05/2025
 *
 * --------------------------------------------
 * @param colors danh sách cặp màu và tên
 * @param selectedColor tên màu được chọn
 * @return Column chứa danh sách màu
 */
@Composable
fun ColorPicker(
    colors: List<Pair<Color, String>>,
    selectedColor: MutableState<String>
) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(
                8.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Text(
                text = "Chọn màu sắc:",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                color = colorScheme.onBackground
            )

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                colors.forEach { colorPair ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(colorPair.first, CircleShape)
                            .border(
                                width = if (selectedColor.value == colorPair.second) 1.dp else 0.dp,
                                color = if (selectedColor.value == colorPair.second) colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { selectedColor.value = colorPair.second }
                    )
                }
            }
        }
    }
}