package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BedroomBaby
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.presentation.component.widget.ActionIcon
import com.sns.homeconnect_v2.presentation.component.widget.SwipeableItemWithActions
import com.sns.homeconnect_v2.presentation.model.SpaceUi

/**
 * Một hàm có thể kết hợp (composable function) hiển thị một thẻ có thể vuốt cho một không gian.
 *
 * Thẻ này hiển thị tên của không gian, số lượng thiết bị trong đó và một biểu tượng đại diện cho không gian.
 * Nó hỗ trợ cử chỉ vuốt để hiển thị các hành động như chỉnh sửa và xóa.
 *
 * @param spaceName Tên của không gian sẽ được hiển thị.
 * @param deviceCount Số lượng thiết bị trong không gian.
 * @param icon [ImageVector] sẽ được sử dụng làm biểu tượng cho không gian. Mặc định là [Icons.Default.Group].
 * @param iconColor [Color] của biểu tượng. Mặc định là màu chính của [MaterialTheme.colorScheme] hiện tại.
 * @param isRevealed Một giá trị boolean cho biết các nút hành động (chỉnh sửa, xóa) có đang hiển thị hay không.
 * @param onExpandOnly Một hàm lambda sẽ được gọi khi thẻ được vuốt để hiển thị các hành động và chỉ thẻ này nên được mở rộng.
 * @param onCollapse Một hàm lambda sẽ được gọi khi thẻ được vuốt trở lại để ẩn các hành động.
 * @param onDelete Một hàm lambda sẽ được gọi khi hành động xóa được kích hoạt.
 * @param onEdit Một hàm lambda sẽ được gọi khi hành động chỉnh sửa được kích hoạt.
 * @param onClick Một hàm lambda sẽ được gọi khi nội dung chính của thẻ được nhấp vào.
 *
 * @author Nguyễn Thanh Sang
 * @since 26-05-2025
 */
@Composable
fun SpaceCardSwipeable(
    spaceName: String,
    deviceCount: Int,
    icon: ImageVector = Icons.Default.Group,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    isRevealed: Boolean,
    onExpandOnly: () -> Unit,
    onCollapse: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onClick: () -> Unit // ✅ Thêm onClick làm tham số
) {
    SwipeableItemWithActions(
        isRevealed = isRevealed,
        onExpanded = onExpandOnly,
        onCollapsed = onCollapse,
        actions = {
            Spacer(Modifier.width(8.dp))
            ActionIcon(
                onClick = onEdit,
                backgroundColor = Color(0xFF4CAF50),
                icon = Icons.Default.Edit
            )
            Spacer(Modifier.width(8.dp))
            ActionIcon(
                onClick = onDelete,
                backgroundColor = Color(0xFFF44336),
                icon = Icons.Default.Delete
            )
        }
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() } // ✅ Gán sự kiện click
                .background(color = Color(0xFFD8E4E8))
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = "Icon for $spaceName",
                tint = iconColor,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    spaceName,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFFFFC107),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Text(
                            text = deviceCount.toString(),
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "Thiết bị",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpaceCardSwipeablePreview() {
    val spaces = remember {
        mutableStateListOf(
            SpaceUi(1, "Bathroom", 5, false, Icons.Default.BedroomBaby, Color.Blue),
            SpaceUi(2, "Bedroom", 3, false, Icons.Default.Home, Color.Red),
            SpaceUi(3, "Lounge", 7, false, Icons.Default.Group, Color.Green)
        )
    }

    LazyColumn {
        itemsIndexed(spaces) { index, space ->
            Spacer(Modifier.height(8.dp))
            SpaceCardSwipeable(
                spaceName = space.name,
                deviceCount = space.device,
                icon = space.icon,
                iconColor = space.iconColor,
                isRevealed = space.isRevealed,
                onExpandOnly = {
                    spaces.indices.forEach { i ->
                        spaces[i] = spaces[i].copy(isRevealed = i == index)
                    }
                },
                onCollapse = {
                    spaces[index] = space.copy(isRevealed = false)
                },
                onDelete = { spaces.removeAt(index) },
                onEdit = { /* TODO: handle edit */ },
                onClick = { println("Clicked on ${space.name}") } // ✅ Xử lý click
            )
        }
    }
}