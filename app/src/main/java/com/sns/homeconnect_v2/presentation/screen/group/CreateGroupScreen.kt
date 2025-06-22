package com.sns.homeconnect_v2.presentation.screen.group

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.presentation.component.widget.ColorPicker
import com.sns.homeconnect_v2.presentation.component.widget.IconPicker
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuItem
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.data.remote.dto.base.GroupIconCategory
import com.sns.homeconnect_v2.presentation.viewmodel.group.CreateGroupState
import com.sns.homeconnect_v2.presentation.viewmodel.group.CreateGroupViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

@Composable
fun CreateGroupScreen(
    navController: NavHostController,
    viewModel: CreateGroupViewModel = hiltViewModel(),
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    val createGroupState by viewModel.createGroupState.collectAsState()

    when (createGroupState) {
        is CreateGroupState.Success -> {
            LaunchedEffect(Unit) {
                snackbarViewModel.showSnackbar("Thêm nhóm thành cộng!", SnackbarVariant.SUCCESS)
                navController.navigate("home") {
                    popUpTo("create_group") { inclusive = true }
                }
            }
        }

        is CreateGroupState.Error -> {
            snackbarViewModel.showSnackbar("Thêm nhóm không thành công!", SnackbarVariant.ERROR)
            // Bạn có thể show lỗi UI hoặc snackbar
        }

        else -> Unit
    }

    var groupName by remember { mutableStateOf("") }
    var groupDesc by remember { mutableStateOf("") }

    // ---------------- icon + color state ----------------
    var selectedLabel by remember { mutableStateOf("Nhà") }
    var selectedColor by remember { mutableStateOf("#0000FF") }

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

    var sel by remember { mutableStateOf<String?>(null) }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type          = "Back",
                    title         = "Settings"
                )
            },
            containerColor = Color.White,
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                item {
                    ColoredCornerBox(
                        cornerRadius = 40.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .height(IntrinsicSize.Min)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                StyledTextField(
                                    value = groupName,
                                    onValueChange = { groupName = it },
                                    placeholderText = "Nhập tên nhóm",
                                    leadingIcon = Icons.Default.People,
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
                            }
                        }
                    }
                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ) {}
                }

                // ---------- icon picker ----------
                item {
                    IconPicker(
                        category = GroupIconCategory.GROUP,
                        selectedIconName = sel,
                        onIconSelected = { sel = it },
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // ---------- color picker ----------
                item {
                    Spacer(Modifier.height(8.dp))
                    ColorPicker(
                        selectedColorLabel = selectedColor,
                        onColorSelected = { selectedColor = it } // << Bạn đã truyền đúng callback này
                    )

                    LaunchedEffect(selectedColor) {
                        println("Selected color is: $selectedColor")
                    }

                    Spacer(Modifier.height(32.dp))
                }

                // ---------- button ----------
                item {
                    Row {
                        ActionButtonWithFeedback(
                            label = "Huỷ bỏ",
                            style = HCButtonStyle.SECONDARY,
                            onAction = { onS, _ ->
                                navController.popBackStack()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp, start = 16.dp),
                            snackbarViewModel = snackbarViewModel
                        )
                        ActionButtonWithFeedback(
                            label = "Tạo nhóm",
                            style = HCButtonStyle.PRIMARY,
                            onAction = { ok, err ->
                                viewModel.createGroup(
                                    groupName = groupName,
                                    groupDesc = groupDesc,
                                    iconName  = sel ?: "house",
                                    iconColor = selectedColor,
                                    onSuccess = { msg ->
                                        ok(msg)
                                        snackbarViewModel.showSnackbar(msg)
                                    },
                                    onError = { msg ->
                                        err(msg)
                                        snackbarViewModel.showSnackbar(msg)
                                    }
                                )
                            },
                            onSuccess = {
                                navController.navigate("home") {
                                    popUpTo("create_group") { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 16.dp, start = 8.dp),
                            snackbarViewModel = snackbarViewModel
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "CreateGroupScreen Preview - Phone")
@Composable
fun CreateGroupScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        CreateGroupScreen(navController = rememberNavController())
    }
}