package com.sns.homeconnect_v2.presentation.screen.group.house

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.core.util.validation.toColor
import com.sns.homeconnect_v2.core.util.validation.toColorString
import com.sns.homeconnect_v2.core.util.validation.toIcon
import com.sns.homeconnect_v2.core.util.validation.toIconString
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.viewmodel.home.HomeScreenViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

@Composable
fun CreateHouseScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
    groupId: Int
) {
    var houseName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf(Icons.Default.Home) }
    var selectedColor by remember { mutableStateOf(Color.Blue) }

    val createHouseState by viewModel.createHouseState.collectAsState()

    LaunchedEffect(createHouseState) {
        when (createHouseState) {
            is com.sns.homeconnect_v2.presentation.viewmodel.home.CreateHouseState.Success -> {
                // Set refresh flag trước khi pop back
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("refresh", true)
                navController.popBackStack()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            Header(
                navController = navController,
                type = "Back",
                title = "House Settings"
            )
        },
        containerColor = Color.White,
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            MenuBottom(navController)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
//            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                ColoredCornerBox(
                    cornerRadius = 40.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .height(IntrinsicSize.Min)
                            .fillMaxWidth()
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
                                value = address,
                                onValueChange = { address = it },
                                placeholderText = "Địa chỉ nhà",
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
                Spacer(Modifier.height(16.dp))
                IconPicker(
                    selectedIconLabel = selectedIcon.toIconString(),
                    onIconSelected = { iconName ->
                        selectedIcon = iconName.toIcon()
                    }
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
                ColorPicker(
                    selectedColorLabel = selectedColor.toColorString(),
                    onColorSelected = { colorName ->
                        selectedColor = colorName.toColor()
                    }
                )
            }

            item {
                Spacer(Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ActionButtonWithFeedback(
                        label = "Huỷ bỏ",
                        style = HCButtonStyle.SECONDARY,
                        onAction = { _, _ ->
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        snackbarViewModel = snackbarViewModel
                    )

                    ActionButtonWithFeedback(
                        label = "Tạo nhà",
                        style = HCButtonStyle.PRIMARY,
                        onAction = { onSuccess, onError ->
                            if (houseName.isBlank()) {
                                onError("Vui lòng nhập tên nhà")
                                return@ActionButtonWithFeedback
                            }
                            if (address.isBlank()) {
                                onError("Vui lòng nhập địa chỉ")
                                return@ActionButtonWithFeedback
                            }

                            viewModel.createHouse(
                                groupId = groupId,
                                houseName = houseName,
                                address = address,
                                iconName = selectedIcon.toIconString(),
                                iconColor = selectedColor.toColorString()
                            )
                            onSuccess("Tạo nhà thành công")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        snackbarViewModel = snackbarViewModel
                    )
                }
            }
        }
    }
}
