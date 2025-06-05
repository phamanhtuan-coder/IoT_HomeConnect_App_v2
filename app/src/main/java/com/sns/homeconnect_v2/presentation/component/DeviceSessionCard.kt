package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.sns.homeconnect_v2.R
import com.sns.homeconnect_v2.data.remote.dto.response.UserActivityResponse
import java.time.OffsetDateTime

fun getDeviceIconRes(deviceName: String): Int {
    val browserKeywords = setOf("browser", "chrome", "firefox", "safari", "edge", "opera")
    val deviceNameLower = deviceName.lowercase()

    return when {
        browserKeywords.any { it in deviceNameLower } -> R.drawable.windows
        else -> R.drawable.android
    }
}

fun isLoggedIn(lastOut: String?): Boolean {
    // Nếu lastOut là null, thiết bị vẫn đang đăng nhập
    return lastOut.isNullOrBlank()
}

fun timeSinceLogout(lastOut: String?): String {
    if (lastOut.isNullOrBlank()) return ""
    val outTime = OffsetDateTime.parse(lastOut)
    val now = OffsetDateTime.now()
    val duration = java.time.Duration.between(outTime, now)
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60
    return when {
        hours > 0 -> "$hours giờ trước"
        minutes > 0 -> "$minutes phút trước"
        else -> "vừa xong"
    }
}

@Composable
fun DeviceSessionCard(
    modifier: Modifier = Modifier,
    userActivityResponse: UserActivityResponse,
    onLogout: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    var isHovered by remember { mutableStateOf(false) }
    var isLogoutPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val hoverInteraction = remember { MutableInteractionSource() }
    val red = Color(0xFFFF6666)
    val redBg = Color(0xFFFFF0F0)
    val isPressed by interactionSource.collectIsPressedAsState()

    val isDeviceLoggedIn = isLoggedIn(userActivityResponse.last_out)
    val lastActive = timeSinceLogout(userActivityResponse.last_out)

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            isLogoutPressed = interaction is androidx.compose.foundation.interaction.PressInteraction.Press
        }
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isHovered) Color(0xFFB8C4C8) else Color(0xFFD8E4E8),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFFF2F5F8),
                shape = RoundedCornerShape(16.dp)
            )
            .hoverable(
                interactionSource = hoverInteraction,
                enabled = true
            )
            .clickable(onClick = { onClick?.invoke() })
    ) {
        LaunchedEffect(hoverInteraction) {
            hoverInteraction.interactions.collect { interaction ->
                when (interaction) {
                    is androidx.compose.foundation.interaction.HoverInteraction.Enter -> isHovered = true
                    is androidx.compose.foundation.interaction.HoverInteraction.Exit -> isHovered = false
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFD8E4E8), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(getDeviceIconRes(userActivityResponse.device_name)),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = userActivityResponse.device_name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF222222),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))

                    if (isDeviceLoggedIn) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(0xFF21D375), shape = CircleShape)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Đang đăng nhập",
                                fontSize = 14.sp,
                                color = Color(0xFF21D375),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(IntrinsicSize.Max)
                        ) {
                            Text(
                                text = "Đăng xuất $lastActive",
                                fontSize = 14.sp,
                                color = Color(0xFFB0B0B0),
                                fontWeight = FontWeight.Medium,
                                softWrap = false,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            ElevatedButton(
                onClick = { onLogout?.invoke() },
                modifier = Modifier
                    .height(42.dp)
                    .padding(start = 16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (isPressed) redBg else Color.White,
                    contentColor = red
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 1.dp,
                    pressedElevation = 0.dp,
                    hoveredElevation = 2.dp
                ),
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = red
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Đăng xuất",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = red
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 370)
@Composable
fun DeviceCardPreview() {
    val userActivity = UserActivityResponse(
        user_device_id = 1,
        user_id = "CUST4E040TZCD017N1YK6B2OMNYX",
        device_name = "iPhone 13 Pro",
        device_id = "device123",
        device_uuid = "UDVCE4I2EUDTUCQXXILDZMR62GJO",
        device_token = null,
        last_login = "2025-06-05T02:46:39.000Z",
        last_out = "2025-06-05T03:46:39.000Z",
        created_at = "2025-06-04T03:49:35.000Z",
        updated_at = "2025-06-05T02:46:39.000Z",
        is_deleted = false
    )
    DeviceSessionCard(
        userActivityResponse = userActivity,
        onLogout = { /* Handle logout */ },
        onClick = { /* Handle click */ }
    )
}
