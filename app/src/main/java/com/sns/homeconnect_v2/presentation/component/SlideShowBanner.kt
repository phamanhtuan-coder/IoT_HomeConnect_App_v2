package com.sns.homeconnect_v2.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sns.homeconnect_v2.presentation.model.SlideShowItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SlideShowBanner(
    listSlideShow: List<SlideShowItem>,
    modifier: Modifier = Modifier,
    height: Dp = 250.dp,
    cornerRadius: Dp = 16.dp,
    loadingColor: Color = Color(0xFF5D9EFF)
) {
    if (listSlideShow.isNotEmpty()) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { listSlideShow.size }
        )
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(listSlideShow.size) {
            while (true) {
                // Đợi user kéo xong
                while (pagerState.isScrollInProgress) {
                    delay(50)
                }
                delay(3000)
                if (listSlideShow.isNotEmpty()) {
                    val nextPage = (pagerState.currentPage + 1) % listSlideShow.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }

        Box(
            modifier = modifier
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .clip(RoundedCornerShape(cornerRadius))
                    .shadow(4.dp, RoundedCornerShape(cornerRadius))
            ) { page ->
                SlideImage(
                    painter = rememberAsyncImagePainter(model = listSlideShow[page].image),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                listSlideShow.forEachIndexed { index, _ ->
                    val isActive = index == pagerState.currentPage
                    val animatedWidth by animateFloatAsState(
                        targetValue = if (isActive) 32f else 12f,
                        animationSpec = tween(300), label = ""
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .size(width = animatedWidth.dp, height = 6.dp)
                            .background(
                                color = if (isActive) Color(0xFF1E88E5) else Color(0xFFB0BEC5),
                                shape = RoundedCornerShape(3.dp)
                            )
                            .clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                    )
                }
            }
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = loadingColor,
                strokeWidth = 4.dp,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

// Preview dùng thử:
@Preview
@Composable
fun SlideShowBannerPreview() {
    val dummyList = listOf(
        SlideShowItem("https://picsum.photos/id/1/600/300"),
        SlideShowItem("https://picsum.photos/id/2/600/300"),
        SlideShowItem("https://picsum.photos/id/3/600/300")
    )
    SlideShowBanner(listSlideShow = dummyList)
}
