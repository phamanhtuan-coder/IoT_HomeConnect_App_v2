package com.sns.homeconnect_v2.presentation.screen.group.house.space

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.core.util.validation.parseColorOrDefault
import com.sns.homeconnect_v2.data.remote.dto.base.GroupIconCategory
import com.sns.homeconnect_v2.presentation.component.widget.ColorPicker
import com.sns.homeconnect_v2.presentation.component.widget.IconPicker
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuItem
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.space.CreateSpaceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateSpaceScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
    houseId: Int
) {
    val viewModel: CreateSpaceViewModel = hiltViewModel()
    val spaceName by viewModel.spaceName.collectAsState()
    val iconColor by viewModel.iconColor.collectAsState()
    val iconName by viewModel.iconName.collectAsState()
    val spaceDescription by viewModel.spaceDescription.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    var sel by remember { mutableStateOf<String?>(null) }
    var selectedIconLabel by remember { mutableStateOf("Nhà") }
    var selectedColorLabel by remember { mutableStateOf("blue") }

    // Giữ GenericDropdown nhưng không dùng logic vai trò
    var selectedRole by remember { mutableStateOf<String?>(null) }
    val roles = listOf("Owner", "Vice", "Admin", "Member")

    // Giữ BottomAppBar
    val items = listOf(
        "Dashboard" to Pair(Icons.Filled.PieChart, "dashboard"),
        "Devices" to Pair(Icons.Filled.Devices, "devices"),
        "Home" to Pair(Icons.Filled.Home, "home"),
        "Profile" to Pair(Icons.Filled.Person, "profile"),
        "Settings" to Pair(Icons.Filled.Settings, "settings")
    )

    val context = LocalContext.current
    val isTablet = isTablet(context)
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()

    // Xử lý lỗi
    LaunchedEffect(error) {
        error?.let {
            snackbarViewModel.showSnackbar(
                msg = it,
                variant = SnackbarVariant.ERROR
            )
            viewModel.clearError()
        }
    }

    // Điều hướng về sau khi tạo thành công
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            snackbarViewModel.showSnackbar(
                msg = "Tạo không gian thành công",
                variant = SnackbarVariant.SUCCESS
            )
            viewModel.resetSuccess()
            navController.popBackStack() // Quay lại HouseDetailScreen
        }
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Tạo Không Gian"
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
                    ColoredCornerBox(cornerRadius = 40.dp) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .height(IntrinsicSize.Min)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                StyledTextField(
                                    value = spaceName,
                                    onValueChange = { viewModel.updateSpaceName(it) },
                                    placeholderText = "Nhập tên không gian",
                                    leadingIcon = Icons.Default.Home,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    //enabled = !isLoading
                                )
                                Log.d("CreateSpaceScreen", "spaceName: $spaceName")
                                Spacer(Modifier.height(8.dp))
                                StyledTextField(
                                    value = spaceDescription,
                                    onValueChange = { viewModel.updateSpaceDescription(it) },
                                    placeholderText = "Nhập mô tả (tùy chọn)",
                                    leadingIcon = Icons.Default.Description,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    //enabled = !isLoading
                                )
                                Log.d("CreateSpaceScreen", "spaceDescription: $spaceDescription")
//                                Spacer(Modifier.height(8.dp))
//                                GenericDropdown(
//                                    items = roles,
//                                    selectedItem = selectedRole,
//                                    onItemSelected = { selectedRole = it },
//                                    modifier = Modifier.padding(horizontal = 16.dp),
//                                    placeHolder = "Chọn nhà",
//                                    isTablet = isTablet,
//                                    leadingIcon = Icons.Default.Room,
//                                    //enabled = !isLoading
//                                )
                            }
                        }
                    }
                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ) {}
                }

                item {
                    IconPicker(
                        category = GroupIconCategory.SPACE,
                        selectedIconName = sel,
                        onIconSelected = { sel = it },
                        modifier = Modifier.padding(16.dp)
                    )
                    viewModel .updateIconName(sel ?: "home") // Cập nhật iconName nếu sel không null
                    Log.d("CreateSpaceScreen", "iconName: $iconName")
                }

                item {
                    ColorPicker(
                        selectedColorLabel =  iconColor,
                        onColorSelected = { viewModel.updateIconColor(it) },
                        //enabled = !isLoading
                    )
                    Log.d("CreateSpaceScreen", "iconColor: $iconColor")
                    Spacer(Modifier.height(32.dp))
                }

                item {
                    ActionButtonWithFeedback(
                        label = "Hoàn tất",
                        style = HCButtonStyle.PRIMARY,
                        onAction = { onS, _ ->
                            scope.launch {
                                viewModel.createSpace(houseId)
                                onS("Done")
                            }
                        },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        snackbarViewModel = snackbarViewModel,
                        //enabled = !isLoading
                    )
                    Log.d("CreateSpaceScreen", "Creating space with houseId: $houseId")
                    Spacer(Modifier.height(8.dp))
                    ActionButtonWithFeedback(
                        label = "Huỷ bỏ",
                        style = HCButtonStyle.SECONDARY,
                        onAction = { onS, _ ->
                            scope.launch {
                                navController.popBackStack()
                                onS("Done")
                            }
                        },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        snackbarViewModel = snackbarViewModel,
                        //enabled = !isLoading
                    )
                }
            }
        }
    }
    Log.d("CreateSpaceScreen", "$error")
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    name = "CreateGroupScreen Preview - Phone"
)
@Composable
fun CreateGroupScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        CreateSpaceScreen(
            navController = rememberNavController(),
            snackbarViewModel = hiltViewModel(),
            houseId = 1
        )
    }
}