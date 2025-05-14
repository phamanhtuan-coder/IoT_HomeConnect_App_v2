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

/* Dữ liệu nút con tái sử dụng từ lần trước */
data class FabChild(
    val icon: ImageVector,
    val onClick: () -> Unit,
    val containerColor: Color = Color(0xFF455A64),
    val contentColor: Color   = Color.White
)

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
