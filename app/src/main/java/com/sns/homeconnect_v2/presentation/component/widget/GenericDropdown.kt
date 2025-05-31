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
    leadingIcon: ImageVector? = null // Icon đầu dòng (nếu có)
) {
    var showSheet by remember { mutableStateOf(false) }

    // Vùng nhấn để mở sheet
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFF9E9E9E), RoundedCornerShape(16.dp))
            .background(Color.White)
            .height(60.dp)
            .fillMaxWidth()
            .clickable { showSheet = true }
            .padding(horizontal = 16.dp),
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
                    tint = Color(0xFF212121),
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = selectedItem.takeUnless { it.isNullOrBlank() } ?: placeHolder,
                fontSize = 20.sp,
                color = if (selectedItem.isNullOrBlank()) Color.Gray else Color(0xFF212121),
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = if (showSheet) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color(0xFF212121),
                modifier = Modifier.size(28.dp)
            )
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                // Title hiển thị placeholder
                Text(
                    text = placeHolder,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF2979FF),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                items.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onItemSelected(item)
                                showSheet = false
                            }
                            .background(if (item == selectedItem) Color(0xFF2979FF).copy(alpha = 0.08f) else Color.Transparent)
                            .padding(vertical = 18.dp, horizontal = 12.dp)
                    ) {
                        Text(
                            text = item,
                            fontSize = 20.sp,
                            color = if (item == selectedItem) Color(0xFF2979FF) else Color(0xFF212121),
                            fontWeight = if (item == selectedItem) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                    if (item != items.last()) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
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
