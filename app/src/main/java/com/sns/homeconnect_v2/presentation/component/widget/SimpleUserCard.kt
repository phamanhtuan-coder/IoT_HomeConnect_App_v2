package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SimpleUserCard(
    userName: String,
    avatarUrl: String,
    modifier: Modifier = Modifier  // ← thêm dòng này
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserAvatar(avatarUrl)
        Spacer(Modifier.width(12.dp))
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleUserCardPreview() {
    val users = listOf(
        "Nguyễn Văn A" to "",
        "Trần Thị B" to "https://i.pravatar.cc/150?img=8",
        "Lê Văn C" to "https://i.pravatar.cc/150?img=12",
        "Phạm Thị D" to "",
        "Hoàng Văn E" to "https://i.pravatar.cc/150?img=20"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        users.forEach { (name, avatar) ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFD8E4E8), // cùng màu như trước
                modifier = Modifier.fillMaxWidth()
            ) {
                SimpleUserCard(
                    userName = name,
                    avatarUrl = avatar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

