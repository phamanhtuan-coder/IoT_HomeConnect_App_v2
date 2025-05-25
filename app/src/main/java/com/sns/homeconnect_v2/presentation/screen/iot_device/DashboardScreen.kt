package com.sns.homeconnect_v2.presentation.screen.iot_device

import CustomLineChart
import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.sns.homeconnect_v2.presentation.component.DeviceStatusCard
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.CustomTabRow
import com.sns.homeconnect_v2.presentation.component.widget.GenericDropdown
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.model.DataPoint
import com.sns.homeconnect_v2.presentation.model.DeviceStatusCardData

/**
 * Hàm Composable cho Màn hình Bảng điều khiển.
 * Màn hình này hiển thị dữ liệu cảm biến (nhiệt độ, độ ẩm, khí gas, điện năng)
 * dưới dạng biểu đồ đường và danh sách các thiết bị cho các phòng khác nhau.
 *
 * @param navController NavHostController để điều hướng.
 * @author Nguyễn Thanh Sang
 * @since 25-05-2025
 */
@Composable
fun DashboardScreen(navController: NavHostController) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        var selectedSensorTypeTabIndex by remember { mutableIntStateOf(0) }
        val sensorTypeTabTitles = listOf("Nhiệt độ", "Độ ẩm", "Khí gas", "Điện tiêu thụ")
        var selectedRoomTabIndex by remember { mutableIntStateOf(0) }
        val roomTabTitles = listOf("Phòng khách", "Phòng ngủ", "Phòng bếp", "Phòng chờ")
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
        val livingRoomDevices = listOf(
            DeviceStatusCardData("MÁY BÁO KHÓI", "Báo cháy", true, "0.0 W", "0.0 độ"),
            DeviceStatusCardData("CAMERA 1", "Camera", true, "2.0 W", "30.0 độ"),
            DeviceStatusCardData("ĐÈN TRẦN", "Chiếu sáng", false, "10.0 W", "32.0 độ"),
        )
        val bedroomDevices = listOf(
            DeviceStatusCardData("MÁY LẠNH", "Điều hoà", true, "800.0 W", "20.0 độ"),
            DeviceStatusCardData("ĐÈN ĐẦU GIƯỜNG", "Chiếu sáng", false, "5.0 W", "30.0 độ"),
        )
        val kitchenDevices = listOf(
            DeviceStatusCardData("BẾP TỪ", "Bếp", false, "1500.0 W", "50.0 độ"),
            DeviceStatusCardData("MÁY HÚT KHÓI", "Hút mùi", true, "120.0 W", "38.0 độ"),
        )
        val lobbyDevices = listOf(
            DeviceStatusCardData("MÁY LỌC KHÔNG KHÍ", "Lọc khí", true, "60.0 W", "28.0 độ"),
        )
        val deviceCardLists = listOf(
            livingRoomDevices, // 0
            bedroomDevices,    // 1
            kitchenDevices,    // 2
            lobbyDevices       // 3
        )
        val currentDeviceList = deviceCardLists.getOrNull(selectedRoomTabIndex) ?: emptyList()

        var selectedDay by remember { mutableStateOf<String?>(null) }

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
                    ) {
                        CustomTabRow(
                            tabs = sensorTypeTabTitles,
                            selectedTabIndex = selectedSensorTypeTabIndex,
                            onTabSelected = { selectedSensorTypeTabIndex = it }
                        )
                    }

                    Column (
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
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
                        GenericDropdown(
                            items = listOf("Phòng khách", "Phòng ngủ", "Nhà bếp"),
                            selectedItem = selectedDay,
                            onItemSelected = { selectedDay = it },
                            isTablet = false,
                            placeHolder = "Ngày",
                            leadingIcon = Icons.Default.Today // 👈 truyền icon vào
                        )
                    }

                    Box {
                        CustomTabRow(
                            tabs = roomTabTitles,
                            selectedTabIndex = selectedRoomTabIndex,
                            onTabSelected = { selectedRoomTabIndex = it },
                            modifier = Modifier
                                .fillMaxWidth(),

                        )
                        // Divider nằm dưới cùng, phủ full chiều ngang
                        Divider(
                            color = Color(0xFFE0E0E0), // Xám nhạt
                            thickness = 1.dp,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        currentDeviceList.forEach { device ->
                            DeviceStatusCard(
                                title = device.title,
                                type = device.type,
                                isOn = device.status,
                                powerConsumption = device.power,
                                temperature = device.temp,
                                onDetailsButtonClick = { /* Mở chi tiết thiết bị */ }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDashboardScreen() {
    DashboardScreen(navController = rememberNavController())
}