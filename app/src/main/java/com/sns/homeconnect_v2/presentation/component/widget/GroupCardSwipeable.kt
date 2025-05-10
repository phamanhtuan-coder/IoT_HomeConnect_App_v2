package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GroupCardSwipeable(
    groupName: String,
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
            Spacer(modifier = Modifier.width(8.dp))
            ActionIcon(
                onClick = onDelete,
                backgroundColor = Color(0xFFF44336),
                icon = Icons.Default.Delete
            )
        },
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = groupName)
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GroupCardSwipeablePreview() {
    MaterialTheme {
        val groups = remember {
            mutableStateListOf(
                GroupUi(id = 1, name = "Gia đình", isRevealed = false),
                GroupUi(id = 2, name = "Marketing", isRevealed = false),
                GroupUi(id = 3, name = "Kỹ thuật", isRevealed = false)
            )
        }

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            itemsIndexed(groups) { index, group ->
                GroupCardSwipeable(
                    groupName = group.name,
                    isRevealed = group.isRevealed,
                    onExpandOnly = {
                        groups.forEachIndexed { i, g ->
                            groups[i] = g.copy(isRevealed = i == index)
                        }
                    },
                    onCollapse = {
                        groups[index] = group.copy(isRevealed = false)
                    },
                    onDelete = {
                        groups.removeAt(index)
                    },
                    onEdit = {
                        // handle edit logic
                    }
                )
            }
        }
    }
}

// 🛠 Mô phỏng đối tượng nhóm
data class GroupUi(
    val id: Int,
    val name: String,
    val isRevealed: Boolean
)
