package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingDown
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.DeviceThermostat
import androidx.compose.material.icons.outlined.WaterDrop
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
import androidx.wear.compose.material3.Icon
import com.sns.homeconnect_v2.presentation.model.SensorDisplayData

@Composable
fun SensorSummaryCard(
    data: SensorDisplayData,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        // Current value
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(18.dp),
            color = Color(0xFFF9F9F9),
            shadowElevation = 8.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = data.icon,
                        contentDescription = null,
                        tint = data.iconTint,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        data.label,
                        color = Color(0xFF404B5A),
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    )
                }
                Text(
                    "${data.currentValue}${data.unit}",
                    color = Color(0xFF232E38),
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        // Highest & lowest cards
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Highest
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFF9F9F9),
                shadowElevation = 8.dp,
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = data.highestIcon,
                            contentDescription = null,
                            tint = data.highestColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            data.highestLabel,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = data.highestColor
                        )
                    }
                    Text(
                        "${data.highestValue}${data.unit}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color(0xFF232E38)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = Color(0xFFB1BAC9),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            data.highestTime,
                            fontSize = 16.sp,
                            color = Color(0xFFB1BAC9)
                        )
                    }
                }
            }
            // Lowest
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFF9F9F9),
                shadowElevation = 8.dp,
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = data.lowestIcon,
                            contentDescription = null,
                            tint = data.lowestColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            data.lowestLabel,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = data.lowestColor
                        )
                    }
                    Text(
                        "${data.lowestValue}${data.unit}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color(0xFF232E38)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = Color(0xFFB1BAC9),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            data.lowestTime,
                            fontSize = 16.sp,
                            color = Color(0xFFB1BAC9)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSensorSummaryCard_Temperature() {
    SensorSummaryCard(
        data = SensorDisplayData(
            icon = Icons.Outlined.DeviceThermostat,
            iconTint = Color(0xFF4193F7),
            label = "Nhiệt độ hiện tại",
            currentValue = "28.5",
            unit = "°C",
            highestValue = "32.1",
            highestTime = "14:30",
            highestColor = Color(0xFF2ECC40),
            highestLabel = "Cao nhất",
            highestIcon = Icons.AutoMirrored.Outlined.TrendingUp,
            lowestValue = "24.3",
            lowestTime = "05:15",
            lowestColor = Color(0xFF4193F7),
            lowestLabel = "Thấp nhất",
            lowestIcon = Icons.AutoMirrored.Outlined.TrendingDown
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSensorSummaryCard_Humidity() {
    SensorSummaryCard(
        data = SensorDisplayData(
            icon = Icons.Outlined.WaterDrop,
            iconTint = Color(0xFF1E88E5),
            label = "Độ ẩm hiện tại",
            currentValue = "67",
            unit = "%",
            highestValue = "82",
            highestTime = "11:05",
            highestColor = Color(0xFF2ECC40),
            highestLabel = "Cao nhất",
            highestIcon = Icons.AutoMirrored.Outlined.TrendingUp,
            lowestValue = "58",
            lowestTime = "01:22",
            lowestColor = Color(0xFF4193F7),
            lowestLabel = "Thấp nhất",
            lowestIcon = Icons.AutoMirrored.Outlined.TrendingDown
        )
    )
}

// Tương tự cho gasData, electricityData...
