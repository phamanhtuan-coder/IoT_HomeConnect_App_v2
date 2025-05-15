package com.sns.homeconnect_v2.presentation.component.widget

import IoTHomeConnectAppTheme
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.roundToInt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme

/**
 * Một Composable function tạo ra một Nút hành động nổi (FAB) dạng tỏa tròn.
 *
 * FAB này có thể mở rộng để hiển thị một tập hợp các FAB con được sắp xếp theo dạng tỏa tròn.
 *
 * @param items Một danh sách các đối tượng [FabChild] đại diện cho các FAB con. Có thể rỗng.
 * @param modifier Modifier cho composable này.
 * @param fabIcon Biểu tượng hiển thị trên FAB chính. Mặc định là [Icons.Default.Add].
 * @param parentColor Màu nền của FAB chính. Mặc định là [MaterialTheme.colorScheme.primary].
 * @param radius Bán kính của cung tròn mà các FAB con được đặt trên đó. Mặc định là `96.dp`.
 * @param startDeg Góc bắt đầu (tính bằng độ) để đặt FAB con đầu tiên.
 *                 -90 độ tương ứng với vị trí trên cùng. Mặc định là `-90f`.
 * @param sweepDeg Góc quét (tính bằng độ) để sắp xếp các FAB con.
 *                 Giá trị âm có nghĩa là ngược chiều kim đồng hồ. Mặc định là `-90f`.
 * @param onParentClick Một hàm lambda sẽ được thực thi khi FAB chính được nhấp
 *                      và không có mục con nào (`items` rỗng).
 *                      Điều này dành cho các hành động ngắn khi FAB không mở rộng.
 */
@Composable
fun RadialFab(
    items: List<FabChild>,                 // nút con (có thể rỗng)
    modifier: Modifier = Modifier,
    fabIcon: ImageVector = Icons.Default.Add,
    parentColor: Color = MaterialTheme.colorScheme.primary,
    radius: Dp = 96.dp,                    // bán kính quạt
    startDeg: Float = -90f,                // hướng đầu (-90° = lên trên)
    sweepDeg: Float = -90f,                // góc quét (âm = ngược chiều kim đồng hồ)
    onParentClick: () -> Unit = {},        // action ngắn (khi KHÔNG bung)
) {
    var expanded by remember { mutableStateOf(false) }
    val transition = updateTransition(expanded, label = "radial")

    val rot by animateFloatAsState(if (expanded) 45f else 0f)

    /* ----- Container ----- */
    Box(modifier, contentAlignment = Alignment.BottomEnd) {

        // ====== FAB con (radial) ======
        items.forEachIndexed { idx, child ->
            val angle = startDeg + sweepDeg / (items.lastIndex.coerceAtLeast(1)) * idx
            val radians = Math.toRadians(angle.toDouble())
            val px = with(LocalDensity.current) { radius.toPx() }
            val offsetX = cos(radians) * px
            val offsetY = sin(radians) * px      // sin(-90°) = -1  -> lên trên

            val animatedX by transition.animateFloat(
                label = "x$idx",
                transitionSpec = { tween(durationMillis = 250) }
            ) { if (it) offsetX.toFloat() else 0f }

            val animatedY by transition.animateFloat(
                label = "y$idx",
                transitionSpec = { tween(250) }
            ) { if (it) offsetY.toFloat() else 0f }

            SmallFloatingActionButton(
                onClick = {
                    expanded = false
                    child.onClick()
                },
                containerColor = child.containerColor,
                contentColor = child.contentColor,
                modifier = Modifier
                    .offset { IntOffset(animatedX.roundToInt(), animatedY.roundToInt()) }
                    .size(48.dp)
            ) { Icon(child.icon, null) }
        }

        FloatingActionButton(
            onClick = {
                if (items.isEmpty()) onParentClick()
                else expanded = !expanded
            },
            containerColor = parentColor,
            contentColor = Color.White,
            modifier = Modifier.rotate(rot)
        ) { Icon(fabIcon, null) }
    }
}



@Preview(showBackground = true, widthDp = 360, heightDp = 640, name = "RadialFab Preview")
@Composable
fun RadialFabPreview() {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            RadialFab(
                items = listOf(
                    FabChild(
                        icon = Icons.Default.Edit,
                        onClick = {},
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    FabChild(
                        icon = Icons.Default.Delete,
                        onClick = {},
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    FabChild(
                        icon = Icons.Default.Share,
                        onClick = {},
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    )
                ),
                radius = 100.dp,
                startDeg = -90f,
                sweepDeg = -90f,
                parentColor = colorScheme.primary
            )
        }
    }
}