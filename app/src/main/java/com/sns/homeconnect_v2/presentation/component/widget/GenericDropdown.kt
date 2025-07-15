package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Thành phần dropdown chung có thể tái sử dụng, được tạo kiểu để khớp với kích thước của SearchBar.
 *
 * Thành phần này cung cấp một menu dropdown với giao diện nhất quán.
 *
 * Các tính năng tạo kiểu chính:
 * - **Chiều cao**: `56.dp` (khớp với chiều cao của SearchBar).
 * - **Padding**: `16.dp` theo chiều ngang & `12.dp` theo chiều dọc.
 * - **Bán kính góc**: `12.dp` (để có giao diện bo tròn đồng nhất).
 * - **Mũi tên ở cuối**: Một biểu tượng mũi tên (xuống/lên) ở cuối dropdown, sẽ thay đổi hướng khi menu được mở hoặc đóng.
 *
 * @param items Danh sách các chuỗi để hiển thị trong menu dropdown.
 * @param selectedItem Mục hiện đang được chọn. Có thể là null nếu không có mục nào được chọn.
 * @param onItemSelected Một hàm callback được gọi khi một mục được chọn từ dropdown.
 * @param modifier [Modifier] tùy chọn để áp dụng cho dropdown.
 * @param placeHolder Văn bản hiển thị khi không có mục nào được chọn. Mặc định là "Chọn...".
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericDropdown(
    items: List<String>,
    selectedItem: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeHolder: String = "Select...",
    isTablet: Boolean = false,
    leadingIcon: ImageVector? = null
) {
    var showSheet by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .background(Color(0xFFF7F2FA))
            .height(48.dp)
            .fillMaxWidth()
            .clickable { showSheet = true }
            .padding(horizontal = 10.dp), // Giảm padding ngang
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = Color(0xFF374151), // Xám đậm nhẹ
                    modifier = Modifier.size(20.dp) // Giảm kích thước icon
                )
                Spacer(modifier = Modifier.width(6.dp)) // Giảm khoảng cách
            }

            Text(
                text = selectedItem.takeUnless { it.isNullOrBlank() } ?: placeHolder,
                fontSize = 14.sp, // Giảm kích thước chữ
                color = if (selectedItem.isNullOrBlank()) Color(0xFF9CA3AF) else Color(0xFF374151), // Xám nhạt/xám đậm
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp), // Thêm padding phải để tránh cắt chữ
                maxLines = 1,
                overflow = TextOverflow.Ellipsis // Xử lý chữ dài
            )

            Icon(
                imageVector = if (showSheet) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color(0xFF374151),
                modifier = Modifier.size(20.dp)
            )
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            containerColor = Color(0xFFF7F2FA),
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp) // Giảm bo góc
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp, start = 12.dp, end = 12.dp) // Giảm padding
            ) {
                Text(
                    text = placeHolder,
                    style = MaterialTheme.typography.titleSmall, // Giảm kích thước tiêu đề
                    color = Color(0xFF1E88E5),
                    modifier = Modifier.padding(bottom = 10.dp) // Giảm padding
                )

                items.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onItemSelected(item)
                                showSheet = false
                            }
                            .background(if (item == selectedItem) Color(0x1A1E88E5) else Color.Transparent) // Nền xanh nhạt khi chọn
                            .padding(vertical = 10.dp, horizontal = 10.dp) // Giảm padding
                    ) {
                        Text(
                            text = item,
                            fontSize = 16.sp, // Giảm kích thước chữ
                            color = if (item == selectedItem) Color(0xFF1E88E5) else Color(0xFF374151),
                            fontWeight = if (item == selectedItem) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                    if (item != items.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 10.dp), // Giảm padding
                            color = Color(0xFFE0E0E0)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenericDropdownPreview() {
    var current by remember { mutableStateOf<String?>(null) }
    MaterialTheme {
        Column(modifier = Modifier.padding(24.dp)) {
            GenericDropdown(
                items = listOf("Phòng khách", "Phòng ngủ", "Nhà bếp"),
                selectedItem = current,
                onItemSelected = { current = it },
                isTablet = false,
                leadingIcon = null,
                placeHolder = "Chọn phòng"
            )
        }
    }
}
