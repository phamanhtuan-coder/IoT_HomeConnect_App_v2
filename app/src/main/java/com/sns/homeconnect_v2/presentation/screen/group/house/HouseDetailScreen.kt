package com.sns.homeconnect_v2.presentation.screen.group.house

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.validation.parseColorOrDefault
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.core.util.validation.toColor
import com.sns.homeconnect_v2.presentation.component.BottomSheetWithTrigger
import com.sns.homeconnect_v2.presentation.component.SpaceCardSwipeable
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.model.FabChild
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.house.GetHouseViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.space.DeleteSpaceViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.space.SpaceScreenViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.space.UpdateSpaceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HouseDetailScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
 spaceViewModel:SpaceScreenViewModel = hiltViewModel(),
    houseId:Int,
    currentUserRole: String,
) {
    val deletespace: DeleteSpaceViewModel = hiltViewModel()
    val deteteState by deletespace.deleteState.collectAsState()
    val updateSpaceViewModel: UpdateSpaceViewModel = hiltViewModel()

    val spaceDetail by updateSpaceViewModel.updatespace.collectAsState()

    // Trạng thái cho bottom sheet
    var isSheetVisible by remember { mutableStateOf(false) }
    var spaceNameInput by remember { mutableStateOf("") }
    var iconNameInput by remember { mutableStateOf("") }
    var iconColorInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }

    LaunchedEffect(spaceDetail) {
        spaceNameInput = spaceDetail?.space_name ?: ""
        iconNameInput = spaceDetail?.icon_name ?: ""
        iconColorInput = spaceDetail?.icon_color ?: ""
        descriptionInput = spaceDetail?.space_description ?: ""
    }

    val scope = rememberCoroutineScope()
    val spaces by spaceViewModel.spaces.collectAsState()
    val houseViewModel: GetHouseViewModel = hiltViewModel()
    val houseState by houseViewModel.houses.collectAsState()

    // Xử lý trạng thái xóa nhóm
    LaunchedEffect(deteteState) {
        deteteState?.let { result ->
            if (result.isSuccess) {
                snackbarViewModel.showSnackbar(
                    msg = "Xóa nhóm thành công",
                    variant = SnackbarVariant.SUCCESS
                )
                spaceViewModel.fetchSpaces(houseId) // Làm mới danh sách nhóm
                deletespace.resetDeleteState()
            } else {
                snackbarViewModel.showSnackbar(
                    msg = "Lỗi khi xóa nhóm: ${result.exceptionOrNull()?.message ?: "Unknown error"}",
                    variant = SnackbarVariant.ERROR
                )
                deletespace.resetDeleteState()
            }
        }
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        houseViewModel.getHouse(houseId)
        spaceViewModel.getSpaces(houseId)
    }
    Log.d("HouseDetailScreen", houseState.toString())


//    val spaces = remember {
//        mutableStateListOf(
//            SpaceUi(1, "Bathroom", 5, false, Icons.Default.BedroomBaby, Color.Blue),
//            SpaceUi(2, "Bedroom", 3, false, Icons.Default.Home, Color.Red),
//            SpaceUi(3, "Lounge", 7, false, Icons.Default.Group, Color.Green)
//        )
//    }

    IoTHomeConnectAppTheme {

//        val fabOptions = listOf(
////            FabChild(
////                Icons.Default.Edit,
////                onClick = { /* TODO: sửa */ },
////                containerColor = colorScheme.primary,
////                contentColor = colorScheme.onPrimary
////            ),
////            FabChild(
////                Icons.Default.Delete,
////                onClick = { /* TODO: xoá */ },
////                containerColor = colorScheme.primary,
////                contentColor = colorScheme.onPrimary
////            ),
//
//        )
        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "House Settings"
                )
            },
            containerColor = Color.White,
            floatingActionButton = {
                RadialFab(
                    items = listOf(
                        FabChild(Icons.Default.Edit, onClick = {}, containerColor = colorScheme.primary, contentColor = colorScheme.onPrimary),
                        FabChild(Icons.Default.Delete, onClick = {}, containerColor = colorScheme.primary, contentColor = colorScheme.onPrimary),
                        FabChild(Icons.Default.Add, onClick = {
                            // navController.navigate(Screens.AddSpace.createRoute(houseId))
                        }, containerColor = colorScheme.primary, contentColor = colorScheme.onPrimary)
                    ),
                    radius = 120.dp,
                    startDeg = -90f,
                    sweepDeg = -90f,
                    onParentClick = {}
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = {
                MenuBottom(navController)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ColoredCornerBox(cornerRadius = 40.dp) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(text = houseState?.house_name ?: "Không có tên", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EditCalendar, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(text = houseState?.address ?: "Không có địa chỉ", color = Color.White, fontSize = 16.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }

                InvertedCornerHeader(backgroundColor = colorScheme.surface, overlayColor = colorScheme.primary) {
                    LabeledBox(label = "Không gian", value = spaces.size.toString(), modifier = Modifier.padding(16.dp))
                }

                LazyColumn {
                    itemsIndexed(spaces) { index, space ->
                        Spacer(Modifier.height(8.dp))

                        updateSpaceViewModel.updateSpace(space.space_id, spaceNameInput, iconNameInput, iconColorInput, descriptionInput)

                        Log.d("SpaceDetail", "Space ID: ${space.space_id}, Name: ${space.space_name}, Icon: ${space.icon_name}, Color: ${space.icon_color}")

                        SpaceCardSwipeable(
                            spaceName = space.space_name ?: "không có tên",
                            deviceCount = space.space_id,
                            icon = space.icon_name?.toIcon() ?: Icons.Default.Home,
                            iconColor = parseColorOrDefault(space.icon_color),
                            isRevealed = space.isRevealed,
                            role = currentUserRole,
                            onExpandOnly = {
                                spaceViewModel.expandItem(index) // Chỉ mở card tại index
                            },
                            onCollapse = {
                                spaceViewModel.collapseItem(index) // Chỉ đóng card tại index
                            },
                            onDelete = {
                                //xóa space
                                showDeleteDialog = true
                            },
                                           
                          onEdit = {
                                updateSpaceViewModel.setEditingSpace(space)
                                isSheetVisible = true
                            },
                            onClick = {
                                navController.navigate(Screens.SpaceDetail.createRoute(space.space_id))
                            }
                        )


                        // Delete confirmation dialog
                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                title = { Text(text = "Xác nhận xóa nhóm") },
                                text = { Text(text = "Bạn có chắc chắn muốn xóa không gian này không? ") },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            deletespace.deleteSpace(space.space_id)
                                            showDeleteDialog = false
                                        }
                                    ) {
                                        Text("Xác nhận", color = colorScheme.error)
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            showDeleteDialog = false
                                        }
                                    ) {
                                        Text("Hủy")
                                    }
                                }
                            )
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
                                                        spaceId = space.space_id,
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

                    item {
                        Spacer(Modifier.height(8.dp))
                        ActionButtonWithFeedback(
                            label = "Cập nhật",
                            style = HCButtonStyle.PRIMARY,
                            onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } },
                            modifier = Modifier.padding(horizontal = 16.dp),
                            snackbarViewModel = snackbarViewModel
                        )
                        Spacer(Modifier.height(8.dp))
                        ActionButtonWithFeedback(
                            label = "Xoá",
                            style = HCButtonStyle.DISABLED,
                            onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } },
                            modifier = Modifier.padding(horizontal = 16.dp),
                            snackbarViewModel = snackbarViewModel
                        )
                    }
                }

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
                        Text("Chỉnh sửa Space", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 16.dp))
                        OutlinedTextField(
                            value = spaceNameInput,
                            onValueChange = { spaceNameInput = it },
                            label = { Text("Tên Space") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        IconPicker(selectedIconLabel = iconNameInput, onIconSelected = { iconNameInput = it })
                        ColorPicker(selectedColorLabel = iconColorInput, onColorSelected = { iconColorInput = it })
                        OutlinedTextField(
                            value = descriptionInput,
                            onValueChange = { descriptionInput = it },
                            label = { Text("Mô tả") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = { isSheetVisible = false }) { Text("Hủy") }
                            Button(
                                onClick = {
                                    scope.launch {
                                        try {
                                            updateSpaceViewModel.updateSpace(
                                                spaceId = spaceDetail?.space_id ?: return@launch,
                                                name = spaceNameInput,
                                                iconName = iconNameInput.takeIf { it.isNotBlank() },
                                                iconColor = iconColorInput.takeIf { it.isNotBlank() },
                                                description = descriptionInput.takeIf { it.isNotBlank() }
                                            )
                                            spaceViewModel.getSpaces(houseId)
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
