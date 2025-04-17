package com.sns.homeconnect_v2.presentation.component.navigation

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.common.util.DeviceProperties.isTablet

/** Giao diện nút Home (FAB Home)
 * -----------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 29/11/2024
 * Lần cập nhật cuối: 29/11/2024
 * -----------------------------------------
 * Input: Không có
 * Output: FloatingActionButton
 *
 *
 * ---------------------------------------
 */
@Preview(showBackground = true)
@Composable
fun NutHome() {
    val context = LocalContext.current
    val isTablet = isTablet(context)

    val fabSize = if (isTablet) 72.dp else 64.dp
    val fabElevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = if (isTablet) 8.dp else 6.dp,
        pressedElevation = if (isTablet) 12.dp else 8.dp
    )

    FloatingActionButton(
        modifier = Modifier
            .offset(y = 60.dp)
            .size(fabSize),
        elevation = fabElevation,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        onClick = {
            /*TODO*/
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = "Home",
            modifier = Modifier.size(if (isTablet) 42.dp else 36.dp)
        )
    }
}