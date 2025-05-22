package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

/**
 * Hiển thị hình ảnh đại diện của người dùng.
 *
 * Nếu `avatarUrl` được cung cấp là trống, một hình đại diện giữ chỗ mặc định với biểu tượng người sẽ được hiển thị.
 * Ngược lại, hình ảnh từ `avatarUrl` sẽ được tải và hiển thị.
 * Hình đại diện được cắt theo hình tròn và có kích thước cố định.
 *
 * @param avatarUrl URL của hình ảnh đại diện của người dùng. Có thể để trống.
 * @author Nguyễn Thanh Sang
 * @since 21-05-25
 */

@Composable
fun UserAvatar(userImageUrl: String) {
    if (userImageUrl.isBlank()) {
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
            painter = rememberAsyncImagePainter(userImageUrl),
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
fun UserAvatarPreview() {
    UserAvatar("https://i.pravatar.cc/150?img=8")
}