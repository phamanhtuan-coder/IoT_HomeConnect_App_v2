package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.Text
import com.sns.homeconnect_v2.presentation.model.FeatureButtonItem

@Composable
fun FeatureButton(
    item: FeatureButtonItem,
    modifier: Modifier = Modifier,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = Color(0xFFE3F2FD),
) {
    Column(
        modifier = Modifier
            .width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box (
            modifier = modifier
                .size(62.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(backgroundColor)
                .clickable { item.onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = item.label,
            color = Color(0xFF333333),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
fun FeatureButtonPreview() {
    val item = FeatureButtonItem(
        icon = Icons.Default.Add,
        label = "Thêm thiết bị",
        onClick = {})

    FeatureButton(item = item)
}