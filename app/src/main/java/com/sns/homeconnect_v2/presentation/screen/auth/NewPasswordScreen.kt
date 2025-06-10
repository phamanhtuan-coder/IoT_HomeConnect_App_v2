package com.sns.homeconnect_v2.presentation.screen.auth

import IoTHomeConnectAppTheme
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.auth.AuthViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.auth.RecoveryPasswordUiState
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

/** Giao diện màn hình Tạo mật khẩu mới (NewPassword Screen)
 * -----------------------------------------
 * Người viết: Phạm Xuân Nhân
 * Ngày viết: 03/12/2024
 * -------------------------
 * Người cập nhật: Phạm Anh Tuấn
 * Lần cập nhật cuối: 12/1/2025
 * -----------------------------------------
 * @param navController: Đối tượng điều khiển điều hướng
 * @param email: Email người dùng*
 * @return Column chứa các thành phần giao diện của màn hình Tạo mật khẩu mới
 */

@Composable
fun NewPasswordScreen(
    email: String,
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel(),
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val passwordErrorState = remember { mutableStateOf("") }
    val passwordConfirmErrorState = remember { mutableStateOf("") }

    when (uiState) {
        is RecoveryPasswordUiState.Success -> {
            LaunchedEffect(Unit) {
                snackbarViewModel.showSnackbar((uiState as RecoveryPasswordUiState.Success).message)
                navController.navigate(Screens.Login.route) {
                    popUpTo(Screens.Login.route) { inclusive = true }
                }
            }
        }
        is RecoveryPasswordUiState.Error -> {
            LaunchedEffect(uiState) {
                snackbarViewModel.showSnackbar((uiState as RecoveryPasswordUiState.Error).message)
            }
        }
        is RecoveryPasswordUiState.Loading -> {
            // Show loading UI nếu muốn
        }
        RecoveryPasswordUiState.Idle -> {}
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp
        val passwordState = remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        val passwordState2 = remember { mutableStateOf("") }
        var passwordVisible2 by remember { mutableStateOf(false) }



        // Xác định xem nếu chúng ta đang ở chế độ ngang
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        //  kích thước phản hồi
        val horizontalPadding = when {
            screenWidthDp < 360.dp -> 8.dp
            screenWidthDp < 600.dp -> 16.dp
            else -> 32.dp
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background),
            containerColor = colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colorScheme.background),
                )
            {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = horizontalPadding)
                            .verticalScroll(rememberScrollState())
                            .heightIn(max = 800.dp)
                            .widthIn(max = 600.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = if (isLandscape) Arrangement.Center else Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tạo khẩu mới",
                            fontSize = if (isTablet(context)) 28.sp else 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Mật khẩu mới của bạn phải khác với mật khẩu đã sử dụng trước đó.",
                            fontSize = 14.sp,
                            color  = colorScheme.onBackground.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        StyledTextField(
                            value = passwordState.value,
                            onValueChange = {
                                passwordState.value = it
                                // Kiểm tra lỗi mật khẩu mới
                                passwordErrorState.value = ValidationUtils.validatePassword(it)
                            },
                            placeholderText = "Mật khẩu mới",
                            leadingIcon = Icons.Default.Password,
                            isPassword = true
                        )
                        // MẬT KHẨU MỚI
                        Spacer(modifier = Modifier.height(8.dp))

                        StyledTextField(
                            value = passwordState2.value,
                            onValueChange = {
                                passwordState2.value = it
                                // Kiểm tra lỗi nhập lại mật khẩu
                                passwordConfirmErrorState.value = ValidationUtils.validateConfirmPassword(
                                    passwordState.value, it
                                )
                            },
                            placeholderText = "Nhập lại mật khẩu mới",
                            leadingIcon = Icons.Default.Password,
                            isPassword = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Nút quay lại màn hình đăng nhập
                        ActionButtonWithFeedback(
                            label = "Đổi mật khẩu",
                            style = HCButtonStyle.PRIMARY,
                            onAction = { _, _ ->
                                viewModel.recoveryPassword(email, passwordState.value)
                            },
                            snackbarViewModel = snackbarViewModel
                        )
                    }
                }
            }
        }
    }
}