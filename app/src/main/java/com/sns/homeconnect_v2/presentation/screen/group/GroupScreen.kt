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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.FabChild
import com.sns.homeconnect_v2.presentation.component.widget.GroupCardSwipeable
import com.sns.homeconnect_v2.presentation.component.widget.GroupUi
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.LabeledBox
import com.sns.homeconnect_v2.presentation.component.widget.RadialFab
import com.sns.homeconnect_v2.presentation.component.widget.SearchBar

@Composable
fun GroupScreen(modifier: Modifier = Modifier) {
    val fabChildren = listOf(
        FabChild(
            icon = Icons.Default.Edit,
            onClick = { /* TODO: sửa */ }
        ),
        FabChild(
            icon = Icons.Default.Delete,
            containerColor = Color.Red,
            onClick = { /* TODO: xoá */ }
        ),
        FabChild(
            icon = Icons.Default.Share,
            onClick = { /* TODO: share */ }
        )
    )


    val groups = remember {
        mutableStateListOf(
            GroupUi(1, "Gia đình", 5, false, Icons.Default.Group, Color.Blue),
            GroupUi(2, "Marketing", 3, false, Icons.Default.Home, Color.Red),
            GroupUi(3, "Kỹ thuật", 7, false, Icons.Default.Group, Color.Green)
        )
    }

    IoTHomeConnectAppTheme {
        Scaffold(
            containerColor = Color.White,
            floatingActionButton = {
                RadialFab(
                    items      = fabChildren,
                    radius     = 120.dp,        // ↑ bán kính ≥ mainSize + miniSize
                    startDeg   = -90f,          // góc bắt đầu –90° (mở thẳng lên)
                    sweepDeg   = -90f,         // quét 90°
                    onParentClick = { /* add new group */ }
                )
            },
            floatingActionButtonPosition = FabPosition.End
        ) { inner ->
            Column (
                modifier= Modifier.padding(inner)
            ) {
            ColoredCornerBox(
                backgroundColor = Color(0xFF3A4750),
                cornerRadius = 40.dp
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    SearchBar(
                        modifier = Modifier
                            .width(300.dp),
                        onSearch = { query ->
                            /* TODO: điều kiện search */
                        }
                    )
                }
            }
            InvertedCornerHeader(
                backgroundColor = Color.White,
                overlayColor = Color(0xFF3A4750)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LabeledBox(
                        label = "Số nhóm",
                        value = "4"
                    )
                }
            }
            LazyColumn(modifier = modifier
                .fillMaxSize()
            ) {
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
    }
}


@Preview(showBackground = true)
@Composable
fun GroupScreenPreview() {
    GroupScreen()
}
