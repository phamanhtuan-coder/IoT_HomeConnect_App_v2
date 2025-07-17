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
 * Thành phần dropdown chung có thể tái sử dụng, được tạo kiểu để khớp với chiều cao SearchBar.
 *
 * @param items        Danh sách chuỗi hiển thị.
 * @param selectedItem Chuỗi đang được chọn (nullable).
 * @param onItemSelected Callback khi chọn mục.
 * @param modifier     Modifier tùy chỉnh.
 * @param placeHolder  Văn bản placeholder khi chưa chọn.
 * @param isTablet     Dự phòng cho UI tablet (chưa dùng).
 * @param leadingIcon  Icon đầu dòng (nullable).
 * @param enabled      Cho phép nhấn hay không.
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
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true
) {
    var showSheet by remember { mutableStateOf(false) }

    /** ----- VÙNG KÍCH HOẠT ----- */
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (enabled) Color(0xFF9E9E9E) else Color(0xFFBDBDBD),
                RoundedCornerShape(16.dp)
            )
            .background(if (enabled) Color.White else Color(0xFFF5F5F5))
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .let { if (enabled) it.clickable { showSheet = true } else it },
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
                    modifier = Modifier.size(28.dp),
                )
                Spacer(Modifier.width(12.dp))
            }

            Text(
                text = selectedItem.takeUnless { it.isNullOrBlank() } ?: placeHolder,
                fontSize = 16.sp,
                color = when {
                    !enabled -> Color(0xFF9E9E9E)
                    selectedItem.isNullOrBlank() -> Color.Gray
                    else -> Color(0xFF212121)
                },
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = if (showSheet) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = if (enabled) Color(0xFF212121) else Color(0xFF9E9E9E),
                modifier = Modifier.size(28.dp)
            )
        }
    }

    /** ----- SHEET ----- */
    if (showSheet && enabled) {
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
                            .background(
                                if (item == selectedItem)
                                    Color(0xFF2979FF).copy(alpha = 0.08f)
                                else Color.Transparent
                            )
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
                        HorizontalDivider(Modifier.padding(horizontal = 8.dp))
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
        Column(Modifier.padding(24.dp)) {
            GenericDropdown(
                items = listOf("Phòng khách", "Phòng ngủ", "Nhà bếp"),
                selectedItem = current,
                onItemSelected = { current = it },
                leadingIcon = null,
                placeHolder = "Chọn phòng"
            )
        }
    }
}
