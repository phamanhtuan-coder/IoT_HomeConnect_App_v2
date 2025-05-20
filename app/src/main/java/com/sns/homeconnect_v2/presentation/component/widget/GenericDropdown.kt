package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Room
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
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

@Composable
fun GenericDropdown(
    items: List<String>,
    selectedItem: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeHolder: String = "Select...",
    isTablet: Boolean = false,
    leadingIcon: ImageVector // ✅ THÊM icon
) {
    var expanded by remember { mutableStateOf(false) }
    val bgColor = Color.White
    val dropdownWidth = remember { mutableIntStateOf(0) }
    val localDensity = LocalDensity.current

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    dropdownWidth.intValue = coordinates.size.width
                }
                .width(if (isTablet) 500.dp else 400.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFF9E9E9E), RoundedCornerShape(16.dp))
                .background(bgColor)
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = Color(0xFF212121),
                    modifier = Modifier.size(48.dp) // ✅ CHO ICON TO LÊN
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = selectedItem.takeUnless { it.isNullOrBlank() } ?: placeHolder,
                    fontSize = 26.sp,
                    color = if (selectedItem.isNullOrBlank()) Color.Gray else Color(0xFF212121),
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color(0xFF212121),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(localDensity) { dropdownWidth.intValue.toDp() })
                .background(Color.White)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            fontSize = 26.sp,
                            color = Color(0xFF212121)
                        )
                    },
                    onClick = {
                        expanded = false
                        onItemSelected(item)
                    }
                )
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
                leadingIcon = Icons.Default.Room // 👈 truyền icon vào
            )
        }
    }
}