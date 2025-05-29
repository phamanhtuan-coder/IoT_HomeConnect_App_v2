package com.sns.homeconnect_v2.presentation.screen.iot_device

import com.sns.homeconnect_v2.presentation.component.CustomLineChart
import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingDown
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.DeviceThermostat
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.ChartLegend
import com.sns.homeconnect_v2.presentation.component.SensorSummaryCard
import com.sns.homeconnect_v2.presentation.component.TimeTabWithCalendar
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.model.DataPoint
import com.sns.homeconnect_v2.presentation.model.SensorDisplayData

/**
 * Composable function for the Dashboard Detail Screen.
 * This screen displays sensor data (temperature, humidity, gas, electricity)
 * in the form of line charts and a list of devices for different rooms.
 *
 * @param navController NavHostController for navigation.
 * @author Nguyễn Thanh Sang
 * @since 25-05-2025
 * @updated 29-05-2025 by Nguyễn Thanh Sang
 */
@Composable
fun DashboardDetailScreen(navController: NavHostController) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        var selectedSensorTypeTabIndex by remember { mutableIntStateOf(0) }
        val temperatureData = (0..23).map { hour ->
            val temperatureValue = (14 + 10 * kotlin.math.sin(Math.PI * (hour - 6) / 15)).toFloat() + (0..2).random()
            DataPoint("%02d:00".format(hour), temperatureValue)
        }
        val humidityData = (0..23).map { hour ->
            val humidityValue = (85 - (hour * 1.2) + (0..3).random()).toFloat()
            DataPoint("%02d:00".format(hour), humidityValue)
        }
        val gasData = (0..23).map { hour ->
            val gasValue = 0.2f + (0..3).random() * 0.01f
            DataPoint("%02d:00".format(hour), gasValue)
        }
        val electricityData = (0..23).map { hour ->
            val electricityUsage = when (hour) {
                in 0..5 -> 0.2f + (0..3).random() * 0.01f
                in 6..16 -> 0.15f + (0..2).random() * 0.01f
                else -> 0.3f + (0..4).random() * 0.01f
            }
            DataPoint("%02d:00".format(hour), electricityUsage)
        }
        // Ví dụ: 4 loại, màu giống mẫu ảnh
        val chartColors = listOf(
            Color(0xFF26A69A), // Xanh ngọc - Nhiệt độ
            Color(0xFF5C6BC0), // Xanh tím - Độ ẩm
            Color(0xFFE57373), // Đỏ - Khí gas
            Color(0xFFFFB300)  // Vàng - Điện tiêu thụ
        )
        val chartFillColors = listOf(
            Color(0x5526A69A), // Xanh ngọc nhạt
            Color(0x555C6BC0), // Xanh tím nhạt
            Color(0x55E57373), // Đỏ nhạt
            Color(0x55FFB300)  // Vàng nhạt
        )
        val chartNames = listOf(
            "Nhiệt độ",
            "Độ ẩm",
            "Khí gas",
            "Điện tiêu thụ"
        )
        val chartDataList = listOf(
            temperatureData, // 0
            humidityData,    // 1
            gasData,         // 2
            electricityData  // 3
        )
        val selectedSensorLineColor = chartColors[selectedSensorTypeTabIndex]
        val selectedSensorFillColor = chartFillColors[selectedSensorTypeTabIndex]
        val selectedSensorChartLabel = chartNames[selectedSensorTypeTabIndex]
        val selectedSensorData = chartDataList.getOrNull(selectedSensorTypeTabIndex) ?: temperatureData

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type          = "Back",
                    title         = "Space Details"
                )
            },
            containerColor = Color.White,
            bottomBar = {
                MenuBottom(navController)
            }
        ) { scaffoldPadding ->
            LazyColumn (
                modifier= Modifier
                    .padding(scaffoldPadding)
            ) {
                item {
                    ColoredCornerBox(
                        cornerRadius = 40.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text          = "Nhiệt độ",
                                color         = Color.White,
                                fontSize      = 30.sp,
                                fontWeight    = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ) { }

                    Column (
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TimeTabWithCalendar()
                        CustomLineChart(
                            dataPoints = selectedSensorData,
                            chartWidth = 350.dp,
                            chartHeight = 240.dp,
                            yLabelCount = 5,
                            lineColor = selectedSensorLineColor,
                            fillColor = selectedSensorFillColor,
                            backgroundColor = Color(0xFFF7F2FA),
                            showFill = true,
                            cornerRadius = 8.dp,
                            labelTextSize = 12f
                        )
                        ChartLegend(
                            color = selectedSensorLineColor,
                            label = selectedSensorChartLabel
                        )
                    }

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
            }
        }
    }
}

@Preview
@Composable
fun PreviewDashboardDetailScreen() {
    DashboardDetailScreen(navController = rememberNavController())
}