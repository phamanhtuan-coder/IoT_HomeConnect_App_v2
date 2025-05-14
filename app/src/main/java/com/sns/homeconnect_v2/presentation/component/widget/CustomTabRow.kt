package com.sns.homeconnect_v2.presentation.component.widget

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun CustomTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    IoTHomeConnectAppTheme {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = modifier
                .fillMaxWidth(),
            containerColor = Color.White,
            contentColor = Color.Black,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = MaterialTheme.colorScheme.primary,       // màu bạn muốn, ví dụ xanh đậm cùng header
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index

                Tab(
                    selected = isSelected,
                    onClick = { onTabSelected(index) },
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color.DarkGray,
                    text = {
                        Text(
                            text  = title,
                            style = MaterialTheme.typography.bodyMedium.copy(     // ← base = bodyMedium
                                fontSize   = 18.sp,                               // ← cỡ chữ lớn hơn
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTabRowPreview() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Chi tiết", "Thành viên")

    // Thay bằng IoTHomeConnectAppTheme nếu có
    IoTHomeConnectAppTheme {
        Column {
            CustomTabRow(
                tabs = tabTitles,
                selectedTabIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}


