package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Castle
import androidx.compose.material.icons.filled.Cottage
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Villa
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sns.homeconnect_v2.data.remote.dto.base.GroupIcon

/**
 * Một hàm Composable hiển thị một lưới các biểu tượng để người dùng lựa chọn.
 *
 * @param selectedIconLabel Nhãn của biểu tượng hiện được chọn. Điều này được sử dụng để làm nổi bật biểu tượng đã chọn trong lưới.
 * @param onIconSelected Một hàm gọi lại được gọi khi một biểu tượng được chọn. Nó nhận nhãn (String) của biểu tượng đã chọn.
 */
@Composable
fun IconPicker(
    selectedIconLabel: String,
    onIconSelected: (String) -> Unit
) {
    val iconOptions = listOf(
        GroupIcon.Vector(Icons.Default.Home, "home", "Nhà"),
        GroupIcon.Vector(Icons.Default.Work, "work", "Cơ quan"),
        GroupIcon.Vector(Icons.Default.School, "school", "Trường"),
        GroupIcon.Vector(Icons.Default.AccountBalance, "account_balance", "Ngân hàng"),
        GroupIcon.Vector(Icons.Default.Apartment, "apartment", "Căn hộ"),
        GroupIcon.Vector(Icons.Default.Hotel, "hotel", "Khách sạn"),
        GroupIcon.Vector(Icons.Default.Villa, "villa", "Biệt thự"),
        GroupIcon.Vector(Icons.Default.Cottage, "cottage", "Nhà gỗ"),
        GroupIcon.Vector(Icons.Default.Castle, "castle", "Lâu đài"),
        GroupIcon.Vector(Icons.Default.LocalLibrary, "library", "Thư viện")
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Chọn biểu tượng:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .heightIn(max = 500.dp)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(iconOptions, key = { it.name }) { icon ->
                val isSelected = icon.name == selectedIconLabel
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .size(72.dp)
                        .clickable { onIconSelected(icon.name) }
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = .15f) else Color.Transparent,
                        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier.size(48.dp)
                    ) {
                        when (icon) {
                            is GroupIcon.Vector -> Icon(
                                imageVector = icon.icon,
                                contentDescription = icon.label,
                                tint = Color(0xFF212121),
                                modifier = Modifier.padding(8.dp)
                            )
                            is GroupIcon.Image -> Image(
                                painter = icon.painter,
                                contentDescription = icon.label,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(icon.label, style = MaterialTheme.typography.bodySmall, fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IconPickerCombinedPreview() {
    var selectedLabel by remember { mutableStateOf("Nhà") }

    IconPicker(
        selectedIconLabel = selectedLabel,
        onIconSelected = { selectedLabel = it }
    )
}
