package com.sns.homeconnect_v2.presentation.screen.iot_device

import com.sns.homeconnect_v2.presentation.component.CustomLineChart
import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.ChartLegend
import com.sns.homeconnect_v2.presentation.component.RoomTabRow
import com.sns.homeconnect_v2.presentation.component.TimeRangePicker
import com.sns.homeconnect_v2.presentation.component.WeatherInfo
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.GenericDropdown
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.model.DataPoint

/**
 * Hàm Composable đại diện cho màn hình dashboard của ứng dụng IoT Home Connect.
 * Màn hình này hiển thị dữ liệu cảm biến khác nhau dưới dạng biểu đồ đường, cùng với thông tin thời tiết,
 * các tab chọn phòng và các thành phần điều hướng.
 *
 * Dashboard hiển thị dữ liệu cho:
 * - Nhiệt độ
 * - Độ ẩm
 * - Mức độ khí gas
 * - Mức tiêu thụ điện
 *
 * Người dùng có thể tương tác với các dropdown để lọc dữ liệu (mặc dù hiện tại đang sử dụng dữ liệu giả)
 * và chọn các phòng khác nhau để xem dữ liệu cụ thể (hiện tại mặc định là phòng "living").
 *
 * Cấu trúc màn hình bao gồm:
 * - Một `Header` ở trên cùng để điều hướng và hiển thị tiêu đề.
 * - Một thành phần `WeatherInfo`.
 * - Các dropdown để chọn nhóm và nhà (chức năng hiện tại là giả).
 * - `RoomTabRow` để chuyển đổi giữa các phòng khác nhau.
 * - Một loạt các thành phần `CustomLineChart`, mỗi thành phần hiển thị một loại dữ liệu cảm biến khác nhau,
 *   cùng với một `ChartLegend`.
 * - Một `MenuBottom` ở dưới cùng để điều hướng ứng dụng.
 *
 * Dữ liệu cho các biểu đồ hiện được tạo ngẫu nhiên cho mục đích minh họa.
 *
 * @param navController `NavHostController` được sử dụng để điều hướng giữa các màn hình khác nhau
 *                      trong ứng dụng.
 * @author Nguyễn Thanh Sang
 * @since 29-05-2025
 */
@Composable
fun DashboardScreen(navController: NavHostController) {
    IoTHomeConnectAppTheme {
        val chartNames = listOf(
            "Nhiệt độ",
            "Độ ẩm",
            "Khí gas",
            "Điện tiêu thụ"
        )
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
        val chartDataList = listOf(
            temperatureData, // 0
            humidityData,    // 1
            gasData,         // 2
            electricityData  // 3
        )
        var current by remember { mutableStateOf<String?>(null) }
        var activeRoom by remember { mutableStateOf("living") }
        val colorScheme = MaterialTheme.colorScheme

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
                                .padding(16.dp)
                        ) {
                            WeatherInfo()
                        }
                    }

                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ) { }

                    Column (
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GenericDropdown(
                                items = listOf("Phòng khách", "Phòng ngủ", "Nhà bếp"),
                                selectedItem = current,
                                onItemSelected = { current = it },
                                placeHolder = "Chọn nhóm",
                                modifier = Modifier.weight(1f)
                            )
                            GenericDropdown(
                                items = listOf("Phòng khách", "Phòng ngủ", "Nhà bếp"),
                                selectedItem = current,
                                onItemSelected = { current = it },
                                placeHolder = "Chọn nhà",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        RoomTabRow(
                            activeRoom = activeRoom,
                            onRoomChange = { activeRoom = it }
                        )

                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            chartNames.forEachIndexed { i, label ->
                                TimeRangePicker() // Nếu cần 4 bộ lọc riêng cho từng biểu đồ
                                CustomLineChart(
                                    dataPoints = chartDataList[i],
                                    chartWidth = 250.dp,
                                    chartHeight = 140.dp,
                                    yLabelCount = 5,
                                    lineColor = chartColors[i],
                                    fillColor = chartFillColors[i],
                                    backgroundColor = Color(0xFFF7F2FA),
                                    showFill = true,
                                    cornerRadius = 8.dp,
                                    labelTextSize = 12f
                                )
                                ChartLegend(
                                    color = chartColors[i],
                                    label = label
                                )
                            }
                        }
                    }
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