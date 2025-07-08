package com.sns.homeconnect_v2.presentation.screen.group

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant

import com.sns.homeconnect_v2.core.util.validation.parseColorOrDefault
import com.sns.homeconnect_v2.data.remote.dto.base.GroupIconCategory
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateGroupRequest
import com.sns.homeconnect_v2.presentation.component.BottomSheetWithTrigger
import com.sns.homeconnect_v2.presentation.component.GroupCardSwipeable
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.model.FabChild
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.group.DeleteGroupViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.group.GetListHouseByGroupViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.group.GetRoleViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.group.GroupListViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.group.UpdateGroupViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GroupScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    groupViewModel: GroupListViewModel = hiltViewModel(),
    updateGroup: UpdateGroupViewModel = hiltViewModel(),
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    var sel by remember { mutableStateOf<String?>(null) }
    val groups by groupViewModel.groupList.collectAsState()
    val delete: DeleteGroupViewModel = hiltViewModel()
    val deleteState by delete.deleteState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val roleViewModel: GetRoleViewModel = hiltViewModel()
    val roles by roleViewModel.roles.collectAsState()

    val listHouseViewModel: GetListHouseByGroupViewModel = hiltViewModel()
    val listhouses by listHouseViewModel.houses.collectAsState()

    // Gọi fetchGroups khi mở màn hình
    LaunchedEffect(Unit) {
        groupViewModel.fetchGroups()
    }

    // Gọi fetchRole cho tất cả groups khi danh sách groups thay đổi
    LaunchedEffect(groups) {
        groups.forEach { group ->
            roleViewModel.fetchRole(group.id)
        }
    }

    // Xử lý trạng thái xóa nhóm
    LaunchedEffect(deleteState) {
        deleteState?.let { result ->
            if (result.isSuccess) {
                snackbarViewModel.showSnackbar(
                    msg = "Xóa nhóm thành công",
                    variant = SnackbarVariant.SUCCESS
                )
                groupViewModel.fetchGroups()
                delete.resetDeleteState()
            } else {
                snackbarViewModel.showSnackbar(
                    msg = "Lỗi khi xóa nhóm: ${result.exceptionOrNull()?.message ?: "Unknown error"}",
                    variant = SnackbarVariant.ERROR
                )
                delete.resetDeleteState()
            }
        }
    }

    // State cho dialog xác nhận xóa nhóm
    var showDeleteDialog by remember { mutableStateOf(false) }
    var groupToDeleteId by remember { mutableStateOf<Int?>(null) }

    // State cho bottom sheet chỉnh sửa nhóm
    var isSheetVisible by remember { mutableStateOf(false) }
    var nameGroup by remember { mutableStateOf("") }
    var idGroup by remember { mutableIntStateOf(-1) }
    var groupDesc by remember { mutableStateOf("") }
    var selectedLabel by remember { mutableStateOf("Nhà") }
    var selectedColor by remember { mutableStateOf("blue") }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Settings"
                )
            },
            containerColor = Color.White,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        snackbarViewModel.showSnackbar("Đã nhấn nút thêm!")
                        navController.navigate(Screens.CreateGroup.route)
                    },
                    containerColor = colorScheme.primary ?: Color(0xFF6200EE),
                    contentColor = colorScheme.onPrimary ?: Color.White,
                    modifier = Modifier.size(60.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "+",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onPrimary ?: Color.White
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = {
                MenuBottom(navController)
            }
        ) { inner ->
            Column(
                modifier = Modifier.padding(inner)
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
                            modifier = Modifier.fillMaxWidth(),
                            onSearch = { query -> /* TODO: tìm kiếm nhóm */ }
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
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LabeledBox(
                            label = "Số nhóm",
                            value = groups.size.toString(),
                        )
                    }
                }

                // Dialog xác nhận xóa nhóm
                if (showDeleteDialog && groupToDeleteId != null) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text(text = "Xác nhận xóa nhóm") },
                        text = { Text(text = "Bạn có chắc chắn muốn xóa nhóm này không? Hành động này không thể hoàn tác.") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    delete.deleteGroup(groupToDeleteId!!)
                                    showDeleteDialog = false
                                    groupToDeleteId = null
                                }
                            ) {
                                Text("Xác nhận", color = colorScheme.error)
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showDeleteDialog = false
                                    groupToDeleteId = null
                                }
                            ) {
                                Text("Hủy")
                            }
                        }
                    )
                }

                LazyColumn(modifier = modifier.fillMaxSize()) {
                    itemsIndexed(groups, key = { _, group -> group.id }) { index, group ->
                        Spacer(Modifier.height(8.dp))

                        // Lấy role cho group này từ Map
                        val currentRole = roleViewModel.getRoleByGroupId(group.id) ?: "guest"

                        Log.d("GroupScreen", "Role for group ${group.id}: $currentRole")

                        GroupCardSwipeable(
                            groupName = group.name,
                            memberCount = group.members,
                            iconName    = group.iconName,
                            iconColor = parseColorOrDefault(group.iconColorName),
                            isRevealed = group.isRevealed,
                            role = currentRole,
                            onExpandOnly = {
                                groupViewModel.updateRevealState(index)
                            },
                            onCollapse = {
                                groupViewModel.collapseItem(index)
                            },
                            onDelete = {
                                if (currentRole == "owner") {
                                    coroutineScope.launch {
                                        listHouseViewModel.getHouseByGroup(group.id)
                                        delay(100) // Đợi dữ liệu cập nhật
                                        Log.d("GroupScreen", "Houses for group ${group.id}: ${listhouses.size}")
                                        if (listhouses.isEmpty()) {
                                            showDeleteDialog = true
                                            groupToDeleteId = group.id
                                        } else {
                                            snackbarViewModel.showSnackbar(
                                                msg = "Không thể xóa nhóm vì vẫn còn nhà trong nhóm",
                                                variant = SnackbarVariant.ERROR
                                            )
                                        }
                                    }
                                } else {
                                    snackbarViewModel.showSnackbar(
                                        msg = "Bạn không có quyền xóa nhóm",
                                        variant = SnackbarVariant.ERROR
                                    )
                                }
                            },
                            onEdit = {
                                idGroup = group.id
                                nameGroup = group.name
                                groupDesc = group.description ?: ""
                                selectedLabel = group.iconName
                                selectedColor = group.iconColorName
                                isSheetVisible = true
                            },
                            onClick = {
                                navController.navigate(Screens.GroupDetail.createRoute(group.id))
                            }
                        )
                    }
                }

                BottomSheetWithTrigger(
                    isSheetVisible = isSheetVisible,
                    onDismiss = { isSheetVisible = false },
                    sheetContent = {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                        ) {
                            item {
                                StyledTextField(
                                    value = nameGroup,
                                    onValueChange = { nameGroup = it },
                                    placeholderText = "Group name",
                                    leadingIcon = Icons.Default.Person,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                StyledTextField(
                                    value = groupDesc,
                                    onValueChange = { groupDesc = it },
                                    placeholderText = "Mô tả của group",
                                    leadingIcon = Icons.Default.NoteAlt,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                IconPicker(
                                    category = GroupIconCategory.GROUP,
                                    selectedIconName = sel,
                                    onIconSelected = { sel = it },
                                    modifier = Modifier.padding(16.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                ColorPicker(
                                    selectedColorLabel = selectedColor,
                                    onColorSelected = { selectedColor = it }
                                )
                                Spacer(Modifier.height(16.dp))
                                ActionButtonWithFeedback(
                                    label = "Cập nhật",
                                    style = HCButtonStyle.PRIMARY,
                                    snackbarViewModel = snackbarViewModel,
                                    onAction = { ok, err ->
                                        if (nameGroup.isBlank()) {
                                            err("Tên nhóm không được để trống!")
                                            return@ActionButtonWithFeedback
                                        }

                                        updateGroup.updateGroup(
                                            groupId = idGroup,
                                            request = UpdateGroupRequest(
                                                group_name = nameGroup,
                                                icon_name = sel ?: "house",
                                                icon_color = selectedColor,
                                                group_description = groupDesc
                                            ),
                                            onSuccess = {
                                                isSheetVisible = false
                                                groupViewModel.fetchGroups()
                                                ok("Cập nhật nhóm thành công!")
                                            },
                                            onError = {
                                                err("Cập nhật nhóm thất bại: $it")
                                            }
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}