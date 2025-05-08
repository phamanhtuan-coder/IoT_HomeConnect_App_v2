package com.sns.homeconnect_v2.presentation.component.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LineChart(
    title: String,
    data: List<Float>,
    labels: List<String>,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val maxValue = (data.maxOrNull() ?: 1f) * 1.2f
            val stepX = size.width / (labels.size - 1)
            val stepY = size.height / maxValue

            // Draw Lines
            for (i in 1 until data.size) {
                drawLine(
                    color = color,
                    start = Offset((i - 1) * stepX, size.height - data[i - 1] * stepY),
                    end = Offset(i * stepX, size.height - data[i] * stepY),
                    strokeWidth = 4f
                )
            }

            // Draw Points
            data.forEachIndexed { index, value ->
                drawCircle(
                    color = color,
                    center = Offset(index * stepX, size.height - value * stepY),
                    radius = 8f
                )
            }
        }
    }
}