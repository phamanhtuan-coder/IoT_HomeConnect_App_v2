package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sns.homeconnect_v2.presentation.component.CustomSwitch
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.door.DoorActionButton
import com.sns.homeconnect_v2.presentation.component.widget.door.DoorCanvas
import com.sns.homeconnect_v2.presentation.component.widget.door.DoorType
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

@Composable
fun DoorDetailScreen(
    deviceId: String,
    deviceName: String,
    serialNumber: String,
    controls: Map<String, String>,
    isViewOnly: Boolean = true,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
) {
    /* ---------- STATE ---------- */
    var doorType by remember { mutableStateOf(DoorType.SLIDING) }
    var isOpen   by remember { mutableStateOf(false) }

    var powerStatusUI by remember { mutableStateOf(false) }
    var isPowerUpdating by remember { mutableStateOf(false) }

    var pendingPowerStatus by remember { mutableStateOf<Boolean?>(null) }

    val colorScheme = MaterialTheme.colorScheme

    IoTHomeConnectAppTheme {
        Scaffold(
            topBar = {
            },
            bottomBar = {
            },
            containerColor = colorScheme.background,
            modifier = Modifier.fillMaxSize(),
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    /** ---- VÙNG CANVAS ---- */
                    item {
                        ColoredCornerBox(
                            cornerRadius = 40.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                // BÊN TRÁI: Thông tin thiết bị
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = deviceName,
                                        color = colorScheme.onPrimary,
                                        fontSize = 20.sp,
                                        lineHeight = 28.sp,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 0.5.sp
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Bật/Tắt",
                                            color = colorScheme.onPrimary,
                                            fontSize = 14.sp
                                        )
                                        CustomSwitch(
                                            isCheck = powerStatusUI,
                                            enabled = !isPowerUpdating,
                                            onCheckedChange = { newStatus ->
                                                pendingPowerStatus = newStatus
                                                isPowerUpdating = true
                                            }
                                        )
                                    }

                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "Trạng thái cửa:",
                                            color = colorScheme.onPrimary,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal
                                        )

                                        Text(
                                            text = if (isOpen) "Đang mở" else "Đang đóng",
                                            color = if (isOpen) Color(0xFFFF0000) else Color(0xFF00FF19),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            letterSpacing = 0.3.sp
                                        )
                                    }
                                }

                                Spacer(Modifier.width(16.dp))

                                // BÊN PHẢI: Cửa đẹp hơn
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    DoorCanvas(
                                        doorType = doorType,
                                        isOpen = isOpen,
                                        onToggle = { if (!isViewOnly) isOpen = !isOpen },
                                        modifier = Modifier
                                            .fillMaxSize(1f)
                                    )
                                }
                            }
                        }
                    }

                    /** ---- DẢI HEADER VỚI SELECTOR ---- */
                    item {
                        InvertedCornerHeader(
                            backgroundColor = colorScheme.surface,
                            overlayColor = colorScheme.primary
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                /* ---------- INFO ---------- */
                                IconButton(
                                    onClick = {
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        tint = colorScheme.primary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                /* ---------- WIFI ---------- */
                                IconButton(
                                    onClick = {
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Wifi,
                                        contentDescription = "Wi-Fi",
                                        tint = colorScheme.primary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }

                    /** ---- BUTTON MỞ/ĐÓNG ---- */
                    item {
                        Spacer(Modifier.height(12.dp))
                        DoorActionButton(
                            isOpen = isOpen,
                            onClick = { isOpen = !isOpen }
                        )
                    }
                }
            }
        )
    }
}

/* ---------------- PREVIEW ---------------- */
@Preview(showBackground = true)
@Composable
private fun DoorDetailScreenPreview() {
    DoorDetailScreen(
        deviceId = "DEVICE001",
        deviceName = "Cửa chính",
        serialNumber = "SERIAL123456",
        controls = mapOf("power_status" to "toggle"),
        isViewOnly = true,
        snackbarViewModel = remember { SnackbarViewModel() }
    )
}
