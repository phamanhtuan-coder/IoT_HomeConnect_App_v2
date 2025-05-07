
package com.sns.homeconnect_v2.presentation.screen.iot_device.access_point_connection

import IoTHomeConnectAppTheme
import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.rememberResponsiveLayoutConfig
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.navigation.Screens

/** Giao diện màn hình Access Point Connection Screen (AccessPointConnectionScreen
 * -----------------------------------------
 * Người viết: Nguyễn Thanh Sang
 * Ngày viết: 07/12/2024
 * Lần cập nhật cuối: 06/1/2025
 * -----------------------------------------
 * @param navController Đối tượng điều khiển điều hướng.
 * @return Scaffold chứa toàn bộ nội dung
 * -----------------------------------------
 * Sủa lại phần giao diện
 *
 * ---------------------------
 * Lần cập nhật 30/12/2024
 * Người sửa: Phạm Anh Tuấn
 * ---------------------------
 * Access Point:
 *  + Hiện
 *  + Kết nối
 *  + Lấy dữ liệu AP đã kết nối
 *  + Các kiểm tra
 * ---------------------------
 * Lần cập nhật 8/1/2024
 * Người sửa: Nguyên Thanh Sang
 * ---------------------------
 */

@Composable
fun AccessPointConnectionScreen(
    navController: NavHostController,
    deviceId: String = "",
    deviceName: String = "",
) {
    val context = LocalContext.current
    val layoutConfig = rememberResponsiveLayoutConfig() // Lấy LayoutConfig
    var showDialog by remember { mutableStateOf(false) }
    var connectionStatus by remember { mutableStateOf<String?>(null) }
    var wifiList by remember { mutableStateOf(listOf<ScanResult>()) }
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiManager.connectionInfo

    //AccessPoint đang kết nối
    val currentSsid = wifiInfo.ssid

    // Hàm kiểm tra và yêu cầu quyền
    fun checkAndRequestPermissions(context: Context): Boolean {
        val requiredPermissions =
            listOf(Manifest.permission.ACCESS_FINE_LOCATION)

        val missingPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                missingPermissions.toTypedArray(),
                1001
            )
            return false
        }

        return true
    }

    // Function to scan Wi-Fi networks
    fun scanWifiNetworks(context: Context, onResult: (List<ScanResult>) -> Unit) {
        if (!checkAndRequestPermissions(context)) {
            println("Quyền chưa được cấp, không thể quét Wi-Fi.")
            return
        }

        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }

        wifiManager.startScan()
        val scanResults = wifiManager.scanResults.filter { it.SSID.isNotEmpty() }.distinctBy { it.SSID }
        onResult(scanResults)
    }

    var isTablet = isTablet(LocalContext.current)
    IoTHomeConnectAppTheme {
        LaunchedEffect(1) {
            scanWifiNetworks(context) { scanResults ->
                wifiList = scanResults
            }
        }
        val colorScheme = MaterialTheme.colorScheme
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = colorScheme.background,
            topBar = {
                Header(navController, "Back", "Kết nối với thiết bị")
                     },
            bottomBar = {
                MenuBottom(navController)
                        },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize() // Chiếm toàn bộ kích thước của màn hình
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Top, // Sắp xếp các item theo chiều dọc, bắt đầu từ trên xuống.
                    horizontalAlignment = Alignment.CenterHorizontally // Căn chỉnh các item theo chiều ngang vào giữa.
                ) {
                    // Tiêu đề
                    item {
                        // Box lớn chứa phần tiêu đề và các thành phần bên trong
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight() // Chiều cao vừa đủ với nội dung bên trong
                                .background(color = colorScheme.background),
                        ) {
                            // Cột chứa các phần tử con
                            Column {
                                // Hộp màu xanh dương bo tròn góc dưới bên trái
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth() // Chiếm toàn bộ chiều rộng
                                        .wrapContentHeight() // Chiều cao vừa đủ với nội dung
                                        .background(
                                            color = colorScheme.primary,
                                            shape = RoundedCornerShape(bottomStart = 40.dp)
                                        )
                                ) {
                                    // Cột chứa văn bản tiêu đề và các TextField
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth() // Chiếm toàn bộ chiều rộng
                                            .padding(
                                                horizontal = layoutConfig.outerPadding, // Padding ngang linh hoạt
                                                vertical = layoutConfig.textFieldSpacing // Padding dọc linh hoạt
                                            ),
                                        horizontalAlignment = Alignment.CenterHorizontally // Căn giữa các phần tử con theo chiều ngang
                                    ) {
                                        Text(
                                            "Kết nối",
                                            fontSize = layoutConfig.headingFontSize, // Font size linh hoạt
                                            color = colorScheme.onPrimary
                                        )
                                        // Văn bản tiêu đề "ĐIỂM TRUY CẬP"
                                        Text(
                                            "điểm truy cập",
                                            fontSize = layoutConfig.headingFontSize, // Font size linh hoạt
                                            color = colorScheme.onPrimary
                                        )

                                        // Khoảng cách giữa tiêu đề và TextField
                                        Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))

                                        // Ô nhập liệu đầu tiên - ID thiết bị
                                        // TextField để nhập ID thiết bị
                                        OutlinedTextField(
                                            value = "ID thiết bị của bạn là: $deviceId",
                                            onValueChange = {},
                                            readOnly = true,
                                            shape = RoundedCornerShape(25),
                                            singleLine = true,
                                            modifier = Modifier
                                                .width(if (isTablet) 400.dp else 300.dp)
                                                .height(if (isTablet) 80.dp else 70.dp),
                                            colors = TextFieldDefaults.colors(
                                                focusedTextColor = colorScheme.onBackground,
                                                unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),
                                                focusedContainerColor = colorScheme.onPrimary,
                                                unfocusedContainerColor = colorScheme.onPrimary,
                                                focusedIndicatorColor = colorScheme.primary,
                                                unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                                            ),
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Text,
                                                imeAction = ImeAction.Next
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))

                                        // TextField nhập tên thiết bị
                                        OutlinedTextField(
                                            value = "Tên thiết bị của bạn là: $deviceName",
                                            onValueChange = {},
                                            shape = RoundedCornerShape(25),
                                            singleLine = true,
                                            readOnly = true,
                                            modifier = Modifier
                                                .width(if (isTablet) 400.dp else 300.dp)
                                                .height(if (isTablet) 80.dp else 70.dp),
                                            colors = TextFieldDefaults.colors(
                                                focusedTextColor = colorScheme.onBackground,
                                                unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),
                                                focusedContainerColor = colorScheme.onPrimary,
                                                unfocusedContainerColor = colorScheme.onPrimary,
                                                focusedIndicatorColor = colorScheme.primary,
                                                unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                                            ),
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Text,
                                                imeAction = ImeAction.Next
                                            )
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(layoutConfig.cornerBoxSize * 0.9f) // Chiều cao linh hoạt theo LayoutConfig
                                ) {
                                    // Box màu vàng nhỏ nằm trên góc phải
                                    Box(
                                        modifier = Modifier.size(layoutConfig.cornerBoxSize)
                                            .align(Alignment.TopEnd)
                                            .background(color = colorScheme.primary)
                                            .zIndex(1f)  // Z-index thấp hơn
                                    )

                                    // Box màu xám bo tròn góc lõm trên cùng bên phải
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = colorScheme.background,
                                                shape = RoundedCornerShape(topEndPercent = layoutConfig.cornerBoxRadius)
                                            )
                                            .height(layoutConfig.cornerBoxSize) // Chiều cao linh hoạt
                                            .zIndex(2f), // Z-index cao hơn
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(horizontal = layoutConfig.outerPadding)
                                                .width(layoutConfig.contentWidth), // Độ rộng linh hoạt theo LayoutConfig
                                            horizontalArrangement = Arrangement.SpaceBetween,     // Các thành phần được bố trí cách xa nhau
                                            verticalAlignment = Alignment.CenterVertically        // Căn giữa theo chiều dọc
                                        ) {
                                            // Nút Icon thông tin
                                            IconButton(
                                                onClick = { showDialog = true },
                                                modifier = Modifier.size(layoutConfig.iconSize),
                                                colors = IconButtonDefaults.iconButtonColors(colorScheme.primary)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Info,
                                                    contentDescription = "Info Icon",
                                                    tint = colorScheme.onPrimary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Công tắc Wi-Fi
                    item {
                        // Cột chứa nội dung công tắc Wi-Fi
                        Column(
                            modifier = Modifier
                                .padding(horizontal = layoutConfig.outerPadding) // Padding ngang linh hoạt theo LayoutConfig
                                .width(layoutConfig.contentWidth),               // Độ rộng linh hoạt theo LayoutConfig
                            horizontalAlignment = Alignment.CenterHorizontally   // Căn giữa theo chiều ngang
                        ) {
                            // Hàng ngang chứa nhãn và công tắc Wi-Fi
                            Row(
                                modifier = Modifier.width(layoutConfig.contentWidth),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Wi-Fi:", fontSize = layoutConfig.textFontSize)
                                WifiToggle(wifiManager, context)
                            }

                            if(wifiManager.isWifiEnabled) {
                                WiFiCard(
                                    navController,
                                    wifiName = currentSsid,
                                    isConnected = true,
                                )
                            }
                        }
                    }

                    // Danh sách các mạng Wi-Fi khả dụng
                    item {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = layoutConfig.outerPadding) // Padding ngang linh hoạt theo LayoutConfig
                                .width(layoutConfig.contentWidth),               // Độ rộng linh hoạt theo LayoutConfig
                            horizontalAlignment = Alignment.CenterHorizontally   // Căn giữa theo chiều ngang
                        ) {
                            Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))
                            Row(
                                modifier = Modifier.width(layoutConfig.contentWidth), // Độ rộng linh hoạt theo LayoutConfig
                                horizontalArrangement = Arrangement.SpaceBetween,     // Các thành phần được bố trí cách xa nhau
                                verticalAlignment = Alignment.CenterVertically        // Căn giữa theo chiều dọc
                            ) {
                                Text("Available networks", fontSize = layoutConfig.textFontSize)
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .clickable(
                                            onClick = {
                                                scanWifiNetworks(context) { scanResults ->
                                                    wifiList = scanResults // Cập nhật danh sách Wi-Fi tìm thấy vào trạng thái
                                                }
                                            }
                                        )
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .padding(horizontal = layoutConfig.outerPadding, vertical = 8.dp)
                                .width(layoutConfig.contentWidth)
                        ) {
                            for (wifiItem in wifiList) {
                                WiFiCard(
                                    navController,
                                    wifiName = wifiItem.SSID,
                                    isConnected = false,
                                )
                            }
                        }
                    }
                }
                // Đặt AlertDialog ngoài LazyColumn để hiển thị hộp thoại khi `showDialog` == true
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        }, // Đóng hộp thoại khi nhấn ra ngoài hoặc nút đóng
                        confirmButton = {
                            // Nút xác nhận (Đóng)
                            TextButton(onClick = { showDialog = false }) {
                                Text("Đóng", color = colorScheme.primary)
                            }
                        },
                        shape = RoundedCornerShape(12.dp), // Bo tròn các góc của hộp thoại
                        text = {
                            // Nội dung của hộp thoại
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = layoutConfig.dialogPadding,
                                        vertical = layoutConfig.textFieldSpacing
                                    )
                            ) {
                                item {
                                    // Đoạn văn bản mô tả hướng dẫn truy cập
                                    Text(
                                        "Bạn hãy chọn điểm truy cập (Access Point) của thiết bị bạn muốn kết nối.\n" +
                                                "Tên của điểm truy cập sẽ có cú pháp: AP-{ID_thiết_bị}.",
                                        fontSize = layoutConfig.textFontSize,
                                        lineHeight = layoutConfig.textFontSize * 1.2,
                                        color = colorScheme.onSecondary,
                                    )
                                    Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))

                                    // Đoạn văn bản hướng hướng dẫn xem tên và ID thiết bị
                                    Text(
                                        "Tên và Id thiết bị sẽ được hiển thị ở bên dưới",
                                        lineHeight = layoutConfig.textFontSize * 1.2,
                                        fontSize = layoutConfig.textFontSize,
                                        color = colorScheme.onSecondary,
                                    )
                                    Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))

                                    // Văn bản ví dụ tiêu đề
                                    Text(
                                        "Ví dụ: ",
                                        fontSize = layoutConfig.textFontSize,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = layoutConfig.textFontSize * 1.2,
                                        color =  colorScheme.onSecondary,
                                    )
                                    Text(
                                        "AP-SLB_001",
                                        fontSize = layoutConfig.textFontSize,
                                        lineHeight = layoutConfig.textFontSize * 1.2,
                                        color =  colorScheme.onSecondary,
                                    )
                                }
                            }
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun WiFiCard(
    navController: NavHostController,
    wifiName: String, // Tên mạng Wi-Fi hiển thị
    isConnected: Boolean, // Trạng thái kết nối (true nếu đã kết nối, false nếu chưa)
) {
    val context = LocalContext.current
    // Trạng thái điều khiển hiển thị dialog
    var showDialog by remember { mutableStateOf(false) }
    // Card chứa thông tin Wi-Fi
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .border(1.dp, colorScheme.onSecondary, RoundedCornerShape(8.dp))
                .clickable(
                    onClick = {
                        if (isConnected) {
                            navController.navigate(Screens.WifiConnection.route)
                        } else {
                            showDialog = true // Hiển thị dialog khi nhấn vào WiFiCard chưa kết nối
                        }
                    }
                ),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.secondary),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            // Nội dung của Card là Row chia thành 2 phần: thông tin mạng và biểu tượng
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically, // Canh giữa theo chiều dọc
                horizontalArrangement = Arrangement.SpaceBetween // Canh các phần tử cách nhau đều
            ) {
                // Phần biểu tượng và thông tin Wi-Fi bên trái
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon( // Biểu tượng Wi-Fi
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "Wi-Fi Icon",
                        tint = colorScheme.onSecondary,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        // Hiển thị tên mạng Wi-Fi
                        Text(
                            text = wifiName,
                            fontSize = 16.sp,
                            maxLines = 1, // Giới hạn 1 dòng
                            overflow = TextOverflow.Ellipsis, // Thêm "..." nếu quá dài
                            color = colorScheme.onSecondary,
                            modifier = Modifier.widthIn(max = 200.dp) // Giới hạn chiều rộng
                        )
                        if (isConnected) {
                            Text(
                                text = "Đã kết nối",
                                fontSize = 12.sp,
                                color = colorScheme.onSecondary,
                            )
                        }
                    }

                }

                // Biểu tượng bên phải
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!isConnected) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked Icon",
                            tint = colorScheme.onSecondary,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Icon",
                        tint = colorScheme.onSecondary,
                    )
                }
            }
        }
    }

    // Hiện dialog để nhập password
    InputPasswordDialog(
        ssid = wifiName,
        navController = navController,
        context = context,
        isVisible = showDialog,
        onDismiss = {
            showDialog = false
        }
    )
}

@Preview(showBackground = true,showSystemUi = true)
@Composable
fun AccessPointConnectionScreenPreview() {
    AccessPointConnectionScreen(navController = rememberNavController())
}

@Composable
fun InputPasswordDialog(
    ssid: String, // Tên Wi-Fi ban đầu được truyền vào
    navController: NavHostController,
    context: Context, // Ngữ cảnh để thực hiện các thao tác hệ thống
    isVisible: Boolean, // Trạng thái hiển thị của dialog
    onDismiss: () -> Unit // Hành động khi dialog bị đóng
) {
    // Hiển thị dialog chỉ khi isVisible == true
    if (isVisible) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.medium // Đặt hình dạng bo góc cho dialog
            ) {
                InputPasswordForm(ssid, navController, context, onDismiss) // Hiển thị nội dung chính của dialog
            }
        }
    }
}

@Composable
fun InputPasswordForm(
    ssid: String, // Tên Wi-Fi
    navController: NavHostController,
    context: Context, // Ngữ cảnh
    onDismiss: () -> Unit // Hành động khi người dùng đóng dialog
) {
    // Hàm kiểm tra và yêu cầu quyền
    fun checkAndRequestPermissions(context: Context): Boolean {
        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            listOf(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
            )
        }

        val missingPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                missingPermissions.toTypedArray(),
                1001
            )
            return false
        }

        return true
    }

    fun connectToWifi(
        context: Context,
        ssid: String,
        password: String,
        onConnectionResult: (Boolean) -> Unit
    ) {
        if (!checkAndRequestPermissions(context)) {
            onConnectionResult(false)
            return
        }

        connectToESPAndroid10Plus(context, ssid, password, onConnectionResult)
    }

    var isTablet = isTablet(LocalContext.current)

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        val layoutConfig = rememberResponsiveLayoutConfig() // Lấy LayoutConfig
        // Trạng thái cho tên Wi-Fi, mật khẩu và thông báo kết nối
        var ssidInput by remember { mutableStateOf(ssid) }
        var password by remember { mutableStateOf("") }
        var connectionStatus by remember { mutableStateOf<String?>(null) }
        val passwordVisible = remember { mutableStateOf(false) }

        // Giao diện chính của form
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiêu đề
            Text(
                "Nhập thông tin Wi-Fi",
                fontSize = layoutConfig.headingFontSize, // Font size linh hoạt
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Trường nhập tên Wi-Fi
            OutlinedTextField(
                value = ssidInput,
                onValueChange = { ssidInput = it },
                placeholder = { Text("Tên Wi-Fi") },
                leadingIcon = { Icon(Icons.Filled.Wifi, contentDescription = null) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(if (isTablet) 500.dp else 400.dp)
                    .height(if (isTablet) 80.dp else 70.dp),
                shape = RoundedCornerShape(25),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorScheme.onBackground,  // Màu text khi TextField được focus
                    unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),  // Màu text khi TextField không được focus
                    focusedContainerColor = colorScheme.onPrimary,
                    unfocusedContainerColor = colorScheme.onPrimary,
                    focusedIndicatorColor = colorScheme.primary,
                    unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                )
            )

            // Trường nhập mật khẩu
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(if (isTablet) 500.dp else 400.dp)
                    .height(if (isTablet) 80.dp else 70.dp),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(
                            imageVector = if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible.value) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(25),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorScheme.onBackground,  // Màu text khi TextField được focus
                    unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),  // Màu text khi TextField không được focus
                    focusedContainerColor = colorScheme.onPrimary,
                    unfocusedContainerColor = colorScheme.onPrimary,
                    focusedIndicatorColor = colorScheme.primary,
                    unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                )
            )

            // Nút kết nối
            Button(
                onClick = {
                    //ToDo: Sử lý sự kiện
                    //Kiểm tra ssidInput và password có giá trị hay không
                    if (ssidInput.isNotEmpty() && password.isNotEmpty()) {
                        connectToWifi(context, ssidInput, password) { success ->
                            Handler(Looper.getMainLooper()).post {
                                connectionStatus = if (success) {
                                    // Kết nối thành công

                                    navController.navigate(Screens.WifiConnection.route)
                                    "Kết nối thành công tới $ssidInput"
                                } else {
                                    // Kết nối thất bại
                                    "Không thể kết nối tới $ssidInput"
                                }
                            }
                        }
                    } else {
                        connectionStatus = "Vui lòng nhập đầy đủ thông tin."
                    }

                },
                modifier = Modifier
                    .width(if (isTablet) 300.dp else 200.dp)
                    .height(if (isTablet) 56.dp else 48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    "Kết nối",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onPrimary
                )
            }

            // Hiển thị thông báo trạng thái kết nối (nếu có)
            connectionStatus?.let {
                Text(it, modifier = Modifier.padding(top = 8.dp))
            }

            // Nút đóng dialog
            TextButton(onClick = { onDismiss() }) {
                Text(
                    "Đóng",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInputPasswordForm() {
    InputPasswordForm(
        ssid = "WiFi_Nha_Minh", // Tên Wi-Fi mẫu
        navController = rememberNavController(),
        context = LocalContext.current, // Lấy context từ LocalContext
        onDismiss = {} // Hành động mẫu khi đóng
    )
}


fun connectToESPAndroid10Plus(
    context: Context,
    ssid: String,
    password: String,
    onConnectionResult: (Boolean) -> Unit
) {
    try {
        val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder()
            .setSsid(ssid)
            .setWpa2Passphrase(password)
            .build()

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .setNetworkSpecifier(wifiNetworkSpecifier)
            .build()

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                println("Connected to Wi-Fi: $ssid")
                bindToNetwork(context, network) // Bind network to process
                onConnectionResult(true)
            }

            override fun onUnavailable() {
                println("Failed to connect to Wi-Fi: $ssid")
                onConnectionResult(false)
            }
        })
    } catch (e: Exception) {
        e.printStackTrace()
        onConnectionResult(false)
    }
}


fun connectToESPBelowAndroid10(
    context: Context,
    ssid: String,
    password: String,
    onConnectionResult: (Boolean) -> Unit
) {
    try {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val wifiConfig = WifiConfiguration().apply {
            SSID = "\"$ssid\""
            preSharedKey = "\"$password\""
        }

        val networkId = wifiManager.addNetwork(wifiConfig)
        if (networkId != -1) {
            wifiManager.disconnect()
            wifiManager.enableNetwork(networkId, true)
            wifiManager.reconnect()
            println("Connected to Wi-Fi: $ssid")
            onConnectionResult(true)
        } else {
            println("Failed to connect to Wi-Fi: $ssid")
            onConnectionResult(false)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        onConnectionResult(false)
    }
}

fun bindToNetwork(context: Context, network: Network) {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityManager.bindProcessToNetwork(network)
    println("Network bound to process.")
}

@Composable
fun WifiToggle(
    wifiManager: WifiManager,
    context: Context
) {
    var isWifiEnabled by remember { mutableStateOf(wifiManager.isWifiEnabled) }

    // For Android 10 (Q) and above, we need to use the system settings
    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // Update the UI state after returning from settings
        isWifiEnabled = wifiManager.isWifiEnabled
    }

    Switch(
        checked = isWifiEnabled,
        onCheckedChange = { enabled ->
            // For Android 10 and above, direct the user to system settings
            val intent = Intent(Settings.Panel.ACTION_WIFI)
            settingsLauncher.launch(intent)
            Toast.makeText(
                context,
                "Please toggle Wi-Fi in system settings",
                Toast.LENGTH_SHORT
            ).show()
        }
    )

    LaunchedEffect(Unit) {
        val filter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        context.registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    isWifiEnabled = wifiManager.isWifiEnabled
                }
            },
            filter
        )
    }
}