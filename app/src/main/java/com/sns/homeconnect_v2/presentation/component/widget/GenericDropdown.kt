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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Reusable generic dropdown component styled to match the SearchBar dimensions.
 *
 * • Height : **56.dp** – cùng chiều cao SearchBar.
 * • Padding : **16.dp** horizontal & **12.dp** vertical.
 * • Corner  : **12.dp** – tròn đồng nhất.
 * • Trailing arrow (down/up) nằm cuối, thay đổi khi mở menu.
 */
@Composable
fun GenericDropdown(
    items: List<String>,
    selectedItem: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeHolder: String = "Chọn...",
) {
    var expanded by remember { mutableStateOf(false) }
    val bgColor = Color.White
    val dropdownWidth = remember { mutableStateOf(0) }
    val localDensity = LocalDensity.current

    Column(modifier = modifier) {
        /* ---------------- Trigger ---------------- */
        Box(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    dropdownWidth.value = coordinates.size.width
                }
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                .background(bgColor)
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row {
                Text(
                    text = selectedItem.takeUnless { it.isNullOrBlank() } ?: placeHolder,
                    fontSize = 26.sp, // Tăng từ 16.sp lên 26.sp
                    color = if (selectedItem.isNullOrBlank()) Color.Gray else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(56.dp)
                )
            }
        }

        /* ---------------- Menu ---------------- */
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(localDensity) { dropdownWidth.value.toDp() })
                .background(Color.White)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            fontSize = 26.sp, // Tăng từ 16.sp lên 26.sp
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        expanded = false
                        onItemSelected(item)
                    },
                    modifier = Modifier.background(Color.White)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenericDropdownPreview() {
    var current by remember { mutableStateOf<String?>(null) }
    val list = listOf("Owner", "Vice", "Admin", "Member")

    MaterialTheme {
        Column(modifier = Modifier.padding(24.dp)) {
            GenericDropdown(
                items = list,
                selectedItem = current,
                onItemSelected = { current = it }
            )
        }
    }
}
