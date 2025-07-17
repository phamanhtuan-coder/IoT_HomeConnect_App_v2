package com.sns.homeconnect_v2.presentation.screen.setting

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.rememberResponsiveLayoutConfig
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.presentation.component.dialog.WarningDialog
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.profile.InfoProfileState
import com.sns.homeconnect_v2.presentation.viewmodel.profile.ProfileScreenViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.profile.ProfileState
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

/** Giao diện màn hình Settings Screen (SettingsScreen)
 * -----------------------------------------
 * Người viết: Nguyễn Thanh Sang
 * Ngày viết: 31/12/2024
 * Lần cập nhật cuối: 31/12/2024
 * -----------------------------------------
 * @param navController Đối tượng điều khiển điều hướng.
 * @return Scaffold chứa toàn bộ nội dung
 */
@Composable
fun SettingsScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel,
    viewModel:     ProfileScreenViewModel  = hiltViewModel(),
) {
    var actionType by remember { mutableStateOf("SINGLE") }

    val layoutConfig = rememberResponsiveLayoutConfig() // Lấy LayoutConfig

    var showAlertDialog by remember { mutableStateOf(false) }

    var profileId by remember { mutableIntStateOf(-1) } // Lắng nghe danh sách thiết bị
    val infoProfileState by viewModel.infoProfileState.collectAsState()

    val logoutState by viewModel.logoutState.collectAsState()

    LaunchedEffect(logoutState) {
        when (logoutState) {
            is ProfileState.Success -> {
                snackbarViewModel.showSnackbar("Đăng xuất thành công", SnackbarVariant.SUCCESS)
                // Quay về màn hình đăng nhập hoặc welcome
                navController.navigate(Screens.Welcome.route) {
                    popUpTo(0) // Xóa toàn bộ stack để không back lại được
                }
            }
            is ProfileState.Error -> {
                snackbarViewModel.showSnackbar("Đăng xuất thất bại", SnackbarVariant.ERROR)
                Log.e("Logout", (logoutState as ProfileState.Error).message)
            }
            else -> {}
        }
    }

    when(infoProfileState){
        is InfoProfileState.Error ->{
            Log.d("Error Profile",  (infoProfileState as InfoProfileState.Error).error)
        }
        is InfoProfileState.Success -> {
            val successState = infoProfileState as InfoProfileState.Success
            profileId = successState.user.UserID
            Log.d("Thành công", "Dữ liệu user: $profileId")
        }
        else -> {
            /* Do nothing */
        }
    }

    LaunchedEffect(1) {
        viewModel.getInfoProfile()
    }

    IoTHomeConnectAppTheme {
        // Trạng thái lưu trữ card được chọn
        val colorScheme = MaterialTheme.colorScheme
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = colorScheme.background,
            topBar = {
                Header(navController, "Back", "Các cài đặt")
            },
            bottomBar = {
                MenuBottom(navController)
            },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize() // Chiếm toàn bộ kích thước của màn hình
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Top, // Sắp xếp các item theo chiều dọc, bắt đầu từ trên xuống.
                    horizontalAlignment = Alignment.CenterHorizontally // Căn chỉnh các item theo chiều ngang vào giữa.
                ) {
                    // Tiêu đề
                    item {
                        // Cột chứa các phần tử con
                        Column (
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            // Cột chứa văn bản tiêu đề và các TextField
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth() // Chiếm toàn bộ chiều rộng
                                    .padding(
                                        horizontal = layoutConfig.outerPadding, // Padding ngang linh hoạt
                                        vertical = layoutConfig.textFieldSpacing // Padding dọc linh hoạt
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally // Căn giữa các phần tử con theo chiều ngang
                            ) {
                                CardSettings(
                                    icon = Icons.Default.Devices,
                                    title = "Lịch sử hoạt động người dùng",
                                    onClick = {
                                        navController.navigate(Screens.UserActivity.route)
                                    }
                                )
                                CardSettings(
                                    icon = Icons.Default.Password,
                                    title = "Đổi mật khẩu",
                                    onClick = {
                                        //ToDo: Kiểm tra dữ liệu, di chuyển đền màn hình cài đặt password.
                                        navController.navigate("${Screens.UpdatePassword.route}/${profileId}")
                                    }
                                )

                                CardSettings(
                                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                                    title = "Đăng xuất",
                                    onClick = {
                                        actionType = "SINGLE"
                                        showAlertDialog = true
                                    }
                                )

                                CardSettings(
                                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                                    title = "Đăng xuất toàn bộ thiết bị",
                                    onClick = {
                                        actionType = "ALL"
                                        showAlertDialog = true
                                    }
                                )

                            }

                            if (showAlertDialog) {
                                WarningDialog(
                                    title = "Cảnh báo",
                                    text = if (actionType == "ALL")
                                        "Bạn có chắc chắn muốn đăng xuất khỏi toàn bộ thiết bị không?"
                                    else
                                        "Bạn có chắc chắn muốn đăng xuất khỏi thiết bị hiện tại không?",
                                    onConfirm = {
                                        if (actionType == "ALL") {
                                            viewModel.logoutAllDevices()
                                        } else {
                                            viewModel.logout()
                                        }
                                        showAlertDialog = false
                                    },
                                    onDismiss = { showAlertDialog = false }
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun CardSettings(icon: ImageVector, title: String, onClick: () -> Unit) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        val isTablet = isTablet(LocalContext.current)
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .width(if (isTablet) 500.dp else 400.dp)
                .height(if (isTablet) 80.dp else 70.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorScheme.onBackground,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Title
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            // Arrow Icon
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )

        }
    }
}