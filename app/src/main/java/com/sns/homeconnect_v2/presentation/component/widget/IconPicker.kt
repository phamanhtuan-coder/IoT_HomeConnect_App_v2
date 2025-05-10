package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IconPicker(
    iconOptions: List<Pair<ImageVector, String>>,
    selectedIcon: ImageVector,
    onIconSelected: (ImageVector) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Chọn biểu tượng:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .height(320.dp)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(iconOptions, key = { it.second }) { (icon, label) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .size(72.dp)
                        .clickable { onIconSelected(icon) }
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (icon == selectedIcon) MaterialTheme.colorScheme.primary.copy(alpha = .15f) else Color.Transparent,
                        border = if (icon == selectedIcon) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = Color(0xFF212121),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(label, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IconAndColorPickerPreview() {
    var selectedIcon by remember { mutableStateOf(Icons.Default.Home) }
    val iconOptions = listOf(
        Icons.Default.Home to "Nhà",
        Icons.Default.Work to "Cơ quan",
        Icons.Default.School to "Trường"
    )

    Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
        IconPicker(
            iconOptions = iconOptions,
            selectedIcon = selectedIcon,
            onIconSelected = { selectedIcon = it }
        )
    }
}
