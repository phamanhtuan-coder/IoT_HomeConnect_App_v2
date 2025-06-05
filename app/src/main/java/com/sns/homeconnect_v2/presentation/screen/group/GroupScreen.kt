package com.sns.homeconnect_v2.presentation.screen.group

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.presentation.component.BottomSheetWithTrigger
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuItem
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.GroupCardSwipeable
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.LabeledBox
import com.sns.homeconnect_v2.presentation.component.widget.RadialFab
import com.sns.homeconnect_v2.presentation.component.widget.SearchBar
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField
import com.sns.homeconnect_v2.presentation.model.FabChild
import com.sns.homeconnect_v2.presentation.model.GroupUi
import com.sns.homeconnect_v2.presentation.viewmodel.group.UpdateGroupViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

@Composable
fun GroupScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    updateGroup: UpdateGroupViewModel = hiltViewModel(),
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    val groups = remember {
        mutableStateListOf(
            GroupUi(1, "Gia đình", 5, false, Icons.Default.Group, Color.Blue,role = "member"),
            GroupUi(2, "Marketing", 3, false, Icons.Default.Home, Color.Red, role = "owned"),
            GroupUi(3, "Kỹ thuật", 7, false, Icons.Default.Group, Color.Green, role = "vice")
        )
    }

    val items = listOf(
        "Dashboard" to Pair(Icons.Filled.PieChart, "dashboard"),
        "Devices" to Pair(Icons.Filled.Devices, "devices"),
        "Home" to Pair(Icons.Filled.Home, "home"),
        "Profile" to Pair(Icons.Filled.Person, "profile"),
        "Settings" to Pair(Icons.Filled.Settings, "settings")
    )
    val context = LocalContext.current
    val isTablet = isTablet(context)

    // Track the last selected route
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    // State to control the visibility of the bottom sheet
    var isSheetVisible by remember { mutableStateOf(false) }

    // State to hold the group being edited
    var nameGroup by remember { mutableStateOf("") }
    var idGroup by remember { mutableIntStateOf(-1) }

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
                BottomAppBar(
                    tonalElevation = 4.dp,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.height(120.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Top
                    ) {
                        items.forEach { item ->
                            val isSelected = currentRoute == item.second.second
                            MenuItem(
                                text = item.first,
                                icon = item.second.first,
                                isSelected = isSelected,
                                onClick = {
                                    navController.navigate(item.second.second) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                isTablet = isTablet,
                            )
                        }
                    }
                }
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
                            value = "4"
                        )
                    }
                }
                
                LazyColumn(modifier = modifier
                    .fillMaxSize()
                ) {
                    itemsIndexed(groups) { index, group ->
                        Spacer(Modifier.height(8.dp))
                        GroupCardSwipeable(
                            groupName = group.name,
                            memberCount = group.members,
                            icon = group.icon,
                            iconColor = group.iconColor,
                            isRevealed = group.isRevealed,
                            role = group.role,
                            onExpandOnly = {
                                groups.indices.forEach { i ->
                                    groups[i] = groups[i].copy(isRevealed = i == index)
                                }
                            },
                            onCollapse = {
                                groups[index] = group.copy(isRevealed = false)
                            },
                            onDelete = { groups.removeAt(index) },
                            onEdit = {
                                idGroup = group.id
                                nameGroup = group.name
                                isSheetVisible = true
                            }
                        )
                    }
                }

                BottomSheetWithTrigger(
                    isSheetVisible = isSheetVisible,
                    onDismiss = { isSheetVisible = false },
                    sheetContent = {
                        Column (
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        ) {
                            StyledTextField(
                                value = nameGroup,
                                onValueChange = { nameGroup = it },
                                placeholderText = "Group name",
                                leadingIcon = Icons.Default.Person
                            )
                            Spacer(modifier.height(8.dp))
                            ActionButtonWithFeedback(
                                label = "Cập nhật",
                                style = HCButtonStyle.PRIMARY,
                                onAction = { ok, err ->
                                    if (nameGroup.isBlank()) {
                                        err("Tên nhóm không được để trống!")
                                        return@ActionButtonWithFeedback
                                    }

                                    updateGroup.updateGroup(
                                        groupId = idGroup,
                                        groupName = nameGroup,
                                        onSuccess = {
                                            ok(it)
                                            val index = groups.indexOfFirst { it.id == idGroup }
                                            if (index != -1) {
                                                groups[index] = groups[index].copy(name = nameGroup)
                                            }
                                            isSheetVisible = false
                                        },
                                        onError = {
                                            err(it)
                                        }
                                    )
                                },
                                snackbarViewModel = snackbarViewModel
                            )

                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "GroupScreen - Phone")
@Composable
fun GroupScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        GroupScreen(navController = rememberNavController())
    }
}