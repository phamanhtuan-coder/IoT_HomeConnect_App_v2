package com.sns.homeconnect_v2.presentation.screen.group.house

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
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
fun CreateHouseScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    var houseName by remember { mutableStateOf("") }
    var houseAddress by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    var selectedLabel by remember { mutableStateOf("Nhà") }

    var selectedColor by remember { mutableStateOf("blue") }

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
                                isTablet = isTablet
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
                                    value = houseName,
                                    onValueChange = { houseName = it },
                                    placeholderText = "Nhập tên nhà",
                                    leadingIcon = Icons.Default.Home,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                StyledTextField(
                                    value = houseAddress,
                                    onValueChange = { houseAddress = it },
                                    placeholderText = "Nhập địa chỉ nhà",
                                    leadingIcon = Icons.Default.LocationOn,
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

                item {
                    IconPicker(
                        selectedIconLabel = selectedLabel,
                        onIconSelected = { selectedLabel = it }
                    )
                }

                item {
                    Spacer(Modifier.height(8.dp))
                    ColorPicker(
                        selectedColorLabel = selectedColor,
                        onColorSelected = { selectedColor = it }
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

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "CreateHouseScreen Preview - Phone")
@Composable
fun CreateHouseScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        CreateHouseScreen(navController = rememberNavController())
    }
}