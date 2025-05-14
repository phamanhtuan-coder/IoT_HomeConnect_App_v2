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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.presentation.component.ColorPicker
import com.sns.homeconnect_v2.presentation.component.IconPicker
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuItem
import com.sns.homeconnect_v2.presentation.component.widget.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateGroupScreen(navController: NavHostController) {
    var groupName by remember { mutableStateOf("") }
    var groupDesc by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // ---------------- icon + color state ----------------
    var selectedLabel by remember { mutableStateOf("Nhà") }

    val iconOptions = listOf(
        Icons.Default.Home      to "Nhà",
        Icons.Default.Work      to "Cơ quan",
        Icons.Default.School    to "Trường",
        Icons.Default.AccountBalance to "Ngân hàng",
        Icons.Default.Apartment     to "Căn hộ",
        Icons.Default.Hotel         to "Khách sạn",
        Icons.Default.Villa         to "Biệt thự",
        Icons.Default.Cottage       to "Nhà gỗ",
        Icons.Default.Castle        to "Lâu đài",
        Icons.Default.LocalLibrary  to "Thư viện"
    )

    var selectedColor by remember { mutableStateOf("blue") }

    val colorOptions = listOf(
        Color.Red to "red",
        Color.Green to "green",
        Color.Blue to "blue",
        Color.Yellow to "yellow",
        Color.Cyan to "cyan",
        Color.Magenta to "magenta",
        Color.Gray to "gray",
        Color.Black to "black",
        Color.White to "white",
        Color(0xFF2196F3) to "customBlue"
    )

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
                        iconOptions = iconOptions,
                        selectedIconLabel = selectedLabel,
                        onIconSelected = { selectedLabel = it }
                    )
                }

                // ---------- color picker ----------
                item {
                    Spacer(Modifier.height(8.dp))
                    ColorPicker(
                        colors = colorOptions,
                        selectedColorLabel = selectedColor,
                        onColorSelected = { selectedColor = it }
                    )
                    Spacer(Modifier.height(32.dp))
                }

                // ---------- button ----------
                item {
                    ActionButtonWithFeedback(
                        label = "Hoàn tất",
                        style = HCButtonStyle.PRIMARY,
                        onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    ActionButtonWithFeedback(
                        label = "Huỷ bỏ",
                        style = HCButtonStyle.SECONDARY,
                        onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
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


