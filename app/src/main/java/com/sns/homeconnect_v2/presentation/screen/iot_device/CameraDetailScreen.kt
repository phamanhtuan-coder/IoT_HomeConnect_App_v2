package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.sns.homeconnect_v2.presentation.component.MiniActionButton
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceDisplayInfoState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.DeviceDisplayViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

/**
 * Composable function for the Camera Detail Screen.
 *
 * This screen displays detailed information and controls for a specific camera device.
 * It includes a live camera preview, options to toggle audio and microphone,
 * action buttons for recording, taking photos, viewing playback, and accessing alert history.
 * It also provides access to device information and Wi-Fi settings.
 *
 * @param navController The navigation controller for handling screen transitions.
 * @param deviceId The unique identifier of the camera device.
 * @param deviceName The name of the camera device.
 * @param snackbarViewModel The ViewModel for displaying snackbar messages.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraDetailScreen(
    navController: NavHostController,
    deviceId: String,
    deviceName: String,
    deviceTypeName: String,
    serialNumber: String,
    controls: Map<String, String>,
    isViewOnly: Boolean = true,
    snackbarViewModel: SnackbarViewModel,
) {
    // State to control the visibility of the bottom sheet
    val showBottomSheet = remember { mutableStateOf(false) }

    // Assume deviceId and deviceName are passed as arguments or retrieved from a ViewModel
    val displayViewModel: DeviceDisplayViewModel = hiltViewModel()
    val displayState by displayViewModel.displayState.collectAsState()

    // Trigger loading of device display info
    var audioEnabled by remember { mutableStateOf(false) }
    var micEnabled by remember { mutableStateOf(false) }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Chi tiết thiết bị"
                )
            },
            bottomBar = {
                MenuBottom(navController)
            },
            containerColor = colorScheme.background,
            modifier = Modifier.fillMaxSize(),
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    item {
                        ColoredCornerBox(
                            cornerRadius = 40.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(32.dp))
                                    .background(Color.Black)
                            ) {
                                AsyncImage(
                                    model = "https://images.unsplash.com/photo-1560448205-4d9b3e6bb6db?auto=format&fit=crop&w=1470&q=80",
                                    contentDescription = "Camera preview",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                Row(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(16.dp)
                                        .background(
                                            Color.Black.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Live",
                                        color = Color.Red,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .background(
                                                Color.Red,
                                                shape = CircleShape
                                            ) // 👈 Dùng CircleShape thay RoundedCornerShape
                                    )
                                }
                            }
                        }
                    }

                    item {
                        InvertedCornerHeader(
                            backgroundColor = colorScheme.surface,
                            overlayColor = colorScheme.primary
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp, vertical = 8.dp
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        showBottomSheet.value = true
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

                                IconButton(
                                    onClick = {
                                        navController.navigate(
                                            Screens.AccessPoint.createRoute(deviceId, deviceName)
                                        )
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

                    item {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Âm thanh", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                IconToggleButton(
                                    checked = audioEnabled,
                                    onCheckedChange = { audioEnabled = it },
                                    enabled = !isViewOnly
                                ) {
                                    Icon(
                                        imageVector = if (audioEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                                        contentDescription = null,
                                        tint = if (audioEnabled) Color(0xFF2563EB) else Color.Gray
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Mic", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                IconToggleButton(
                                    checked = micEnabled,
                                    onCheckedChange = { micEnabled = it },
                                    enabled = !isViewOnly
                                ) {
                                    Icon(
                                        imageVector = if (micEnabled) Icons.Default.Mic else Icons.Default.MicOff,
                                        contentDescription = null,
                                        tint = if (micEnabled) Color(0xFF2563EB) else Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                MiniActionButton(
                                    icon = Icons.Default.Videocam,
                                    label = "Ghi hình",
                                    enabled = !isViewOnly
                                ) {
                                    // TODO: Ghi hình
                                }

                                MiniActionButton(
                                    icon = Icons.Default.PhotoCamera,
                                    label = "Chụp ảnh",
                                    enabled = !isViewOnly
                                ) {
                                    // TODO: Chụp ảnh
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                MiniActionButton(
                                    icon = Icons.Default.Movie,
                                    label = "Xem lại",
                                    enabled = !isViewOnly
                                ) {
                                    // TODO: Xem lại
                                }

                                MiniActionButton(
                                    icon = Icons.Default.Notifications,
                                    label = "Lịch sử cảnh báo",
                                    enabled = !isViewOnly
                                ) {
                                    // TODO: Hiển thị lịch sử cảnh báo
                                }
                            }

                            MiniActionButton(
                                icon = Icons.Default.Settings,
                                label = "Cài đặt",
                                enabled = !isViewOnly,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(0.9f)
                            ) {
                                // TODO: Điều hướng đến cài đặt
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))

                        if (!isViewOnly) {
                            ActionButtonWithFeedback(
                                label = "Gọi hỗ trợ",
                                snackbarViewModel = snackbarViewModel,
                                onAction = { onSuccess, onError ->
                                    onSuccess("Đã gọi hỗ trợ thành công!")
                                }
                            )
                        }
                    }
                }
            }
        )

        // Bottom sheet to display device information
        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet.value = false }
            ) {
                when (displayState) {
                    is DeviceDisplayInfoState.Success -> {
                        val product = (displayState as DeviceDisplayInfoState.Success).product
                        val category = (displayState as DeviceDisplayInfoState.Success).category
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Tên sản phẩm: ${product.name ?: "Không rõ"}")
                            Text("Danh mục: ${category.name}")
                            Text("Giá: ${product.selling_price ?: "Không rõ"}")
                            Text("Mô tả: ${product.description_normal ?: "Không có mô tả"}")
                        }
                    }

                    is DeviceDisplayInfoState.Loading -> {
                        Text("Đang tải thông tin sản phẩm...")
                    }

                    is DeviceDisplayInfoState.Error -> {
                        Text("Lỗi: ${(displayState as DeviceDisplayInfoState.Error).error}")
                    }

                    else -> {}
                }
            }
        }
    }
}


