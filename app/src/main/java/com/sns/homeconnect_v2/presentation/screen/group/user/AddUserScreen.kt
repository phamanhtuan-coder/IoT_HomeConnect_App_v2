package com.sns.homeconnect_v2.presentation.screen.group.user

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Room
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.GenericDropdown
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.SearchBar
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.viewmodel.group.user.AddUserUiState
import com.sns.homeconnect_v2.presentation.viewmodel.group.user.AddUserViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

/**
 * Hàm Composable đại diện cho màn hình "Thêm người dùng".
 * Màn hình này cho phép người dùng tìm kiếm và thêm người dùng mới vào một nhóm,
 * gán vai trò cho họ và điều hướng đến các phần khác của ứng dụng.
 *
 * Màn hình bao gồm:
 * - Thanh ứng dụng trên cùng với nút quay lại và tiêu đề.
 * - Thanh tìm kiếm để tìm người dùng.
 * - Một phần để hiển thị người dùng đang được thêm (hiện tại là một trình giữ chỗ).
 * - Menu thả xuống để chọn vai trò cho người dùng mới (Chủ sở hữu, Phó, Quản trị viên, Thành viên).
 * - Nút "Thêm" để xác nhận việc thêm người dùng.
 * - Thanh điều hướng dưới cùng để điều hướng đến các màn hình khác như Bảng điều khiển, Thiết bị, Trang chủ, Hồ sơ và Cài đặt.
 *
 * @param navController [NavHostController] được sử dụng để điều hướng trong ứng dụng.
 * @author Nguyễn Thanh Sang
 * @since 19-05-2025
 */

@Composable
fun AddUserScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
    viewModel: AddUserViewModel = hiltViewModel(),
    groupId: Int
) {
    val roleState = remember { mutableStateOf("") }
    val searchState = remember { mutableStateOf("") }
    var accountId by remember { mutableStateOf("") }
    val list = listOf("Vice", "Admin", "Member")


    val isTablet = isTablet(LocalContext.current)

    val addUserUiState by viewModel.addUserState.collectAsState()

    LaunchedEffect(addUserUiState) {
        when (val state = addUserUiState) {
            is AddUserUiState.Success -> {
                snackbarViewModel.showSnackbar("Thêm thành viên thành công!", SnackbarVariant.SUCCESS)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("refresh", true)

                viewModel.resetAddUserState()
                navController.navigateUp()
            }
            is AddUserUiState.Error -> {
                snackbarViewModel.showSnackbar(state.message, SnackbarVariant.ERROR)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("refresh", true)

                viewModel.resetAddUserState()
                navController.navigateUp()
            }
            else -> Unit
        }
    }


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
                MenuBottom(navController)
            }
        ) { inner ->
            Column(
                modifier = Modifier.padding(inner)
            ) {
                ColoredCornerBox(
                    cornerRadius = 40.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        SearchBar(
                            modifier = Modifier.fillMaxWidth(),
                            onSearch = { query ->
                                searchState.value = query
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
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Comment out searchedUsers display temporarily
                    /*
                    searchedUsers.forEach { (name, avatar) ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFD8E4E8),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SimpleUserCard(
                                userName = name,
                                avatarUrl = avatar,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                    */

                    // Add TextField for accountId
                    OutlinedTextField(
                        value = accountId,
                        onValueChange = { accountId = it },
                        label = { Text("Account ID") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )

                    Spacer(Modifier.height(8.dp))

                    GenericDropdown(
                        items = list,
                        selectedItem = roleState.value,
                        onItemSelected = { selectedRole ->
                            roleState.value = selectedRole
                        },
                        isTablet = isTablet,
                        leadingIcon = Icons.Default.Room
                    )


                    Spacer(Modifier.height(8.dp))

                    ActionButtonWithFeedback(
                        label = "Thêm",
                        style = HCButtonStyle.PRIMARY,
                        snackbarViewModel = snackbarViewModel,
                        isLoadingFromParent = addUserUiState is AddUserUiState.Loading,
                        onAction = { _, onError ->
                            if (accountId.isBlank()) {
                                onError("Vui lòng nhập Account ID")
                            } else if (roleState.value.isBlank()) {
                                onError("Vui lòng chọn vai trò")
                            } else {
                                viewModel.addMemberToGroup(accountId, roleState.value)
                            }
                        }
                    )
                }
            }
        }
    }
}
