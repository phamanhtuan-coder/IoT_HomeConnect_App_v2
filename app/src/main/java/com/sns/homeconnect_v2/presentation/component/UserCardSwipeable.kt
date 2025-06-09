package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sns.homeconnect_v2.core.util.validation.RoleLevel
import com.sns.homeconnect_v2.core.util.validation.hasPermission
import com.sns.homeconnect_v2.presentation.component.widget.ActionIcon
import com.sns.homeconnect_v2.presentation.component.widget.SwipeableItemWithActions

/**
 * Một hàm composable hiển thị thẻ người dùng với các hành động có thể vuốt.
 *
 * Thành phần này hiển thị thông tin người dùng bao gồm tên, vai trò và ảnh đại diện của họ.
 * Nó hỗ trợ các cử chỉ vuốt để hiển thị các hành động như chỉnh sửa và xóa.
 *
 * @param userName Tên của người dùng.
 * @param role Vai trò của người dùng (ví dụ: Chủ sở hữu, Quản trị viên, Thành viên).
 * @param avatarUrl URL của hình ảnh đại diện của người dùng. Mặc định là một chuỗi trống, sẽ hiển thị một ảnh đại diện mặc định.
 * @param isRevealed Một trạng thái boolean cho biết các hành động có thể vuốt hiện có được hiển thị hay không.
 * @param onExpandOnly Một hàm lambda sẽ được gọi khi các hành động có thể vuốt được mở rộng.
 *                     Điều này thường được sử dụng để đảm bảo chỉ một mục được mở rộng tại một thời điểm.
 * @param onCollapse Một hàm lambda sẽ được gọi khi các hành động có thể vuốt được thu gọn.
 * @param onDelete Một hàm lambda sẽ được gọi khi hành động xóa được kích hoạt.
 * @param onEdit Một hàm lambda sẽ được gọi khi hành động chỉnh sửa được kích hoạt.
 *
 * @author Nguyễn Thanh Sang
 * @since 20-05-2025
 */
@Composable
fun UserCardSwipeable(
    userName: String,
    role: String,
    avatarUrl: String = "",
    isRevealed: Boolean,
    onExpandOnly: () -> Unit,
    onCollapse: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val canEdit = hasPermission(role, RoleLevel.VICE)
    val canDelete = hasPermission(role, RoleLevel.VICE)

    SwipeableItemWithActions(
        isRevealed = isRevealed,
        onExpanded = onExpandOnly,
        onCollapsed = onCollapse,
        actions = {
            Spacer(Modifier.width(8.dp))
            ActionIcon(
                onClick = onEdit,
                backgroundColor = if (canEdit) Color(0xFF4CAF50) else Color.Gray,
                icon = Icons.Default.Edit,
                enabled = canEdit
            )
            Spacer(Modifier.width(8.dp))
            ActionIcon(
                onClick = onDelete,
                backgroundColor = if (canDelete) Color(0xFFF44336) else Color.Gray,
                icon = Icons.Default.Delete,
                enabled = canDelete
            )

        }
    ) {
        Row(
            modifier = Modifier
                .background(color = Color(0xFFD8E4E8))
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(avatarUrl)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    )
                )
                Text(
                    text = role,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    color = Color.Black
                )
            }
        }
    }
}
