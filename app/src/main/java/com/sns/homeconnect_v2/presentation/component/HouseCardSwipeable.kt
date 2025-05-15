package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Castle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.presentation.component.widget.ActionIcon
import com.sns.homeconnect_v2.presentation.component.widget.SwipeableItemWithActions

@Composable
fun HouseCardSwipeable(
    houseName: String,
    spaceCount: Int,
    icon: ImageVector = Icons.Default.Home,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    isRevealed: Boolean,
    onExpandOnly: () -> Unit,
    onCollapse: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    SwipeableItemWithActions(
        isRevealed = isRevealed,
        onExpanded = onExpandOnly,
        onCollapsed = onCollapse,
        actions = {
            ActionIcon(
                onClick = onEdit,
                backgroundColor = Color(0xFF4CAF50),
                icon = Icons.Default.Edit
            )
            Spacer(Modifier.width(8.dp))
            ActionIcon(
                onClick = onDelete,
                backgroundColor = Color(0xFFF44336),
                icon = Icons.Default.Delete
            )
        }
    ) {
        Row(
            modifier = Modifier
                .background(color = Color(0xFFD8E4E8))
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = houseName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "$spaceCount space",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Arrow",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HouseCardSwipeablePreview() {
    val houses = remember {
        mutableStateListOf(
            HouseUi(1, "Main house", 3, false, Icons.Default.Home, Color.Black),
            HouseUi(2, "Villa", 5, false, Icons.Default.Castle, Color.Red),
            HouseUi(3, "Office", 2, false, Icons.Default.Home, Color.DarkGray)
        )
    }

    LazyColumn {
        itemsIndexed(houses) { index, house ->
            Spacer(Modifier.height(8.dp))
            HouseCardSwipeable(
                houseName = house.name,
                spaceCount = house.spaces,
                icon = house.icon,
                iconColor = house.iconColor,
                isRevealed = house.isRevealed,
                onExpandOnly = {
                    houses.indices.forEach { i ->
                        houses[i] = houses[i].copy(isRevealed = i == index)
                    }
                },
                onCollapse = {
                    houses[index] = house.copy(isRevealed = false)
                },
                onDelete = { houses.removeAt(index) },
                onEdit = { /* TODO */ }
            )
        }
    }
}