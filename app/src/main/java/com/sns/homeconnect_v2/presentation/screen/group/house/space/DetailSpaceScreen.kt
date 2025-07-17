package com.sns.homeconnect_v2.presentation.screen.group.house.space

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.presentation.component.BottomSheetWithTrigger
import com.sns.homeconnect_v2.presentation.component.DeviceCardSwipeable
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.LabeledBox
import com.sns.homeconnect_v2.presentation.component.widget.RadialFab
import com.sns.homeconnect_v2.presentation.component.widget.SearchBar
import com.sns.homeconnect_v2.presentation.model.FabChild
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.space.SpaceScreenDetailViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.space.UpdateSpaceViewModel
import com.sns.homeconnect_v2.presentation.navigation.Screens

/**
 * Hàm Composable đại diện cho màn hình chi tiết của một không gian/nhóm.
 *
 * Màn hình này hiển thị danh sách các thiết bị trong một nhóm cụ thể. Nó bao gồm một thanh tìm kiếm,
 * một tiêu đề hiển thị số lượng thiết bị và danh sách các thẻ thiết bị có thể vuốt.
 * Một nút hành động nổi (FAB) dạng tròn cung cấp các tùy chọn để chỉnh sửa, xóa hoặc chia sẻ nhóm.
 * Màn hình cũng có một thanh điều hướng ở cuối màn hình.
 *
 * @param modifier Modifier cho composable này. Mặc định là [Modifier].
 * @param navController [NavHostController] để điều hướng trong ứng dụng.
 * @author Sang
 * @since 16/05/2025
 */

object Roles {
    const val MEMBER  = "member"
    const val OWNER   = "owner"
    const val VICE    = "vice"
    const val ADMIN   = "admin"
}

@Composable
fun DetailSpaceScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    spaceId: Int,
    currentUserRole: String
) {
//    val deviceList = remember {
//        mutableStateListOf(
//            DeviceUi(1, "Gia đình", "bedroom", false, Icons.Default.Gr, Color.Blue),
//            DeviceUi(2, "Marketing", "living room", false, Icons.Default.Home, Color.Red),
//            DeviceUi(3, "Kỹ thuật", "kitchen", false, Icons.Default.Group, Color.Green)
//        )
//    }
    val isViewOnly = currentUserRole.equals(Roles.MEMBER, ignoreCase = true)
    val permissionType = if (isViewOnly) "VIEW" else "CONTROL"
    val spaceViewModel: SpaceScreenDetailViewModel = hiltViewModel()
    val updateSpaceViewModel: UpdateSpaceViewModel = hiltViewModel()
    val snackbarViewModel: SnackbarViewModel = hiltViewModel()

    val spaceDetail by updateSpaceViewModel.updatespace.collectAsState()

    // Trạng thái cho bottom sheet
    var isSheetVisible by remember { mutableStateOf(false) }
    var spaceNameInput by remember { mutableStateOf(spaceDetail?.space_name ?: "") }
    var iconNameInput by remember { mutableStateOf(spaceDetail?.icon_name ?: "") }
    var iconColorInput by remember { mutableStateOf(spaceDetail?.icon_color ?: "") }
    var descriptionInput by remember { mutableStateOf(spaceDetail?.space_description ?: "") }

    LaunchedEffect(spaceDetail) {
        // Cập nhật input khi spaceDetail thay đổi
        spaceNameInput = spaceDetail?.space_name ?: ""
        iconNameInput = spaceDetail?.icon_name ?: ""
        iconColorInput = spaceDetail?.icon_color ?: ""
        descriptionInput = spaceDetail?.space_description ?: ""
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        spaceViewModel.getSpaces(spaceId)
        updateSpaceViewModel.updateSpace(spaceId, spaceNameInput, iconNameInput, iconColorInput, descriptionInput)
    }
    Log.d("DetailSpaceScreen", "spaceId: $spaceId")

    val deviceList by spaceViewModel.spacesDevice.collectAsState()

    Log.d("DetailSpaceScreen", "deviceList: $deviceList")
    //val deviceList = remember { mutableStateListOf<DeviceUi>() }
    Log.d("DetailSpaceScreen", "spaceDetail: $spaceDetail")

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        val floatingActionButtons = listOf(
            FabChild(
                icon = Icons.Default.Edit,
                onClick = { isSheetVisible = true  },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ),
            FabChild(
                icon = Icons.Default.Delete,
                onClick = { /* TODO: xoá */ },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ),
            FabChild(
                icon = Icons.Default.Share,
                onClick = { /* TODO: share */ },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            )
        )

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type          = "Back",
                    title         = "Space Details"
                )
            },
            containerColor = Color.White,
            floatingActionButton = {
                RadialFab(
                    items      = floatingActionButtons,
                    radius     = 120.dp,        // ↑ bán kính ≥ mainSize + miniSize
                    startDeg   = -90f,          // góc bắt đầu –90° (mở thẳng lên)
                    sweepDeg   = -90f,         // quét 90°
                    onParentClick = { /* add new group */ }
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = {
                MenuBottom(navController)
            }
        ) { scaffoldPadding ->
            Column (
                modifier= Modifier.padding(scaffoldPadding)
            ) {
                ColoredCornerBox(
                    cornerRadius = 40.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        SearchBar(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onSearch = { query ->
                                /* TODO: điều kiện search */
                            }
                        )
                    }
                }

                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LabeledBox(
                            label = "Thiết bị",
                            value = deviceList.size.toString()
                        )
                    }
                }

                if (deviceList.isNotEmpty()) {
                    LazyColumn(modifier = modifier
                        .fillMaxSize()
                    ) {
                        itemsIndexed(deviceList) { deviceIndex, deviceUi ->
                            Spacer(Modifier.height(8.dp))
                            DeviceCardSwipeable(
                                deviceName = deviceUi.name?: "Thiết bị không tên,",
                                roomName = deviceUi.serial_number,
                                isRevealed = deviceUi.isRevealed,
                                onExpandOnly = {
                                    deviceList.indices.forEach { i ->
                                        spaceViewModel.updateRevealState(i)
                                    }
                                },
                                onClick = {
                                    navController.navigate(
                                        Screens.DynamicDeviceDetail.build(
                                            deviceId     = deviceUi.device_id?:"",
                                            deviceName   = deviceUi.name.orEmpty(),
                                            serialNumber = deviceUi.serial_number,
                                            deviceTypeName = deviceUi.device_type_name,
                                            deviceTypeParentName = null,
                                            permissionType = permissionType,
                                            productId    = deviceUi.template_id,
                                            groupId = deviceUi.group_id?:0
                                        )
                                    )
                                },
                                onCollapse = {
                                    spaceViewModel.updateRevealState(deviceIndex)
                                },
                                onDelete = {  },
                                onEdit = { /* TODO */ }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Không tìm thấy")
                    }
                }
                // Bottom Sheet để chỉnh sửa space
                BottomSheetWithTrigger(
                    isSheetVisible = isSheetVisible,
                    onDismiss = { isSheetVisible = false }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Chỉnh sửa Space",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        OutlinedTextField(
                            value = spaceNameInput,
                            onValueChange = { spaceNameInput = it },
                            label = { Text("Tên Space") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = iconNameInput,
                            onValueChange = { iconNameInput = it },
                            label = { Text("Tên Icon (ví dụ: living-room)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = iconColorInput,
                            onValueChange = { iconColorInput = it },
                            label = { Text("Màu Icon (ví dụ: #3366FF)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = descriptionInput,
                            onValueChange = { descriptionInput = it },
                            label = { Text("Mô tả") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                onClick = { isSheetVisible = false }
                            ) {
                                Text("Hủy")
                            }
                            Button(
                                onClick = {
                                    scope.launch {
                                        try {
                                            updateSpaceViewModel.updateSpace(
                                                spaceId = spaceId,
                                                name = spaceNameInput,
                                                iconName = iconNameInput.takeIf { it.isNotBlank() },
                                                iconColor = iconColorInput.takeIf { it.isNotBlank() },
                                                description = descriptionInput.takeIf { it.isNotBlank() }
                                            )
                                            snackbarViewModel.showSnackbar("Cập nhật space thành công")
                                            isSheetVisible = false
                                        } catch (e: Exception) {
                                            snackbarViewModel.showSnackbar("Lỗi: ${e.message}")
                                        }
                                    }
                                },
                                enabled = spaceNameInput.isNotBlank()
                            ) {
                                Text("Lưu")
                            }
                        }
                    }
                }
            }
        }
    }
}