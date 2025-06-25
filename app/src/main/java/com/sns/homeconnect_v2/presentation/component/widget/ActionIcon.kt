package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Một hàm composable hiển thị một nút biểu tượng hành động.
 *
 * @param onClick Hàm callback sẽ được gọi khi nút biểu tượng này được nhấp.
 * @param backgroundColor Màu nền của nút biểu tượng.
 * @param icon [ImageVector] sẽ được hiển thị bên trong nút biểu tượng.
 * @param modifier [Modifier] sẽ được áp dụng cho nút biểu tượng này.
 * @param contentDescription Văn bản mô tả cho dịch vụ trợ năng.
 * @param enabled Boolean cho biết nút có được kích hoạt hay không. Mặc định là true.
 * @param iconTint Màu sắc sẽ được áp dụng cho [icon]. Nếu không xác định, sử dụng [Color.White].
 *
 * @author Nguyễn Thanh Sang
 * @since 26-05-2025
 */
@Composable
fun ActionIcon(
    onClick: () -> Unit,
    backgroundColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true,
    iconTint: Color = Color.White
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .size(48.dp)
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint.copy(alpha = if (enabled) 1f else 0.5f)
        )
    }
}