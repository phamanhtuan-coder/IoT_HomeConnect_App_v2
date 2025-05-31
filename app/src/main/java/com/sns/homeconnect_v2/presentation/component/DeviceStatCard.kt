package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.Text
import com.sns.homeconnect_v2.presentation.model.DeviceStatCardItem

@Composable
fun DeviceStatCard(
    item: DeviceStatCardItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(350.dp)
            .height(120.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFD8E4E8))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = item.iconColor,
                modifier = Modifier.size(46.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = item.title,
                    color = Color(0xFF888E9C),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = item.value,
                    color = Color(0xFF111111),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun DeviceStatPrevice() {
    val item = DeviceStatCardItem(
        icon = Icons.Default.Memory,
        iconColor = Color(0xFFF54B63),
        title = "Tổng số thiết bị",
        value = "12 thiết bị")
    DeviceStatCard(item = item)
}