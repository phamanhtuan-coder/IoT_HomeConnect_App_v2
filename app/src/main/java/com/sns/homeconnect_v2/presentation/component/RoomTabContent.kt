package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sns.homeconnect_v2.presentation.model.SpaceTab

@Composable
fun RoomTabContent(room: SpaceTab, isActive: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .wrapContentWidth()
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = if (isActive) Color(0xFF2979FF) else Color(0xFFF4F7FB),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = room.iconRes),   // ✅ dùng painterResource
                contentDescription = room.name,
                tint = if (isActive) Color.White else Color(0xFF404B5A),
                modifier = Modifier.size(34.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = room.name,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp,
            color = if (isActive) Color(0xFF2979FF) else Color(0xFF404B5A),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}