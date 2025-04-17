package com.sns.homeconnect_v2.presentation.component.navigation

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet

/**
 * Giao diện Thanh Menu Bottom (BottomAppBar)
 * -----------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 29/11/2024
 * Lần cập nhật cuối: 11/12/2024
 * -----------------------------------------
 * @return BottomAppBar chứa các MenuItem
 * ---------------------------------------
 */
@Preview
@Composable
fun MenuBottom(
    navController: NavHostController = rememberNavController()
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

        BottomAppBar(
            tonalElevation = 4.dp,
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.height(120.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Top
            ) {
                items.forEach { item ->
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
}



/**
 * Giao diện MenuItem
 * -----------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 29/11/2024
 * Lần cập nhật cuối: 11/12/2024
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

    if (isTablet) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                    shape = RoundedCornerShape(18.dp)
                )
                .clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = LocalIndication.current
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                modifier = Modifier
                    .size(iconSize)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                modifier = Modifier.padding(end= 6.dp),
                text = text,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                fontSize = textSize,
                maxLines = 1
            )
        }
    } else {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                    shape = RoundedCornerShape(18.dp)
                )
                .clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = LocalIndication.current
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val mobileIconSize = if (isSelected) 28.dp else 40.dp
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                modifier = Modifier
                    .size(mobileIconSize)
                    .padding(4.dp)
            )
            if (isSelected) {
                Log.d("MenuItem", "isSelected: $isSelected")
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    maxLines = 1
                )
            }
        }
    }
}