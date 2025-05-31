package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.presentation.model.DeviceStatCardItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeviceStatCardCarousel(
    items: List<DeviceStatCardItem>,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 300.dp,
) {
    /* ---------- setup kích thước & state ---------- */
    val cardSpacing = 12.dp
    val edgePadding = (LocalConfiguration.current.screenWidthDp.dp - cardWidth) / 2
    val virtualCount = Int.MAX_VALUE
    val startIndex = remember { (virtualCount / 2) - (virtualCount / 2 % items.size) }

    val listState = rememberLazyListState(startIndex)
    val fling = rememberSnapFlingBehavior(listState)
    val coroutine = rememberCoroutineScope()

    /* Trang hiện tại (theo item thực) */
    val currentRealIndex by remember {
        derivedStateOf { (listState.firstVisibleItemIndex) % items.size }
    }

    Column(modifier) {
        /* ---------- Carousel ---------- */
        LazyRow(
            state = listState,
            flingBehavior = fling,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(cardSpacing),
            contentPadding = PaddingValues(
                start = edgePadding,
                end = edgePadding,
                top = 8.dp,
                bottom = 8.dp
            )
        ) {
            items(virtualCount) { vIndex ->
                val rIndex = vIndex % items.size
                DeviceStatCard(
                    item = items[rIndex],
                    modifier = Modifier
                        .width(cardWidth)
                        .height(120.dp)
                )
            }
        }

        /* ---------- QUICK PICK INDICATOR ---------- */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, _ ->
                val selected = index == currentRealIndex
                Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (selected) 10.dp else 6.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                    .clickable {
                        /* Tìm vị trí ảo gần nhất của item cần chọn, rồi animate tới đó */
                        val target =
                            listState.firstVisibleItemIndex +
                                    (index - currentRealIndex)
                        coroutine.launch {
                            listState.animateScrollToItem(target)
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 600)
@Composable
fun DeviceStatCardCarouselPreview() {
    val items = listOf(
        DeviceStatCardItem(Icons.Default.Memory , Color(0xFFF54B63), "Tổng số thiết bị", "12 thiết bị"),
        DeviceStatCardItem(Icons.Default.Wifi   , Color(0xFF23D37F), "Đang hoạt động" ,  "10 thiết bị"),
        DeviceStatCardItem(Icons.Default.WifiOff, Color(0xFFF54B63), "Mất kết nối"     ,  "2 thiết bị")
    )
    DeviceStatCardCarousel(items = items)
}
