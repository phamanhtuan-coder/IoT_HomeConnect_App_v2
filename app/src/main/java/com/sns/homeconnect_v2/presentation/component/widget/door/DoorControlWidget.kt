package com.sns.homeconnect_v2.presentation.component.widget.door

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Loại cửa: truyền thống, trượt ngang, cuốn lên.
 */
enum class DoorType(val label: String) {
    TRADITIONAL("Cửa cánh"),
    SLIDING("Cửa trượt"),
    ROLLER("Cửa cuốn");

    companion object {
        fun fromLabel(label: String): DoorType? {
            return values().find { it.label == label }
        }
    }
}

// Thời gian animation chậm hơn (ms)
private const val ANIM_DURATION = 900

/* ---------------------- DOOR CANVAS ---------------------- */
@Composable
fun DoorCanvas(
    doorType: DoorType,
    isOpen: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isOpen) Color(0xFF4CAF50) else Color(0xFF1565C0)
    val lockIcon = if (isOpen) Icons.Default.LockOpen else Icons.Default.Lock
    val doorBg = Color(0xFFB0C6E0)

    Surface(
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 8.dp,
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onToggle() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            /*** DRAW SPECIFIC DOOR ***/
            when (doorType) {
                DoorType.TRADITIONAL   -> TraditionalDoor(isOpen, doorBg, borderColor)
                DoorType.SLIDING       -> SlidingDoor(isOpen, doorBg, borderColor)
                DoorType.ROLLER        -> RollerDoor(isOpen, doorBg, borderColor)
            }

            /*** LOCK OVERLAY ***/
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(lockIcon, contentDescription = null, tint = borderColor)
                }
            }
        }
    }
}

/* ---------------------- DOOR IMPLEMENTATIONS ---------------------- */
@Composable
private fun TraditionalDoor(isOpen: Boolean, bg: Color, borderColor: Color) {
    // animate width fraction (1f closed -> 0.25f open)
    val fraction by animateFloatAsState(
        targetValue = if (isOpen) 0.25f else 1f,
        animationSpec = tween(ANIM_DURATION, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction)
                .background(bg)
        )
        // knob
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
                .size(width = 6.dp, height = 40.dp)
                .background(Color.Gray, RoundedCornerShape(3.dp))
        )
    }
}

@Composable
private fun SlidingDoor(isOpen: Boolean, bg: Color, borderColor: Color) {
    val offset by animateFloatAsState(
        targetValue = if (isOpen) 0.6f else 0f,
        animationSpec = tween(ANIM_DURATION, easing = FastOutSlowInEasing)
    )
    val panelWidth = 0.5f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
    ) {
        // left panel
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(panelWidth)
                .offset(x = (-offset * 100).dp) // smoother conversion
                .background(bg)
        )
        // right panel
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .fillMaxWidth(panelWidth)
                .offset(x = (offset * 100).dp)
                .background(bg)
        )
        // central handle
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 4.dp, height = 60.dp)
                .background(borderColor)
        )
    }
}

@Composable
private fun RollerDoor(isOpen: Boolean, bg: Color, borderColor: Color) {
    val fraction by animateFloatAsState(
        targetValue = if (isOpen) 0f else 1f,
        animationSpec = tween(ANIM_DURATION, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
    ) {
        // top hood
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(Color.LightGray)
        )

        // door sheet
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction)
                .align(Alignment.TopCenter)
        ) {
            Column(Modifier.fillMaxSize()) {
                repeat(10) {
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(bg)
                            .border(0.5.dp, Color.White)
                    )
                }
            }
        }
    }
}

/* ---------------------- PREVIEW ---------------------- */
@Preview(showBackground = true)
@Composable
fun DoorControlWidgetPreview() {
    var type by remember { mutableStateOf(DoorType.TRADITIONAL) }
    var open by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        DoorCanvas(doorType = type, isOpen = open, onToggle = { open = !open })
    }
}