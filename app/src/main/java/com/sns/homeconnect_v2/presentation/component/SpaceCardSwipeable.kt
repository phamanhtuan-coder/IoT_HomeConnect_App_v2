package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

@Composable
fun SpaceCardSwipeable(
    spaceName: String,
    deviceCount: Int,
    iconName: String,
    iconColor: Color,
    isRevealed: Boolean,
    role: String,
    onExpandOnly: () -> Unit,
    onCollapse: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onClick: () -> Unit
) {
    val canEdit = hasPermission(role, RoleLevel.VICE) // "owner", "vice"
    val canDelete = hasPermission(role, RoleLevel.OWNER) // chỉ "owner"
    val iconRes   = remember(iconName) { getIconResByName(iconName) }

    SwipeableItemWithActions(
        isRevealed = isRevealed,
        onExpanded = onExpandOnly,
        onCollapsed = onCollapse,
        actions = {
            Spacer(Modifier.width(8.dp))

            ActionIcon(
                onClick = onEdit,
                backgroundColor = if (canEdit) Color(0xFF4CAF50) else Color.LightGray,
                icon = ActionIconContent.VectorIcon(Icons.Default.Edit), // ✅ sửa chỗ này
                enabled = canEdit
            )

            Spacer(Modifier.width(8.dp))

            ActionIcon(
                onClick = onDelete,
                backgroundColor = if (canDelete) Color(0xFFF44336) else Color.LightGray,
                icon = ActionIconContent.VectorIcon(Icons.Default.Delete), // ✅ sửa chỗ này
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
            /* Hiển thị icon từ resource */
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
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
                            text = "$deviceCount",
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