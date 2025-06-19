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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sns.homeconnect_v2.R
import com.sns.homeconnect_v2.data.remote.dto.base.GroupIcon
import com.sns.homeconnect_v2.data.remote.dto.base.GroupIconCategory

/**
 * Một hàm Composable hiển thị một lưới các biểu tượng để người dùng lựa chọn.
 *
 * @param selectedIconLabel Nhãn của biểu tượng hiện được chọn. Điều này được sử dụng để làm nổi bật biểu tượng đã chọn trong lưới.
 * @param onIconSelected Một hàm gọi lại được gọi khi một biểu tượng được chọn. Nó nhận nhãn (String) của biểu tượng đã chọn.
 */

@Composable
fun getGroupIcons(): List<GroupIcon.Vector> = listOf(
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_company), "company", "Công ty", GroupIconCategory.GROUP),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_family), "family", "Gia đình", GroupIconCategory.GROUP),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_government), "government", "Chính phủ", GroupIconCategory.GROUP),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_organization), "organization", "Tổ chức", GroupIconCategory.GROUP),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_personal), "personal", "Cá nhân", GroupIconCategory.GROUP),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_school_group), "school_group", "Nhóm trường", GroupIconCategory.GROUP)
)

@Composable
fun getHouseIcons(): List<GroupIcon.Vector> = listOf(
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_house), name  = "house", label = "Nhà riêng", category = GroupIconCategory.HOUSE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_apartment), name  = "apartment", label = "Căn hộ", category = GroupIconCategory.HOUSE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_villa), name  = "villa", label = "Biệt thự", category = GroupIconCategory.HOUSE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_office), name  = "office", label = "Văn phòng", category = GroupIconCategory.HOUSE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_factory), name  = "factory", label = "Nhà máy", category = GroupIconCategory.HOUSE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_hotel), name  = "hotel", label = "Khách sạn", category = GroupIconCategory.HOUSE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_school), name  = "school", label = "Trường học", category = GroupIconCategory.HOUSE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_store), name  = "store", label = "Cửa hàng", category = GroupIconCategory.HOUSE)
)

@Composable
fun getSpaceIcons(): List<GroupIcon.Vector> = listOf(
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_balcony), "balcony", "Ban công", GroupIconCategory.SPACE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_bathroom), "bathroom", "Phòng tắm", GroupIconCategory.SPACE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_bedroom), "bedroom", "Phòng ngủ", GroupIconCategory.SPACE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_entertainment), "entertainment", "Giải trí", GroupIconCategory.SPACE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_garage), "garage", "Garage", GroupIconCategory.SPACE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_garden), "garden", "Sân vườn", GroupIconCategory.SPACE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_hallway), "hallway", "Hành lang", GroupIconCategory.SPACE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_kitchen), "kitchen", "Phòng bếp", GroupIconCategory.SPACE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_livingroom), "livingroom", "Phòng khách", GroupIconCategory.SPACE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_lobby), "lobby", "Sảnh", GroupIconCategory.SPACE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_rooftop), "rooftop", "Sân thượng", GroupIconCategory.SPACE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_store), "store", "Cửa hàng", GroupIconCategory.SPACE),
    GroupIcon.Vector(ImageVector.vectorResource(R.drawable.ic_workroom), "workroom", "Phòng làm việc", GroupIconCategory.SPACE)
)

@Composable
fun IconPicker(
    category: GroupIconCategory,
    selectedIconName: String?,
    onIconSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxHeight: Int = 400          // chiều cao tối đa cho grid; có thể điều chỉnh
) {
    /* Lấy danh sách theo category */
    val iconList = when (category) {
        GroupIconCategory.GROUP -> getGroupIcons()
        GroupIconCategory.HOUSE -> getHouseIcons()
        GroupIconCategory.SPACE -> getSpaceIcons()
    }

    Column(modifier = modifier) {
        Text(
            text = when (category) {
                GroupIconCategory.GROUP -> "Nhóm người dùng"
                GroupIconCategory.HOUSE -> "Loại nhà"
                GroupIconCategory.SPACE -> "Không gian"
            },
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.heightIn(max = maxHeight.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(iconList, key = { it.name }) { icon ->
                val isSelected = icon.name == selectedIconName
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
                        Icon(
                            imageVector = icon.icon,
                            contentDescription = icon.label,
                            tint = Color(0xFF212121),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(icon.label, style = MaterialTheme.typography.bodySmall, fontSize = 14.sp)
                }
            }
        }
    }
}

/* ----------  PREVIEWS ---------- */

@Preview(showBackground = true)
@Composable
fun IconPickerGroupPreview() {
    var sel by remember { mutableStateOf<String?>(null) }
    IconPicker(
        category = GroupIconCategory.GROUP,
        selectedIconName = sel,
        onIconSelected = { sel = it },
        modifier = Modifier.padding(16.dp)
    )
}

