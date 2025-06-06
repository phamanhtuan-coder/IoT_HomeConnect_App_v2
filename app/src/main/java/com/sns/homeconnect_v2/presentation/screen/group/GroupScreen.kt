package com.sns.homeconnect_v2.presentation.screen.group

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.core.util.validation.getIconByName
import com.sns.homeconnect_v2.core.util.validation.parseColorOrDefault
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateGroupRequest
import com.sns.homeconnect_v2.presentation.component.BottomSheetWithTrigger
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.GroupCardSwipeable
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.ColorPicker
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.IconPicker
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.LabeledBox
import com.sns.homeconnect_v2.presentation.component.widget.RadialFab
import com.sns.homeconnect_v2.presentation.component.widget.SearchBar
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField
import com.sns.homeconnect_v2.presentation.model.FabChild
import com.sns.homeconnect_v2.presentation.viewmodel.group.UpdateGroupViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.group.GroupListViewModel

@Composable
fun GroupScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    groupViewModel: GroupListViewModel = hiltViewModel(),
    updateGroup: UpdateGroupViewModel = hiltViewModel(),
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    val groups by groupViewModel.groupList.collectAsState()

    // Gọi fetchGroups một lần khi mở màn hình
    LaunchedEffect(Unit) {
        groupViewModel.fetchGroups()
    }

    // Track the last selected route
    //val currentRoute = navController.currentBackStackEntry?.destination?.route

    // State to control the visibility of the bottom sheet
    var isSheetVisible by remember { mutableStateOf(false) }

    // State to hold the group being edited
    var nameGroup by remember { mutableStateOf("") }
    var idGroup by remember { mutableIntStateOf(-1) }

    var groupDesc by remember { mutableStateOf("") }

    // ---------------- icon + color state ----------------
    var selectedLabel by remember { mutableStateOf("Nhà") }
    var selectedColor by remember { mutableStateOf("blue") }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        val fabChildren = listOf(
            FabChild(
                icon = Icons.Default.Edit,
                onClick = { /* TODO: sửa */

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
                    items      = fabChildren,
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
        ) { inner ->
            Column (
                modifier= Modifier.padding(inner)
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

                LazyColumn(modifier = modifier.fillMaxSize()) {
                    itemsIndexed(groups) { index, group ->
                        Spacer(Modifier.height(8.dp))
                        GroupCardSwipeable(
                            groupName = group.name,
                            memberCount = group.members,
                            icon = getIconByName(group.iconName),
                            iconColor = parseColorOrDefault(group.iconColorName),
                            isRevealed = group.isRevealed,
                            role = group.role,
                            onExpandOnly = {
                                groupViewModel.updateRevealState(index)
                            },
                            onCollapse = {
                                groupViewModel.collapseItem(index)
                            },
                            onDelete = {
                                // TODO
                            },
                            onEdit = {
                                idGroup = group.id
                                nameGroup = group.name
                                groupDesc = group.description ?: ""
                                selectedLabel = group.iconName
                                selectedColor = group.iconColorName
                                isSheetVisible = true
                            }
                        )
                    }
                }

                BottomSheetWithTrigger(
                    isSheetVisible = isSheetVisible,
                    onDismiss = { isSheetVisible = false },
                    sheetContent = {
                        LazyColumn (
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
                                    selectedIconLabel = selectedLabel,
                                    onIconSelected = { selectedLabel = it }
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
                                                icon_name = selectedLabel,
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
