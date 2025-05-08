package com.sns.homeconnect_v2.presentation.screen.iot_device.access_point_connection

import IoTHomeConnectAppTheme
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.PermissionEventHandler
import com.sns.homeconnect_v2.core.util.rememberResponsiveLayoutConfig
import com.sns.homeconnect_v2.presentation.component.WiFiCard
import com.sns.homeconnect_v2.presentation.component.WifiToggle
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.access_point_connection.AccessPointViewModel
import kotlinx.coroutines.launch



@Composable
fun AccessPointConnectionScreen(
    navController: NavHostController,
    deviceId: String = "",
    deviceName: String = "",
    viewModel: AccessPointViewModel = hiltViewModel(),
    permissionEventHandler: PermissionEventHandler = hiltViewModel()
) {
    val context = LocalContext.current
    val layoutConfig = rememberResponsiveLayoutConfig()
    var showDialog by remember { mutableStateOf(false) }
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val currentSsid = wifiManager.connectionInfo?.ssid?.takeIf { it != "<unknown ssid>" } ?: ""
    val isTablet = isTablet(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()

    // Collect permission results from SharedFlow
    LaunchedEffect(Unit) {
        permissionEventHandler.permissionResultFlow.collect { (resultContext, grantResults) ->
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED } && wifiManager.isWifiEnabled) {
                coroutineScope.launch {
                    viewModel.scanWifiNetworks(resultContext)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!wifiManager.isWifiEnabled) {
            Toast.makeText(context, "Please enable Wi-Fi to scan for networks", Toast.LENGTH_LONG).show()
            context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } else if (viewModel.checkPermissions(context)) {
            coroutineScope.launch {
                viewModel.scanWifiNetworks(context)
            }
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                viewModel.permissionManager.getLocationWifiPermissions(),
                1001
            )
        }
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = colorScheme.background,
            topBar = { Header(navController, "Back", "Kết nối với thiết bị") },
            bottomBar = { MenuBottom(navController) },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(colorScheme.background)
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .background(
                                            color = colorScheme.primary,
                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(bottomStart = 40.dp)
                                        )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                horizontal = layoutConfig.outerPadding,
                                                vertical = layoutConfig.textFieldSpacing
                                            ),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            "Kết nối",
                                            fontSize = layoutConfig.headingFontSize,
                                            color = colorScheme.onPrimary
                                        )
                                        Text(
                                            "điểm truy cập",
                                            fontSize = layoutConfig.headingFontSize,
                                            color =colorScheme.onPrimary
                                        )
                                        Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))
                                        OutlinedTextField(
                                            value = "ID thiết bị của bạn là: $deviceId",
                                            onValueChange = {},
                                            readOnly = true,
                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(25),
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
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))
                                        OutlinedTextField(
                                            value = "Tên thiết bị của bạn là: $deviceName",
                                            onValueChange = {},
                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(25),
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
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = layoutConfig.outerPadding)
                                .width(layoutConfig.contentWidth),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.width(layoutConfig.contentWidth),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Wi-Fi:", fontSize = layoutConfig.textFontSize)
                                WifiToggle(wifiManager, context)
                            }
                            if (wifiManager.isWifiEnabled && currentSsid.isNotEmpty()) {
                                WiFiCard(
                                    navController = navController,
                                    wifiName = currentSsid,
                                    isConnected = true
                                )
                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = layoutConfig.outerPadding)
                                .width(layoutConfig.contentWidth),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))
                            Row(
                                modifier = Modifier.width(layoutConfig.contentWidth),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Available networks", fontSize = layoutConfig.textFontSize)
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "",
                                    modifier = Modifier.clickable {
                                        if (!wifiManager.isWifiEnabled) {
                                            Toast.makeText(context, "Please enable Wi-Fi to scan for networks", Toast.LENGTH_LONG).show()
                                            context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS).apply {
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            })
                                        } else if (viewModel.checkPermissions(context)) {
                                            coroutineScope.launch {
                                                viewModel.scanWifiNetworks(context)
                                            }
                                        } else {
                                            ActivityCompat.requestPermissions(
                                                context as Activity,
                                                viewModel.permissionManager.getLocationWifiPermissions(),
                                                1001
                                            )
                                        }
                                    }
                                )
                            }
                            if (viewModel.isLoading) {
                                CircularProgressIndicator()
                            } else {
                                viewModel.wifiList.forEach { wifiItem ->
                                    WiFiCard(
                                        navController = navController,
                                        wifiName = wifiItem.SSID.takeIf { it?.isNotEmpty() == true } ?: "",
                                        isConnected = false
                                    )
                                }
                            }
                        }
                    }
                }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        confirmButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Đóng", color = colorScheme.primary)
                            }
                        },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        text = {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = layoutConfig.dialogPadding,
                                        vertical = layoutConfig.textFieldSpacing
                                    )
                            ) {
                                item {
                                    Text(
                                        "Bạn hãy chọn điểm truy cập (Access Point) của thiết bị bạn muốn kết nối.\n" +
                                                "Tên của điểm truy cập sẽ có cú pháp: AP-{ID_thiết_bị}.",
                                        fontSize = layoutConfig.textFontSize,
                                        lineHeight = layoutConfig.textFontSize * 1.2,
                                        color = colorScheme.onSecondary
                                    )
                                    Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))
                                    Text(
                                        "Tên và Id thiết bị sẽ được hiển thị ở bên dưới",
                                        lineHeight = layoutConfig.textFontSize * 1.2,
                                        fontSize = layoutConfig.textFontSize,
                                        color = colorScheme.onSecondary
                                    )
                                    Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))
                                    Text(
                                        "Ví dụ: ",
                                        fontSize = layoutConfig.textFontSize,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = layoutConfig.textFontSize * 1.2,
                                        color = colorScheme.onSecondary
                                    )
                                    Text(
                                        "AP-SLB_001",
                                        fontSize = layoutConfig.textFontSize,
                                        lineHeight = layoutConfig.textFontSize * 1.2,
                                        color = colorScheme.onSecondary
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