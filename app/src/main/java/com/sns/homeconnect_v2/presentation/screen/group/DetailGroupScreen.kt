package com.sns.homeconnect_v2.presentation.screen.group

import IoTHomeConnectAppTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.core.util.validation.RoleLevel
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.core.util.validation.hasPermission
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
import com.sns.homeconnect_v2.presentation.viewmodel.group.DetailGroupViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.group.UpdateGroupMemberRoleUiState
import com.sns.homeconnect_v2.presentation.viewmodel.group.UpdateGroupMemberRoleViewModel

@Composable
fun DetailGroupScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel,
    groupId: Int
) {
    val viewModel: DetailGroupViewModel = hiltViewModel()
    val updateRoleViewModel: UpdateGroupMemberRoleViewModel = hiltViewModel()
    val members by viewModel.members.collectAsState()
    val housesResponse by viewModel.houses.collectAsState()
    val refreshTrigger = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("refresh", false)
        ?.collectAsState()

    val housesUi = remember { mutableStateListOf<HouseUi>() }
    LaunchedEffect(housesResponse) {
        housesUi.clear()
        housesUi.addAll(housesResponse.map { it.toHouseUi(role = "member") }) // ToDo: Sửa quyền sau này
    }

    LaunchedEffect(refreshTrigger?.value) {
        if (refreshTrigger?.value == true) {
            viewModel.fetchGroupMembers()
            viewModel.fetchHouses()  // Thêm fetch houses
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("refresh", false) // Reset flag
        }
    }

    val selectedIcon = Icons.Default.Home

    val groupName        = "Family Group"
    val groupDescription = "Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group "

    val memberCount = members.size.toString()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Thành viên", "Nhà")

    val revealStates = remember(members) {
        members.map { mutableStateOf(false) }.toMutableStateList()
    }

    // State for role editing
    var isRoleSheetVisible by remember { mutableStateOf(false) }
    var selectedMemberId by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("") }

    // Available roles (excluding "owned")
    val availableRoles = listOf("admin", "vice", "member")

    // Handle role update state
    val roleUpdateState by updateRoleViewModel.updateState.collectAsState()
    LaunchedEffect(roleUpdateState) {
        when (roleUpdateState) {
            is UpdateGroupMemberRoleUiState.Success -> {
                snackbarViewModel.showSnackbar(
                    msg = "Cập nhật quyền thành công",
                    variant = SnackbarVariant.SUCCESS
                )
                viewModel.fetchGroupMembers()
                isRoleSheetVisible = false
            }
            is UpdateGroupMemberRoleUiState.Error -> {
                val errorState = roleUpdateState as? UpdateGroupMemberRoleUiState.Error
                errorState?.let { error ->
                    snackbarViewModel.showSnackbar(
                        msg = error.message,
                        variant = SnackbarVariant.ERROR
                    )
                }
            }
            else -> Unit
        }
    }

    IoTHomeConnectAppTheme {
        val fabChildren = listOf(
            FabChild(
                icon = Icons.Default.Add,
                onClick = {
                    when (selectedTab) {
                        0 -> {
                            navController.navigate(Screens.AddUser.createRoute(groupId))
                        }
                        1 -> {
                            navController.navigate(Screens.CreateHouse.createRoute(groupId))
                        }
                    }
                },
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
                    title         = "Settings"
                )
            },
            containerColor = Color.White,
            floatingActionButton = {
                RadialFab(
                    items            = fabChildren,
                    radius           = 120.dp,
                    startDeg         = -90f,
                    sweepDeg         = -90f,
                    onParentClick    = { /* thêm thành viên */ }
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

                /* ------------ HEADER MÀU ĐẬM (có góc bo) -------------- */
                ColoredCornerBox(
                    cornerRadius    = 40.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        /* ----- Thông tin tên & mô tả group ----- */
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .height(IntrinsicSize.Min)
                        ) {
                            /*  Phần trái: icon – tên – mô tả  */
                            Column(
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                // Tên
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
                                // Mô tả
                                Row(
                                    modifier = Modifier
                                        .width(250.dp),               // hàng đủ rộng
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EditCalendar,
                                        contentDescription = "Description Icon",
                                        tint  = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))

                                    Text(
                                        text = groupDescription,
                                        modifier = Modifier.weight(1f),               // <── quan trọng
                                        color  = Color.White,
                                        fontSize = 16.sp,
                                        // tuỳ chọn: tối đa 2 dòng, nếu dài hơn sẽ có dấu …
                                        maxLines  = 2,
                                        overflow  = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Column (
                                modifier = Modifier
                                    .align(Alignment.CenterEnd),
                            ) {
                                IconButton(
                                    onClick = { /* TODO: rời group */ },
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

                /* ------------ KHUNG TRẮNG BO GÓC NGƯỢC -------------- */
                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {
                    CustomTabRow(
                        tabs = tabTitles,
                        selectedTabIndex = selectedTab,
                        onTabSelected = { selectedTab = it },
                    )
                }

                when (selectedTab) {
                    0 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            // --- thanh tìm kiếm ---
                            SearchBar(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                onSearch = { /* TODO */ }
                            )

                            Spacer(Modifier.height(8.dp))

                            // --- hàng thống kê ---
                            LabeledBox(
                                label = "Số lượng thành viên",
                                value = memberCount,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }

                        /* ------------ DANH SÁCH THÀNH VIÊN -------------- */
                        val bottomPadding = innerPadding.calculateBottomPadding()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding(),                // tránh gesture-bar nếu có
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(
                                bottom = bottomPadding-32.dp       // ❶  đẩy cao hơn BottomBar/FAB
                            )
                        ) {
                            itemsIndexed(members) { index, member ->
                                UserCardSwipeable(
                                    userName = member.full_name ?: member.username,
                                    role = member.role,
                                    avatarUrl = member.avatar ?: "",
                                    isRevealed = revealStates[index].value,
                                    onExpandOnly = {
                                        revealStates.forEachIndexed { i, state ->
                                            state.value = i == index
                                        }
                                    },
                                    onCollapse = { revealStates[index].value = false },
                                    onDelete = { /* Xoá user */ },
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

                        // BottomSheet for role selection
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Spacer(Modifier.height(8.dp))
                            // --- thống kê số lượng nhà ---
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
                                    bottom = innerPadding.calculateBottomPadding()-32.dp
                                )
                            ) {
                                itemsIndexed(housesUi) { index, house ->
                                    HouseCardSwipeable(
                                        houseName = house.name,
                                        spaceCount = house.spaces,
                                        icon = house.icon,
                                        iconColor = house.iconColor,
                                        isRevealed = house.isRevealed,
                                        role = members.find { it.account_id == viewModel.currentUserId }?.role ?: "",
                                        onExpandOnly = {
                                            housesUi.indices.forEach { i ->
                                                housesUi[i] = housesUi[i].copy(isRevealed = i == index)
                                            }
                                        },
                                        onCollapse = {
                                            housesUi[index] = house.copy(isRevealed = false)
                                        },
                                        onDelete = {
                                            housesUi.removeAt(index)
                                        },
                                        onEdit = {
                                            navController.navigate(Screens.EditHouse.createRoute(house.id))
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

//@Composable
//fun HouseTabContent(
//    houses: SnapshotStateList<HouseUi>,
//    modifier: Modifier = Modifier,
//    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
//    navController: NavHostController
//) {
//    val viewModel: DetailGroupViewModel = hiltViewModel()
//
//    val canEdit = hasPermission(currentUserRole, RoleLevel.VICE)
//    val canDelete = hasPermission(currentUserRole, RoleLevel.VICE)
//
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//    ) {
//        Spacer(Modifier.height(8.dp))
//        // --- thống kê số lượng nhà ---
//        LabeledBox(
//            label = "Số lượng nhà",
//            value = houses.size.toString(),
//            modifier = Modifier.padding(start = 16.dp)
//        )
//
//        Spacer(Modifier.height(8.dp))
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth(),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//        ) {
//            itemsIndexed(houses) { index, house ->
//                HouseCardSwipeable(
//                    houseName = house.name,
//                    spaceCount = house.spaces,
//                    icon = house.icon,
//                    iconColor = house.iconColor,
////                    isRevealed = house.isRevealed,
////                    role = currentUserRole,  // Thêm role vào để kiểm tra quyền
//                    onExpandOnly = {
//                        houses.indices.forEach { i ->
//                            houses[i] = houses[i].copy(isRevealed = i == index)
//                        }
//                    },
//                    onCollapse = {
//                        houses[index] = house.copy(isRevealed = false)
//                    },
////                    onDelete = {
////                        if (canDelete) {
////                            viewModel.deleteHouse(house.id)
////                            houses.removeAt(index)
////                        }
////                    },
////                    onEdit = {
////                        if (canEdit) {
////                            navController.navigate(Screens.EditHouse.createRoute(house.id))
////                        }
////                    }
//                )
//            }
//        }
//    }

//}
