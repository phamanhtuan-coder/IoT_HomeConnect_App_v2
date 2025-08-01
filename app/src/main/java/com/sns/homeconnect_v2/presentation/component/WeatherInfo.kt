package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable function để hiển thị thông tin thời tiết.
 *
 * Hàm này nhận các tham số thời tiết khác nhau làm đầu vào và hiển thị chúng ở định dạng thân thiện với người dùng.
 * Nó điều chỉnh bố cục của mình dựa trên việc thiết bị có phải là máy tính bảng hay không.
 *
 * @param isTablet Một giá trị boolean cho biết thiết bị có phải là máy tính bảng hay không. Mặc định là false.
 * @param thoiGian Thời gian và ngày của thông tin thời tiết. Mặc định là "22/05/2025 15:30".
 * @param thoiTiet Điều kiện thời tiết (ví dụ: "Nắng", "Nhiều mây"). Mặc định là "Nắng".
 * @param nhietDo Nhiệt độ theo độ C. Mặc định là "31°C".
 * @param viTri Vị trí của thông tin thời tiết. Mặc định là "Hà Nội, Việt Nam".
 * @param doAm Phần trăm độ ẩm. Mặc định là "62".
 * @param tamNhin Tầm nhìn theo kilômét. Mặc định là "10".
 * @param gio Tốc độ gió theo kilômét trên giờ. Mặc định là "18".
 * @author Nguyễn THanh Sang
 * @since 29-05-2025
 */
@Composable
fun WeatherInfo(
    isTablet: Boolean = false,
    thoiGian: String = "22/05/2025 15:30",
    thoiTiet: String = "Sunny",
    nhietDo: String = "31°C",
    viTri: String = "Hà Nội, Việt Nam",
    doAm: String = "62",
    tamNhin: String = "10",
    gio: String = "18"
//    viewModel: WeatherViewModel = hiltViewModel(),
//    permissionManager: PermissionManager = hiltViewModel<PermissionViewModel>().permissionManager
) {
//    val screenWidth = LocalConfiguration.current.screenWidthDp
//    val isTablet = screenWidth > 600
//    val context = LocalContext.current
//    val weatherState by viewModel.weatherState.collectAsState()
//    var showRationaleDialog by remember { mutableStateOf(false) }
    // Permission launcher
//    val permissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        if (permissions.values.all { it }) {
//            viewModel.onPermissionGranted(context)
//        } else {
//            viewModel.onPermissionDenied()
//        }
//    }
    // Handle weather state and permission requests
//    LaunchedEffect(weatherState) {
//        when (weatherState) {
//            is WeatherState.Idle -> {
//                viewModel.fetchWeather(context)
//            }
//            is WeatherState.PermissionRequired -> {
//                if (permissionManager.shouldShowRationale(context as Activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                    showRationaleDialog = true
//                } else {
//                    permissionLauncher.launch(permissionManager.getLocationWifiPermissions())
//                }
//            }
//            else -> { /* Do nothing */ }
//        }
//    }

    // Show rationale dialog
//    if (showRationaleDialog) {
//        WarningDialog(
//            title = "Location Permission Needed",
//            text = "This app needs location access to fetch weather data for your current location.",
//            onConfirm = {
//                showRationaleDialog = false
////                permissionLauncher.launch(permissionManager.getLocationWifiPermissions())
//            },
//            onDismiss = {
//                showRationaleDialog = false
////                viewModel.onPermissionDenied()
//            }
//        )
//    }

//    when (weatherState) {
//        is WeatherState.Success -> {
//            val data = (weatherState as WeatherState.Success).weatherResponse
//            val weatherCondition = data.current.condition.text
//            val iconThoiTiet = getWeatherIcon(weatherCondition)
//            val iconThoiGian = if (isDayTime()) Icons.Filled.WbSunny else Icons.Filled.NightsStay
//            val thoiGianHienTai = formatDateTime(data.location.localtime)
//            val thoiTietHienTai = data.current.condition.text
//            val nhietDoHienTai = "${data.current.tempC}°C"
//            val viTriHienTai = "${data.location.name}, ${data.location.country}"
//            val doAm = "${data.current.humidity}"
//            val tamNhin = "${data.current.visKm}"
//            val tocDoGio = "${data.current.windKph}"


    Column(
        modifier = Modifier
            .background(
                color = Color(0xFFF7F2FA), // Nền đồng bộ với DashboardScreen
                shape = RoundedCornerShape(10.dp) // Giảm bo góc
            )
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp)) // Viền nhẹ
            .padding(10.dp), // Giảm padding
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.WbSunny,
                contentDescription = null,
                tint = Color(0xFFF57C00), // Cam nhạt cho icon thời tiết
                modifier = Modifier.size(if (isTablet) 48.dp else 40.dp) // Giảm kích thước icon
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp), // Thêm padding để tránh cắt chữ
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(3.dp) // Giảm khoảng cách
            ) {
                Text(
                    text = thoiTiet,
                    color = Color(0xFF1E88E5), // Xanh dương nhẹ cho thời tiết
                    fontSize = 14.sp, // Giảm kích thước chữ
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // Ngăn cắt chữ
                )
                Text(
                    text = thoiGian,
                    color = Color(0xFF6B7280), // Xám trung cho thời gian
                    fontSize = 11.sp, // Giảm kích thước chữ
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = viTri,
                    color = Color(0xFF9CA3AF), // Xám nhạt cho vị trí
                    fontSize = 11.sp, // Giảm kích thước chữ
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = nhietDo,
                    color = Color(0xFFE65100), // Cam đậm cho nhiệt độ
                    fontSize = if (isTablet) 24.sp else 20.sp, // Giảm kích thước chữ
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Filled.WbSunny,
                    contentDescription = null,
                    tint = Color(0xFFE65100), // Cam đậm cho icon nhiệt độ
                    modifier = Modifier.size(if (isTablet) 24.dp else 20.dp) // Giảm kích thước icon
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp), // Giảm khoảng cách
            thickness = 1.dp,
            color = Color(0xFFE0E0E0)
        )
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 80.dp),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                WeatherWidget(
                    icon = Icons.Filled.WaterDrop,
                    value = "$doAm%",
                    label = "Độ ẩm",
                    isTablet = isTablet,
                    iconTint = Color(0xFF1E88E5) // Xanh dương nhẹ cho widget
                )
            }
            item {
                WeatherWidget(
                    icon = Icons.Filled.Visibility,
                    value = "$tamNhin km",
                    label = "Tầm nhìn",
                    isTablet = isTablet,
                    iconTint = Color(0xFF1E88E5)
                )
            }
            item {
                WeatherWidget(
                    icon = Icons.Filled.Air,
                    value = "$gio km/h",
                    label = "Phong",
                    isTablet = isTablet,
                    iconTint = Color(0xFF1E88E5)
                )
            }
        }
    }
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colors = listOf(Color(0xFF6EC6FF), Color(0xFF2196F3))
//                        ),
//                        shape = RoundedCornerShape(bottomStartPercent = 25, bottomEndPercent = 25)
//                    )
//                    .padding(bottom = 16.dp)
//            ) {
//
//            }
//        }
//        is WeatherState.Loading -> {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//        is WeatherState.Error -> {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = (weatherState as WeatherState.Error).message,
//                    color = Color.Red,
//                    fontSize = 16.sp
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(
//                    onClick = { permissionManager.openAppSettings() },
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
//                ) {
//                    Text("Open Settings", color = Color.White)
//                }
//            }
//        }
//        is WeatherState.PermissionRequired, is WeatherState.Idle -> {
//            // Show a placeholder or nothing
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "Waiting for location permission...",
//                    color = Color.Gray,
//                    fontSize = 16.sp
//                )
//            }
//        }
}



//fun isDayTime(): Boolean {
//    val hour = LocalTime.now().hour
//    return hour in 6..18
//}
//
//fun getWeatherIcon(weatherCondition: String): ImageVector {
//    return when (weatherCondition.lowercase()) {
//        "sunny", "clear" -> Icons.Filled.WbSunny
//        "partly cloudy", "cloudy" -> Icons.Filled.Visibility
//        "rain", "showers", "drizzle" -> Icons.Filled.WaterDrop
//        "snow", "sleet" -> Icons.Filled.NightsStay
//        "thunderstorm" -> Icons.Filled.Air
//        else -> Icons.Filled.Visibility
//    }
//}
//
//fun formatDateTime(input: String): String {
//    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
//    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
//    val dateTime = LocalDateTime.parse(input, inputFormatter)
//    return dateTime.format(outputFormatter)
//}

@Preview(showBackground = true)
@Composable
fun WeatherInfoPreview() {
    WeatherInfo()
}
