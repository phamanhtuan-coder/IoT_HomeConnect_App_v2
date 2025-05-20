package com.sns.homeconnect_v2.presentation.component

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A composable function that displays a row of information with a label, value, unit, and a state indicator.
 *
 * @param label The label text displayed on the left side of the row.
 * @param value The main value text displayed prominently in the center of the row.
 * @param unit The unit text displayed next to the value, typically in a smaller font size.
 * @param stateColor The background color of the circular state indicator on the right side of the row.
 * @param stateText The text displayed inside the circular state indicator.
 */
@Composable
fun InfoRow(label: String, value: String, unit: String, stateColor: Color, stateText: String)
{
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                color = colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.weight(2f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = unit,
                    fontSize = 12.sp,
                    modifier = Modifier.offset(y = 3.dp),
                    color = colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(stateColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stateText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}