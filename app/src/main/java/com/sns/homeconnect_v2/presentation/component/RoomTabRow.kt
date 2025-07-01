package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.presentation.model.SpaceTab

/**
 * Một hàm Composable hiển thị một hàng các tab, mỗi tab đại diện cho một phòng.
 * Nó có thể là một hàng tab có thể cuộn hoặc một hàng tab cố định tùy thuộc vào số lượng phòng
 * và tham số `tabPerScreen`.
 *
 * @param activeRoom ID của phòng hiện đang hoạt động.
 * @param onRoomChange Một hàm callback được gọi khi một tab phòng được nhấp vào.
 *                     Nó nhận ID của phòng đã chọn làm tham số.
 * @param rooms Một danh sách các đối tượng [Room] sẽ được hiển thị dưới dạng tab.
 *              Mặc định là một danh sách các phòng phổ biến được xác định trước.
 * @param tabPerScreen Số lượng tab tối đa hiển thị trên màn hình cùng một lúc mà không cần cuộn.
 *                     Nếu số lượng phòng vượt quá giá trị này, một [ScrollableTabRow] sẽ được sử dụng.
 */
@Composable
fun RoomTabRow(
    activeRoom: String,
    onRoomChange: (String) -> Unit,
    rooms: List<SpaceTab>,
    tabPerScreen: Int = 4
) {
    val selectedIndex = rooms.indexOfFirst { it.id == activeRoom }.coerceAtLeast(0)
    val useScrollable = rooms.size > tabPerScreen

    if (useScrollable) {
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            edgePadding = 0.dp,
            containerColor = Color.White,
            contentColor = Color.Black,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    height = 3.dp,
                    color = Color(0xFF2979FF)
                )
            }
        ) {
            rooms.forEachIndexed { index, room ->
                val isActive = index == selectedIndex
                Tab(
                    selected = isActive,
                    onClick = { onRoomChange(room.id) },
                    selectedContentColor = Color(0xFF2979FF),
                    unselectedContentColor = Color(0xFF404B5A),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    RoomTabContent(room, isActive)
                }
            }
        }
    } else {
        TabRow(
            selectedTabIndex = selectedIndex,
            containerColor = Color.White,
            contentColor = Color.Black,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    height = 3.dp,
                    color = Color(0xFF2979FF)
                )
            }
        ) {
            rooms.forEachIndexed { index, room ->
                val isActive = index == selectedIndex
                Tab(
                    selected = isActive,
                    onClick = { onRoomChange(room.id) },
                    selectedContentColor = Color(0xFF2979FF),
                    unselectedContentColor = Color(0xFF404B5A),
                ) {
                    RoomTabContent(room, isActive)
                }
            }
        }
    }
}

