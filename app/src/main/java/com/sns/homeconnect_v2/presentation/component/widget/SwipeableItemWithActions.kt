package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeableItemWithActions(
    isRevealed: Boolean,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val offset = remember { Animatable(0f) }
    val maxSwipeWidthPx = with(LocalDensity.current) { 112.dp.toPx() }

    LaunchedEffect(isRevealed) {
        if (isRevealed) {
            offset.animateTo(-maxSwipeWidthPx)
        } else {
            offset.animateTo(0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(maxSwipeWidthPx) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(-maxSwipeWidthPx, 0f)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            scope.launch {
                                if (offset.value <= -maxSwipeWidthPx / 2f) {
                                    offset.animateTo(-maxSwipeWidthPx)
                                    onExpanded()
                                } else {
                                    offset.animateTo(0f)
                                    onCollapsed()
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}