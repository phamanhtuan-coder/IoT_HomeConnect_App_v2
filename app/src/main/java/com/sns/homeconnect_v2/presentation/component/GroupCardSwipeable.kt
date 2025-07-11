package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
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
import com.sns.homeconnect_v2.presentation.component.widget.ActionIconContent
import com.sns.homeconnect_v2.presentation.component.widget.SwipeableItemWithActions

/**
 * Một hàm Composable hiển thị thẻ nhóm với các hành động có thể vuốt.
 *
 * Thẻ này hiển thị tên nhóm, số lượng thành viên và một biểu tượng. Nó có thể được vuốt
 * để hiển thị các hành động như chỉnh sửa và xóa.
 *
 * @param groupName Tên của nhóm.
 * @param memberCount Số lượng thành viên trong nhóm.
 * @param icon Biểu tượng hiển thị cho nhóm. Mặc định là `Icons.Default.Group`.
 * @param iconColor Màu của biểu tượng nhóm. Mặc định là `MaterialTheme.colorScheme.primary`.
 * @param isRevealed Một giá trị boolean cho biết liệu các hành động vuốt hiện có đang được hiển thị hay không.
 * @param onExpandOnly Một hàm lambda sẽ được gọi khi mục được vuốt để hiển thị các hành động.
 *                     Thường được sử dụng để đảm bảo chỉ có một mục được mở rộng tại một thời điểm.
 * @param onCollapse Một hàm lambda sẽ được gọi khi các hành động đã hiển thị được vuốt lại để ẩn đi.
 * @param onDelete Một hàm lambda sẽ được gọi khi hành động xóa được nhấp.
 * @param onEdit Một hàm lambda sẽ được gọi khi hành động chỉnh sửa được nhấp.
 *
 * @author Nguyễn Thanh Sang
 * @since 26-05-2025
 */
@Composable
fun GroupCardSwipeable(
    groupName: String,
    memberCount: Int,
    iconName: String? = null,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    isRevealed: Boolean,
    role: String,
    onExpandOnly: () -> Unit,
    onCollapse: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onClick: () -> Unit
) {
    // Quyền chỉnh sửa: Cho phép cả "owner" và "vice"
    val canEdit = hasPermission(role, RoleLevel.VICE)
    // Quyền xóa: Chỉ cho phép "owner"
    val canDelete = hasPermission(role, RoleLevel.OWNER)

    /* Map tên icon → drawableId một lần theo tên */
    val iconRes = remember(iconName) { getIconResByName(iconName) }

    SwipeableItemWithActions(
        isRevealed = isRevealed,
        onExpanded = onExpandOnly,
        onCollapsed = onCollapse,
        actions = {
            Spacer(Modifier.width(8.dp))

            ActionIcon(
                onClick = onEdit,
                backgroundColor = if (canEdit) Color(0xFF4CAF50) else Color.LightGray,
                icon = ActionIconContent.VectorIcon(Icons.Default.Edit),
                enabled = canEdit
            )
            Spacer(Modifier.width(8.dp))
            ActionIcon(
                onClick = onDelete,
                backgroundColor = if (canDelete) Color(0xFFF44336) else Color.LightGray,
                icon = ActionIconContent.VectorIcon(Icons.Default.Delete),
                enabled = canDelete,
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
            /* Vẽ icon từ painterResource */
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    groupName,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFFFFC107),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Text(
                            text = "$memberCount",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "Thành viên",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                        color = Color.Black
                    )
                }
            }
        }
    }
}