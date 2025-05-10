package com.sns.homeconnect_v2.presentation.screen.group

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import com.sns.homeconnect_v2.presentation.component.widget.GroupCardSwipeable
import com.sns.homeconnect_v2.presentation.component.widget.GroupUi

@Composable
fun GroupScreen(modifier: Modifier = Modifier) {
    val groups = remember {
        mutableStateListOf(
            GroupUi(1, "Gia đình", 5, false, Icons.Default.Group, Color.Blue),
            GroupUi(2, "Marketing", 3, false, Icons.Default.Home, Color.Red),
            GroupUi(3, "Kỹ thuật", 7, false, Icons.Default.Group, Color.Green)
        )
    }

    IoTHomeConnectAppTheme {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            itemsIndexed(groups) { index, group ->
                Spacer(Modifier.height(8.dp))
                GroupCardSwipeable(
                    groupName = group.name,
                    memberCount = group.members,
                    icon = group.icon,
                    iconColor = group.iconColor,
                    isRevealed = group.isRevealed,
                    onExpandOnly = {
                        groups.indices.forEach { i ->
                            groups[i] = groups[i].copy(isRevealed = i == index)
                        }
                    },
                    onCollapse = {
                        groups[index] = group.copy(isRevealed = false)
                    },
                    onDelete = { groups.removeAt(index) },
                    onEdit = { /* TODO */ }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GroupScreenPreview() {
    GroupScreen()
}
