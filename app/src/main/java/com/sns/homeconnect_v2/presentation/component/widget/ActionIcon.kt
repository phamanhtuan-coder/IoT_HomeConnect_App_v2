package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
 * @param contentDescription Văn bản được các dịch vụ trợ năng sử dụng để mô tả biểu tượng này
 * đại diện cho điều gì. Điều này phải luôn được cung cấp trừ khi biểu tượng này được sử dụng cho mục đích trang trí
 * và không đại diện cho một hành động có ý nghĩa mà người dùng có thể thực hiện.
 * @param tint Màu sắc sẽ được áp dụng cho [icon]. Nếu [Color.Unspecified] được cung cấp, thì không có
 * màu sắc nào được áp dụng. Mặc định là [Color.White].
 */
@Composable
fun ActionIcon(
    onClick: () -> Unit,
    backgroundColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = Color.White
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(48.dp)
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}