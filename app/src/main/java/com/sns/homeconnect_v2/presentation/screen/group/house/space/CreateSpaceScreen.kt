package com.sns.homeconnect_v2.presentation.screen.group.house.space

import IoTHomeConnectAppTheme
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
import com.sns.homeconnect_v2.presentation.component.widget.ColorPicker
import com.sns.homeconnect_v2.presentation.component.widget.IconPicker
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuItem
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateGroupScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
) {
    var spaceName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<String?>(null) }
    var selectedIconLabel by remember { mutableStateOf("Nhà") }
    var selectedColorLabel by remember { mutableStateOf("blue") }

    val roles = listOf("Owner", "Vice", "Admin", "Member")
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
                                    onValueChange = { spaceName = it },
                                    placeholderText = "Nhập tên space",
                                    leadingIcon = Icons.Default.People,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                GenericDropdown(
                                    items = roles,
                                    selectedItem = selectedRole,
                                    onItemSelected = { selectedRole = it },
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    placeHolder = "Chọn nhà",
                                    isTablet = isTablet,
                                    leadingIcon = Icons.Default.Room
                                )
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
                        selectedIconLabel = selectedIconLabel,
                        onIconSelected = { selectedIconLabel = it }
                    )
                }

                item {
                    ColorPicker(
                        selectedColorLabel = selectedColorLabel,
                        onColorSelected = { selectedColorLabel = it }
                    )
                    Spacer(Modifier.height(32.dp))
                }

                item {
                    ActionButtonWithFeedback(
                        label = "Hoàn tất",
                        style = HCButtonStyle.PRIMARY,
                        onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        snackbarViewModel = snackbarViewModel
                    )
                    Spacer(Modifier.height(8.dp))
                    ActionButtonWithFeedback(
                        label = "Huỷ bỏ",
                        style = HCButtonStyle.SECONDARY,
                        onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        snackbarViewModel = snackbarViewModel
                    )
                }
            }
        }
    }
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
        CreateGroupScreen(
            navController = rememberNavController(),
        )
    }
}