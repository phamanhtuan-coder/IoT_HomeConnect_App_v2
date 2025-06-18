package com.sns.homeconnect_v2.presentation.screen.group

import IoTHomeConnectAppTheme
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.core.util.validation.toHouseUi
import com.sns.homeconnect_v2.presentation.component.BottomSheetWithTrigger
import com.sns.homeconnect_v2.presentation.component.HouseCardSwipeable
import com.sns.homeconnect_v2.presentation.component.UserCardSwipeable
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.model.FabChild
import com.sns.homeconnect_v2.presentation.model.HouseUi
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.group.DeleteGroupViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.group.DetailGroupViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.group.UpdateGroupMemberRoleUiState
import com.sns.homeconnect_v2.presentation.viewmodel.group.UpdateGroupMemberRoleViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.house.DeleteHouseViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.space.SpaceScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetailGroupScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
    groupId: Int
) {
    val viewModel: DetailGroupViewModel = hiltViewModel()
    val updateRoleViewModel: UpdateGroupMemberRoleViewModel = hiltViewModel()
    val spaceViewModel: SpaceScreenViewModel = hiltViewModel()
    val deleteGroupViewModel: DeleteGroupViewModel = hiltViewModel()
    val deleteHouseViewModel: DeleteHouseViewModel = hiltViewModel()

    val members by viewModel.members.collectAsState()
    val housesResponse by viewModel.houses.collectAsState()
    val spaces by spaceViewModel.spaces.collectAsState()
    //val spaceError by spaceViewModel.errorState.collectAsState()
    val deleteGroupState by deleteGroupViewModel.deleteState.collectAsState()
    val deleteHouseState by deleteHouseViewModel.deleteState.collectAsState()
    val refreshTrigger = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("refresh", false)
        ?.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val currentUserRole = members.find { it.account_id == viewModel.currentUserId }?.role ?: ""

//    // Xử lý lỗi tải không gian
//    LaunchedEffect(spaceError) {
//        spaceError?.let { error ->
//            snackbarViewModel.showSnackbar(
//                msg = "Lỗi khi kiểm tra không gian: ${error.message ?: "Unknown error"}",
//                variant = SnackbarVariant.ERROR
//            )
//            spaceViewModel.resetErrorState()
//        }
//    }

    // Xử lý trạng thái xóa nhóm
    LaunchedEffect(deleteGroupState) {
        deleteGroupState?.let { result ->
            if (result.isSuccess) {
                snackbarViewModel.showSnackbar(
                    msg = "Xóa nhóm thành công",
                    variant = SnackbarVariant.SUCCESS
                )
                navController.popBackStack()
                deleteGroupViewModel.resetDeleteState()
            } else {
                snackbarViewModel.showSnackbar(
                    msg = "Lỗi khi xóa nhóm: ${result.exceptionOrNull()?.message ?: "Unknown error"}",
                    variant = SnackbarVariant.ERROR
                )
                deleteGroupViewModel.resetDeleteState()
            }
        }
    }

    // Xử lý trạng thái xóa nhà
    LaunchedEffect(deleteHouseState) {
        deleteHouseState?.let { result ->
            if (result.isSuccess) {
                snackbarViewModel.showSnackbar(
                    msg = "Xóa nhà thành công",
                    variant = SnackbarVariant.SUCCESS
                )
                viewModel.fetchHouses() // Làm mới danh sách nhà
                deleteHouseViewModel.resetDeleteState()
            } else {
                snackbarViewModel.showSnackbar(
                    msg = "Lỗi khi xóa nhà: ${result.exceptionOrNull()?.message ?: "Unknown error"}",
                    variant = SnackbarVariant.ERROR
                )
                deleteHouseViewModel.resetDeleteState()
            }
        }
    }

    val housesUi = remember { mutableStateListOf<HouseUi>() }
    LaunchedEffect(housesResponse) {
        housesUi.clear()
        housesUi.addAll(housesResponse.map { it.toHouseUi(role = currentUserRole) })
    }

    LaunchedEffect(refreshTrigger?.value) {
        if (refreshTrigger?.value == true) {
            viewModel.fetchGroupMembers()
            viewModel.fetchHouses()
            navController.currentBackStackEntry?.savedStateHandle?.set("refresh", false)
        }
    }

    val selectedIcon = Icons.Default.Home
    val groupName = "Family Group"
    val groupDescription = "Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group "
    val memberCount = members.size.toString()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Thành viên", "Nhà")
    val revealStates = remember(members) {
        members.map { mutableStateOf(false) }.toMutableStateList()
    }

    var isRoleSheetVisible by remember { mutableStateOf(false) }
    var selectedMemberId by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("") }
    val availableRoles = listOf("admin", "vice", "member")

    var showDeleteDialog by remember { mutableStateOf(false) }
    var houseToDeleteId by remember { mutableStateOf<Int?>(null) }

    // Handle role update state
    LaunchedEffect(updateRoleViewModel.updateState.collectAsState().value) {
        when (val state = updateRoleViewModel.updateState.value) {
            is UpdateGroupMemberRoleUiState.Success -> {
                snackbarViewModel.showSnackbar(
                    msg = "Cập nhật quyền thành công",
                    variant = SnackbarVariant.SUCCESS
                )
                viewModel.fetchGroupMembers()
                isRoleSheetVisible = false
            }
            is UpdateGroupMemberRoleUiState.Error -> {
                snackbarViewModel.showSnackbar(
                    msg = state.message,
                    variant = SnackbarVariant.ERROR
                )
            }
            else -> Unit
        }
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        val fabChildren = listOf(
            FabChild(
                icon = Icons.Default.Add,
                onClick = {
                    when (selectedTab) {
                        0 -> navController.navigate(Screens.AddUser.createRoute(groupId))
                        1 -> navController.navigate(Screens.CreateHouse.createRoute(groupId))
                    }
                },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ),
            FabChild(
                icon = Icons.Default.Delete,
                onClick = {
                    snackbarViewModel.showSnackbar(
                        msg = "Vui lòng chọn nhà để xóa",
                        variant = SnackbarVariant.WARNING
                    )
                },
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

        // Delete confirmation dialog
        if (showDeleteDialog && houseToDeleteId != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = "Xác nhận xóa nhà") },
                text = { Text(text = "Bạn có chắc chắn muốn xóa nhà này không?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            deleteHouseViewModel.deleteHouse(houseToDeleteId!!)
                            showDeleteDialog = false
                            houseToDeleteId = null
                        }
                    ) {
                        Text("Xác nhận", color = colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            houseToDeleteId = null
                        }
                    ) {
                        Text("Hủy")
                    }
                }
            )
        }

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
                RadialFab(
                    items = fabChildren,
                    radius = 120.dp,
                    startDeg = -90f,
                    sweepDeg = -90f,
                    onParentClick = {
                        when (selectedTab) {
                            0 -> navController.navigate(Screens.AddUser.createRoute(groupId))
                            1 -> navController.navigate(Screens.CreateHouse.createRoute(groupId))
                        }
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = { MenuBottom(navController) }
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
                            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = selectedIcon,
                                        contentDescription = "Group Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = groupName,
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
                                        contentDescription = "Description Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = groupDescription,
                                        modifier = Modifier.weight(1f),
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            Column(modifier = Modifier.align(Alignment.CenterEnd)) {
                                IconButton(
                                    onClick = {
                                        deleteGroupViewModel.deleteGroup(groupId)
                                    },
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .size(76.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ExitToApp,
                                        contentDescription = "Exit group",
                                        tint = Color.Red,
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
                    CustomTabRow(
                        tabs = tabTitles,
                        selectedTabIndex = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                }
                when (selectedTab) {
                    0 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            SearchBar(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                onSearch = { /* TODO */ }
                            )
                            Spacer(Modifier.height(8.dp))
                            LabeledBox(
                                label = "Số lượng thành viên",
                                value = memberCount,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(
                                bottom = innerPadding.calculateBottomPadding() - 32.dp
                            )
                        ) {
                            itemsIndexed(members) { index, member ->
                                UserCardSwipeable(
                                    userName = member.full_name ?: member.username,
                                    role = currentUserRole,
                                    avatarUrl = member.avatar ?: "",
                                    isRevealed = revealStates[index].value,
                                    onExpandOnly = {
                                        revealStates.forEachIndexed { i, state ->
                                            state.value = i == index
                                        }
                                    },
                                    onCollapse = { revealStates[index].value = false },
                                    onDelete = { /* TODO */ },
                                    onEdit = {
                                        if (member.role != "owned") {
                                            selectedMemberId = member.account_id
                                            selectedRole = member.role
                                            isRoleSheetVisible = true
                                        }
                                    }
                                )
                            }
                        }
                        BottomSheetWithTrigger(
                            isSheetVisible = isRoleSheetVisible,
                            onDismiss = { isRoleSheetVisible = false },
                            sheetContent = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Chọn quyền",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                    availableRoles.forEach { role ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    selectedRole = role
                                                    updateRoleViewModel.updateMemberRole(
                                                        groupId = groupId,
                                                        accountId = selectedMemberId,
                                                        role = role.lowercase()
                                                    )
                                                }
                                                .padding(vertical = 12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = role.capitalize(),
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            if (role == selectedRole) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                        if (role != availableRoles.last()) {
                                            Divider()
                                        }
                                    }
                                }
                            }
                        )
                    }
                    1 -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Spacer(Modifier.height(8.dp))
                            LabeledBox(
                                label = "Số lượng nhà",
                                value = housesUi.size.toString(),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .navigationBarsPadding(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(
                                    bottom = innerPadding.calculateBottomPadding() - 32.dp
                                )
                            ) {
                                itemsIndexed(housesUi) { index, house ->
                                    HouseCardSwipeable(
                                        houseName = house.name,
                                        spaceCount = house.spaces,
                                        icon = house.icon,
                                        iconColor = house.iconColor,
                                        isRevealed = house.isRevealed,
                                        role = currentUserRole,
                                        onExpandOnly = {
                                            housesUi.indices.forEach { i ->
                                                housesUi[i] = housesUi[i].copy(isRevealed = i == index)
                                            }
                                        },
                                        onCollapse = {
                                            housesUi[index] = house.copy(isRevealed = false)
                                        },
                                        onDelete = {
                                            coroutineScope.launch {
                                                spaceViewModel.getSpaces(house.id)
                                                delay(100) // Đợi dữ liệu cập nhật
                                                if (spaces.isEmpty()) {
                                                    showDeleteDialog = true
                                                    houseToDeleteId = house.id
                                                } else {
                                                    snackbarViewModel.showSnackbar(
                                                        msg = "Không thể xóa nhà còn có không gian bên trong!",
                                                        variant = SnackbarVariant.ERROR
                                                    )
                                                }
                                            }
                                        },
                                        onEdit = {
                                            navController.navigate(Screens.EditHouse.createRoute(house.id))
                                        },
                                        onClick = {
                                            navController.navigate(Screens.ListSpace.createRoute(house.id, currentUserRole))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}