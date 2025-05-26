package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Một composable cung cấp một mục có thể vuốt với các hành động.
 *
 * Composable này cho phép người dùng vuốt một mục theo chiều ngang để hiển thị các hành động bên dưới,
 * chẳng hạn như các nút chỉnh sửa hoặc xóa. Nó xử lý hoạt ảnh và phát hiện cử chỉ cho thao tác vuốt.
 *
 * @param isRevealed Một boolean cho biết liệu các hành động có hiện đang được tiết lộ hay không.
 *                   Điều này có thể được sử dụng để điều khiển trạng thái được tiết lộ theo chương trình.
 * @param modifier [Modifier] tùy chọn được áp dụng cho Box gốc của composable này.
 * @param onExpanded Một lambda gọi lại được gọi khi mục được vuốt mở (các hành động được tiết lộ hoàn toàn).
 * @param onCollapsed Một lambda gọi lại được gọi khi mục được vuốt đóng (các hành động bị ẩn).
 * @param actions Một lambda composable xác định nội dung của các hành động được tiết lộ khi vuốt.
 *                Lambda này được gọi trong một [RowScope], vì vậy các hành động được bố trí theo chiều ngang.
 * @param content Một lambda composable xác định nội dung chính của mục hiển thị theo mặc định
 *                và có thể được vuốt.
 */
@Composable
fun SwipeableItemWithActions(
    isRevealed: Boolean,
    onExpanded: () -> Unit,
    onCollapsed: () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit
) {
    val offset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Tự động đo chiều rộng action buttons
    val swipeWidthPx = remember { mutableFloatStateOf(0f) }

    Box(modifier = Modifier.fillMaxWidth()) {
        // --- Actions ---
        Row(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    swipeWidthPx.floatValue = coordinates.size.width.toFloat()
                }
                .wrapContentWidth()
                .align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )

        // --- Sliding Content ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(swipeWidthPx.floatValue) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, drag ->
                            scope.launch {
                                val newOffset = (offset.value + drag).coerceIn(-swipeWidthPx.floatValue, 0f)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            scope.launch {
                                if (offset.value < -swipeWidthPx.floatValue / 2f) {
                                    offset.animateTo(-swipeWidthPx.floatValue)
                                    onExpanded()
                                } else {
                                    offset.animateTo(0f)
                                    onCollapsed()
                                }
                            }
                        }
                    )
                },
            shape = RoundedCornerShape(16.dp)
        ) {
            content()
        }
    }

    // Đảm bảo hiển thị đúng trạng thái khi `isRevealed` đổi từ bên ngoài
    LaunchedEffect(isRevealed, swipeWidthPx.floatValue) {
        if (isRevealed) {
            offset.animateTo(-swipeWidthPx.floatValue)
        } else {
            offset.animateTo(0f)
        }
    }
}
