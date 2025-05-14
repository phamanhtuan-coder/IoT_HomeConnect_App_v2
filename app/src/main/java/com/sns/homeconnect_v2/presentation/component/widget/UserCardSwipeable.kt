package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun UserCardSwipeable(
    userName: String,
    role: String,
    avatarUrl: String = "",
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
            UserAvatar(avatarUrl)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    )
                )
                Text(
                    text = role,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun UserAvatar(avatarUrl: String) {
    if (avatarUrl.isBlank()) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFBDBDBD)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default avatar",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    } else {
        Image(
            painter = rememberAsyncImagePainter(avatarUrl),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserCardSwipeablePreview() {
    val users = listOf(
        Triple("Nguyễn Văn A", "Owner", ""),
        Triple("Trần Thị B", "Vice", "https://i.pravatar.cc/150?img=8"),
        Triple("Lê Văn C", "Admin", "https://i.pravatar.cc/150?img=12"),
        Triple("Phạm Thị D", "Member", ""),
        Triple("Hoàng Văn E", "Member", "https://i.pravatar.cc/150?img=20")
    )

    val revealStates = remember { users.map { mutableStateOf(false) } }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        users.forEachIndexed { index, (name, role, avatar) ->
            UserCardSwipeable(
                userName = name,
                role = role,
                avatarUrl = avatar,
                isRevealed = revealStates[index].value,
                onExpandOnly = {
                    revealStates.forEachIndexed { i, state ->
                        state.value = i == index
                    }
                },
                onCollapse = {
                    revealStates[index].value = false
                },
                onDelete = {},
                onEdit = {}
            )
        }
    }
}
