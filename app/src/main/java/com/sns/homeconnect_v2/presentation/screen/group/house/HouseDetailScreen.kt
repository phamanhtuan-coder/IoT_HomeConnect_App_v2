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
//    houseIcon: ImageVector,
//    colorIcon: Color,
//    houseName: String,
//    houseAddress: String,
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



    val spaces by spaceViewModel.spaces.collectAsState()
    Log.d("spaces", spaces.toString())
    Log.d("houseId", houseId.toString())

    val houseViewModel: GetHouseViewModel = hiltViewModel()
    val houseState by houseViewModel.houses.collectAsState()
    LaunchedEffect(Unit) {
        houseViewModel.getHouse(houseId)
    }

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
                FloatingActionButton(
                    onClick = {
                        Log.d("FAB", "Add button clicked")
                        snackbarViewModel.showSnackbar("Đã nhấn nút thêm!")
                        // TODO: Thêm logic để thêm không gian mới, ví dụ:
                        navController.navigate(Screens.AddSpace.createRoute(houseId))
                    },
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary,
                    modifier = Modifier.size(60.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center, // Căn giữa nội dung
                        modifier = Modifier.fillMaxSize() // Đảm bảo Box chiếm toàn bộ không gian của FAB
                    ) {
                        Text(
                            text = "+",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onPrimary
                        )
                    }
                }
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
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .height(IntrinsicSize.Min)
                        ) {
                            // Trái: icon + tên + địa chỉ
                            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = "House Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = houseState?.house_name ?: "Không có tên",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.width(250.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EditCalendar,
                                        contentDescription = "Address Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = houseState?.address ?: "Không có địa chỉ",
                                        modifier = Modifier.weight(1f),
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            // Phải: nút sửa
                            Column(
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                IconButton(
                                    onClick = { /* TODO: chỉnh sửa house */ },
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .size(76.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit house",
                                        tint = Color.White,
                                        modifier = Modifier.size(46.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {
                    LabeledBox(
                        label = "Không gian",
                        value = spaces.size.toString(),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                LazyColumn {
                    itemsIndexed(spaces) { index, space ->
                        Spacer(Modifier.height(8.dp))

                        updateSpaceViewModel.updateSpace(space.space_id, spaceNameInput, iconNameInput, iconColorInput, descriptionInput)

                        Log.d("SpaceDetail", "Space ID: ${space.space_id}, Name: ${space.space_name}, Icon: ${space.icon_name}, Color: ${space.icon_color}")
                        SpaceCardSwipeable(
                            spaceName = space.space_name?: "không có tên",
                            deviceCount = space.space_id,
                            iconName = space.icon_name ?: "home",
                            iconColor = space.icon_color?.toColor()?: Color.Gray,
                            isRevealed = space.isRevealed,
                            role = currentUserRole,
                            onExpandOnly = {
                                spaceViewModel.updateRevealState(index)
                            },
                            onCollapse = {
                                spaceViewModel.collapseItem(index)
                            },

                            onDelete = {
                                //xóa space
                                showDeleteDialog = true
                            },
                                           
                            onEdit = {
                                isSheetVisible = true

                            },
                            onClick = {
                                navController.navigate(Screens.SpaceDetail.createRoute(space.space_id))
                                Log.d("Space Clicked", "ID: ${space.space_id}, Name: ${space.space_name}")
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

            }
        }
    }
}

