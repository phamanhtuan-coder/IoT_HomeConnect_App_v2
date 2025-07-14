package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sns.homeconnect_v2.presentation.component.CustomSwitch
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.door.DoorActionButton
import com.sns.homeconnect_v2.presentation.component.widget.door.DoorCanvas
import com.sns.homeconnect_v2.presentation.component.widget.door.DoorType
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_door.ApiResult
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.detail_door.DoorViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

@Composable
fun DoorDetailScreen(
    deviceId: String,
    deviceName: String,
    deviceTypeName: String,
    serialNumber: String,
    controls: Map<String, String>,
    isViewOnly: Boolean = true,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
) {
    val viewModel: DoorViewModel = hiltViewModel()
    val toggleState by viewModel.toggleState.collectAsState()
    val doorStatusState by viewModel.doorStatus.collectAsState()

    var doorType by remember(deviceTypeName) {
        mutableStateOf(DoorType.fromLabel(deviceTypeName) ?: DoorType.TRADITIONAL)
    }

    var isOpen by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchDoorStatus(serialNumber)
    }

    LaunchedEffect(pendingAction) {
        pendingAction?.let { desiredOpen ->
            isProcessing = true
            viewModel.toggleDoorPower(serialNumber, desiredOpen)
            pendingAction = null // Quan trọng để tránh gửi lại
        }
    }


    when (doorStatusState) {
        is ApiResult.Success -> {
            val status = (doorStatusState as ApiResult.Success).data
            doorType = DoorType.fromLabel(deviceTypeName) ?: DoorType.TRADITIONAL
            isOpen = status.door_state == "open"
        }
        is ApiResult.Error -> {
            snackbarViewModel.showSnackbar((doorStatusState as ApiResult.Error).message)
        }
        else -> {}
    }

    // Xử lý toggleState sau khi gọi API
    when (toggleState) {
        is ApiResult.Success -> {
            val result = (toggleState as ApiResult.Success).data
            isOpen = result.door.door_state == "open"
            isProcessing = false
        }
        is ApiResult.Error -> {
            snackbarViewModel.showSnackbar((toggleState as ApiResult.Error).message)
            isProcessing = false
        }
        else -> {}
    }

    IoTHomeConnectAppTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        ColoredCornerBox(cornerRadius = 40.dp) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = deviceName,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                    Text(
                                        text = "Trạng thái cửa:",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                    Text(
                                        text = if (isOpen) "Đang mở" else "Đang đóng",
                                        color = if (isOpen) Color.Green else Color.Red,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    DoorCanvas(
                                        doorType = doorType,
                                        isOpen = isOpen,
                                        onToggle = {
                                            if (!isViewOnly && !isProcessing) {
                                                pendingAction = !isOpen
                                            }
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                    item {
                        InvertedCornerHeader(
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            overlayColor = MaterialTheme.colorScheme.primary
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Wifi,
                                        contentDescription = "Wi-Fi",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        DoorActionButton(
                            isOpen = isOpen,
                            onClick = {
                                if (!isViewOnly && !isProcessing) {
                                    pendingAction = !isOpen
                                }
                            }
                        )
                    }
                }
            }
        )
    }
}