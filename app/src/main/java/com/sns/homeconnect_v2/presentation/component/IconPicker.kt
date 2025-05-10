package com.sns.homeconnect_v2.presentation.component

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Hiển thị danh sách icon để chọn
 * --------------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 11/12/2024
 * Lần cập nhật cuối: 13/12/2024
 *
 * --------------------------------------------
 *  @param icons danh sách icon
 *  @param selectedIcon icon được chọn
 *  @return Column chứa danh sách icon
 *
 */
@Composable
fun IconPicker(
    icons: List<Pair<ImageVector, String>>,
    selectedIcon: MutableState<String>
) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(
                8.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Chọn biểu tượng:",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                color = colorScheme.onBackground
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.background),
                content = {
                    items(icons.size) { index ->
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    selectedIcon.value = icons[index].second
                                }
                        ) {
                            Icon(
                                imageVector = icons[index].first,
                                contentDescription = null,
                                tint = if (selectedIcon.value == icons[index].second) colorScheme.primary else colorScheme.onBackground,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = icons[index].second,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            )
        }
    }
}