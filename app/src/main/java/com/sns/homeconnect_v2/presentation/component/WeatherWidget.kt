package com.sns.homeconnect_v2.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
/**
 * Hàm Composable để hiển thị một widget thời tiết.
 *
 * Widget này hiển thị một biểu tượng, một giá trị và một nhãn, thường đại diện
 * cho một thông tin thời tiết (ví dụ: nhiệt độ, độ ẩm).
 * Giao diện và kích thước của widget có thể được điều chỉnh dựa trên việc
 * nó được hiển thị trên máy tính bảng hay thiết bị nhỏ hơn.
 *
 * @param icon [ImageVector] sẽ được hiển thị làm biểu tượng cho widget.
 * @param value [String] giá trị sẽ được hiển thị (ví dụ: "25°C", "70%").
 * @param label [String] nhãn mô tả giá trị (ví dụ: "Nhiệt độ", "Độ ẩm").
 * @param isTablet [Boolean] cho biết widget có đang được hiển thị trên máy tính bảng hay không.
 *                 Điều này ảnh hưởng đến padding, chiều rộng, chiều cao và kích thước phông chữ.
 *
 */
@Composable
fun WeatherWidget(
    icon: ImageVector,
    value: String,
    label: String,
    isTablet: Boolean
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
//                permissionLauncher.launch(permissionManager.getLocationWifiPermissions())
//            },
//            onDismiss = {
//                showRationaleDialog = false
//                viewModel.onPermissionDenied()
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
//            val viTriHienTai = formatLocation(data.location.name, data.location.country)
//            val doAm = "${data.current.humidity}"
//            val tamNhin = "${data.current.visKm}"
//            val tocDoGio = "${data.current.windKph}"

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
//                Column(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .background(
//                            color = Color.White.copy(alpha = 0.7f),
//                            shape = RoundedCornerShape(25.dp)
//                        )
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.SpaceBetween,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = iconThoiGian,
//                            contentDescription = null,
//                            tint = Color(0xFF424242),
//                            modifier = Modifier.size(if (isTablet) 80.dp else 40.dp)
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Column(horizontalAlignment = Alignment.Start) {
//                            Text(
//                                text = thoiGianHienTai,
//                                color = Color(0xFF424242),
//                                fontSize = 14.sp
//                            )
//                            Text(
//                                text = thoiTietHienTai,
//                                color = Color(0xFF424242),
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Text(
//                                text = viTriHienTai,
//                                color = Color(0xFF424242),
//                                fontSize = 14.sp,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis
//                            )
//                        }
//                        Spacer(modifier = Modifier.width(10.dp))
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Text(
//                                text = nhietDoHienTai,
//                                color = Color(0xFF424242),
//                                fontSize = if (isTablet) 40.sp else 30.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Icon(
//                                imageVector = iconThoiTiet,
//                                contentDescription = null,
//                                tint = Color(0xFF424242),
//                                modifier = Modifier.size(if (isTablet) 32.dp else 28.dp)
//                            )
//                        }
//                    }
//
//                    HorizontalDivider(
//                        modifier = Modifier.padding(vertical = 16.dp),
//                        thickness = 1.dp,
//                        color = Color(0xFFBDBDBD)
//                    )
//                    LazyVerticalGrid(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .heightIn(max = 100.dp),
//                        columns = GridCells.Fixed(3),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        item {
//                            WeatherWidget(
//                                icon = Icons.Filled.WaterDrop,
//                                value = "$doAm%",
//                                label = "Độ ẩm",
//                                isTablet = isTablet
//                            )
//                        }
//                        item {
//                            WeatherWidget(
//                                icon = Icons.Filled.Visibility,
//                                value = "$tamNhin km",
//                                label = "Tầm nhìn",
//                                isTablet = isTablet
//                            )
//                        }
//                        item {
//                            WeatherWidget(
//                                icon = Icons.Filled.Air,
//                                value = "$tocDoGio km/h",
//                                label = "Phong",
//                                isTablet = isTablet
//                            )
//                        }
//                    }
//                }
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
//    }
}
