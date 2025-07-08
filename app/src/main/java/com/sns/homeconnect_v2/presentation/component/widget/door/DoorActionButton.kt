package com.sns.homeconnect_v2.presentation.component.widget.door

import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoorBack
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sns.homeconnect_v2.R

@Composable
fun DoorActionButton(
    isOpen: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val label = if (isOpen) "Đóng cửa" else "Mở cửa"
    val iconRes = if (isOpen) R.drawable.door_closed else R.drawable.door_open
    val backgroundColor = if (isOpen) Color(0xFFFFEBEE) else Color(0xFFE3F2FD)
    val contentColor = if (isOpen) Color(0xFFD32F2F) else Color(0xFF1565C0)

    Surface(
        onClick = onClick,
        modifier = modifier
            .width(160.dp)
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier.size(56.dp),
                tonalElevation = 2.dp
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize(),
                    colorFilter = ColorFilter.tint(contentColor)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = label,
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInteractiveDoorActionButton() {
    var isOpen by remember { mutableStateOf(false) }

    DoorActionButton(
        isOpen = isOpen,
        onClick = { isOpen = !isOpen }
    )
}
