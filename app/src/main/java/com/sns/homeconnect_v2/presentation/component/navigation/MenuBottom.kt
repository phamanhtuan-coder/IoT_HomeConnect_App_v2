package com.sns.homeconnect_v2.presentation.component.navigation

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet

/**
 * Giao diện Thanh Menu Bottom (BottomAppBar)
 * -----------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 29/11/2024
 * Lần cập nhật cuối: 23/05/2025
 * -----------------------------------------
 * @return BottomAppBar chứa các MenuItem với Home button nổi
 * ---------------------------------------
 */
@Preview
@Composable
fun MenuBottom(
    navController: NavHostController = rememberNavController(),
    backgroundColor: Color = MaterialTheme.colorScheme.background // Background color of the app's scaffold
) {
    IoTHomeConnectAppTheme {
        val items = listOf(
            "Dashboard" to Pair(Icons.Filled.PieChart, "dashboard"),
            "Devices" to Pair(Icons.Filled.Devices, "devices"),
            "Home" to Pair(Icons.Filled.Home, "home"),
            "Profile" to Pair(Icons.Filled.Person, "profile"),
            "Settings" to Pair(Icons.Filled.Settings, "settings")
        )
        val context = LocalContext.current
        val isTablet = isTablet(context)

        // Track the last selected route
        val currentRoute = navController.currentBackStackEntry?.destination?.route

        // Main container with proper stacking context
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.height(if (isTablet) 120.dp else 90.dp)
        ) {
            // Bottom Navigation Bar with lower z-index
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .zIndex(0f)
            ) {
                BottomAppBar(
                    tonalElevation = 4.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = if (isTablet) 16.dp else 8.dp),
                    modifier = Modifier
                        .height(if (isTablet) 100.dp else 80.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // First half of menu items
                        items.take(2).forEach { item ->
                            val isSelected = currentRoute == item.second.second
                            MenuItem(
                                text = item.first,
                                icon = item.second.first,
                                isSelected = isSelected,
                                onClick = {
                                    navController.navigate(item.second.second) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                isTablet = isTablet,
                            )
                        }

                        // Empty space for the floating button
                        Spacer(modifier = Modifier.weight(1f))

                        // Second half of menu items
                        items.takeLast(2).forEach { item ->
                            val isSelected = currentRoute == item.second.second
                            MenuItem(
                                text = item.first,
                                icon = item.second.first,
                                isSelected = isSelected,
                                onClick = {
                                    navController.navigate(item.second.second) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                isTablet = isTablet,
                            )
                        }
                    }
                }
            }

            // Floating Home Button with background overlay for enhanced floating effect
            val homeItem = items[2] // Home item
            val isHomeSelected = currentRoute == homeItem.second.second
            val scale by animateFloatAsState(
                targetValue = if (isHomeSelected) 1.15f else 1f,
                animationSpec = tween(300, easing = FastOutSlowInEasing),
                label = "home_button_scale"
            )

            // Position the floating components at the top-center of the box with higher z-index
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
                    .offset(y = 0.dp) // Position it exactly where needed
            ) {
                // Overlay circle with app background color to create floating effect
                Box(
                    modifier = Modifier
                        .size(if (isTablet) 80.dp else 70.dp)
                        .background(backgroundColor, CircleShape)
                        .zIndex(0.9f)
                )

                FloatingActionButton(
                    onClick = {
                        navController.navigate(homeItem.second.second) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                                inclusive = false
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 12.dp,
                        pressedElevation = 8.dp
                    ),
                    modifier = Modifier
                        .size(if (isTablet) 70.dp else 60.dp)
                        .shadow(16.dp, CircleShape) // Increased shadow for better floating effect
                        .scale(scale)
                        .zIndex(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home",
                        modifier = Modifier.size(if (isTablet) 36.dp else 30.dp)
                    )
                }
            }
        }
    }
}

/**
 * Giao diện MenuItem
 * -----------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 29/11/2024
 * Lần cập nhật cuối: 23/05/2025
 * -----------------------------------------
 *
 * @param text: Tên menu
 * @param icon: Icon của menu
 * @param isSelected: Trạng thái chọn
 * @param onClick: Hàm xử lý khi click vào menu
 * @param isTablet: Kiểm tra thiết bị có phải là tablet không
 * @param textSize: Kích thước chữ
 * @param iconSize: Kích thước icon
 *
 * @return MenuItem chứa thông tin menu tùy theo thiết bị
 *
 * ---------------------------------------
 */
@Composable
fun MenuItem(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    isTablet: Boolean,
    textSize: TextUnit = 14.sp,
    iconSize: Dp = 60.dp
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Animation for the icon scale
    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "icon_scale"
    )

    // Animation for elevation
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 6.dp else 0.dp,
        animationSpec = tween(300),
        label = "elevation"
    )

    if (isTablet) {
        // Tablet layout - Cleaner design with text and icon
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .shadow(elevation = elevation, shape = RoundedCornerShape(16.dp))
                .clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = LocalIndication.current
                ),
            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier
                        .size(28.dp)
                        .graphicsLayer(scaleX = iconScale, scaleY = iconScale)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                    fontSize = textSize,
                    maxLines = 1
                )
            }
        }
    } else {
        // Mobile layout - Clean icon-only design with animation
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = LocalIndication.current
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .shadow(elevation = elevation, shape = CircleShape),
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                shape = CircleShape
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier
                            .size(24.dp)
                            .graphicsLayer(scaleX = iconScale, scaleY = iconScale)
                    )
                }
            }

            // Indicator dot for selected item instead of text
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn(animationSpec = tween(200)) + scaleIn(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200)) + scaleOut(animationSpec = tween(200))
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(4.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
            }
        }
    }
}