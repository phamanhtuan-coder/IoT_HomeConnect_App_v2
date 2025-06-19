package com.sns.homeconnect_v2.presentation.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.core.util.validation.RoleLevel
import com.sns.homeconnect_v2.core.util.validation.getIconResByName
import com.sns.homeconnect_v2.core.util.validation.hasPermission
import com.sns.homeconnect_v2.presentation.component.widget.ActionIcon
import com.sns.homeconnect_v2.presentation.component.widget.SwipeableItemWithActions

/**
 * Thành phần giao diện (Composable) để hiển thị thẻ thông tin nhà có thể vuốt.
 *
 * @param houseName Tên của ngôi nhà.
 * @param spaceCount Số lượng không gian trong nhà.
 * @param icon Biểu tượng hiển thị cho ngôi nhà. Mặc định là Icons.Default.Home.
 * @param iconColor Màu của biểu tượng. Mặc định là MaterialTheme.colorScheme.primary.
 * @param isRevealed Trạng thái cho biết các hành động có được hiển thị hay không.
 * @param onExpandOnly Hàm gọi lại khi mục được mở rộng.
 * @param onCollapse Hàm gọi lại khi mục được thu gọn.
 * @param onDelete Hàm gọi lại khi hành động xóa được nhấp.
 * @param onEdit Hàm gọi lại khi hành động sửa được nhấp.
 *
 * @author Nguyễn Thanh Sang
 * @since 26-05-2025
 */
@Composable
fun HouseCardSwipeable(
    houseName: String,
    spaceCount: Int,
    iconName: String,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    isRevealed: Boolean,
    role: String,
    onExpandOnly: () -> Unit,
    onCollapse: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onClick: () -> Unit
){
    Log.d("HouseCardSwipeable", "role: $role, isRevealed: $isRevealed")
    val canEdit = hasPermission(role, RoleLevel.VICE)
    val canDelete = hasPermission(role, RoleLevel.OWNER)

    val iconRes   = remember(iconName) { getIconResByName(iconName) }

    SwipeableItemWithActions(
        isRevealed = isRevealed,
        onExpanded = onExpandOnly,
        onCollapsed = onCollapse,
        actions = {
            Spacer(Modifier.width(8.dp))
            ActionIcon(
                onClick = onEdit,
                backgroundColor = Color(0xFF4CAF50),
                icon = Icons.Default.Edit,
                enabled = canEdit
            )
            Spacer(Modifier.width(8.dp))
            ActionIcon(
                onClick = onDelete,
                backgroundColor = Color(0xFFF44336),
                icon = Icons.Default.Delete,
                enabled = canDelete
            )
        }
    ) {
        Row(
            modifier = Modifier
                .background(color = Color(0xFFD8E4E8))
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),          // 👈 dùng painterResource
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = houseName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "$spaceCount space",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Arrow",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HouseCardSwipeablePreview() {
//    val houses = remember {
//        mutableStateListOf(
//            HouseUi(1, "Main house", 3, false, Icons.Default.Home, Color.Black),
//            HouseUi(2, "Villa", 5, false, Icons.Default.Castle, Color.Red),
//            HouseUi(3, "Office", 2, false, Icons.Default.Home, Color.DarkGray)
//        )
//    }
//
//    LazyColumn {
//        itemsIndexed(houses) { index, house ->
//            Spacer(Modifier.height(8.dp))
//            HouseCardSwipeable(
//                houseName = house.name,
//                spaceCount = house.spaces,
//                icon = house.icon,
//                iconColor = house.iconColor,
//                isRevealed = house.isRevealed,
//                onExpandOnly = {
//                    houses.indices.forEach { i ->
//                        houses[i] = houses[i].copy(isRevealed = i == index)
//                    }
//                },
//                onCollapse = {
//                    houses[index] = house.copy(isRevealed = false)
//                },
//                onDelete = { houses.removeAt(index) },
//                onEdit = { /* TODO */ }
//            )
//        }
//    }
//}