package com.sns.homeconnect_v2.presentation.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.auth.CheckEmailUiState
import com.sns.homeconnect_v2.presentation.viewmodel.auth.CheckEmailViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

/**
 * Màn hình Khôi phục mật khẩu
 * ----------------------------
 * Người viết: Nguyễn Thanh Sang
 * Ngày viết: 01/12/2024
 * Lần cập nhật cuối: [Current Date]
 * ----------------------------
 * @param navController: Đối tượng điều khiển điều hướng
 * @param viewModel: ViewModel cho màn hình khôi phục mật khẩu
 * @return Scaffold chứa toàn bộ nội dung màn hình khôi phục mật khẩu
 * ----------------------------
 * Người cập nhật: [Your Name]
 * Ngày cập nhật: [Current Date]
 * Nội dung cập nhật:
 * - Chuyển sang clean architecture với RecoverPasswordUseCase
 * - Sử dụng RecoverPasswordUiModel cho trạng thái UI
 * - Đồng bộ với RegisterScreen và LoginScreen
 */

@Composable
fun RecoverPasswordScreen(
    navController: NavHostController,
    viewModel: CheckEmailViewModel = hiltViewModel(),
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    val emailState = remember { mutableStateOf("") }
    val emailErrorState = remember { mutableStateOf("") }
    val isTablet = com.google.android.gms.common.util.DeviceProperties.isTablet(context)
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is CheckEmailUiState.Idle -> { /* ... */ }
        is CheckEmailUiState.Loading -> CircularProgressIndicator()
        is CheckEmailUiState.Error -> Text((uiState as CheckEmailUiState.Error).message, color = MaterialTheme.colorScheme.error)
        is CheckEmailUiState.Success -> {
            LaunchedEffect(Unit) {
                snackbarViewModel.showSnackbar("Email hợp lệ, chuyển sang OTP")
                navController.navigate(Screens.OTP.createRoute("reset_password", emailState.value))
            }
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
        containerColor = colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = if (isTablet) 32.dp else 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = "Khôi phục mật khẩu",
                fontSize = if (isTablet) 28.sp else 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary
            )
            Text(
                text = "Vui lòng nhập email để nhận liên kết khôi phục.",
                fontSize = 14.sp,
                color = colorScheme.onBackground.copy(alpha = 0.6f)
            )

            StyledTextField(
                value = emailState.value,
                onValueChange = {
                    emailState.value = it
                    emailErrorState.value = ValidationUtils.validateEmail(it)
                },
                placeholderText = "Nhập email",
                leadingIcon = Icons.Default.Email
            )

            ActionButtonWithFeedback(
                label = "Xác nhận email",
                style = if (emailErrorState.value.isEmpty() && emailState.value.isNotBlank())
                    HCButtonStyle.PRIMARY else HCButtonStyle.DISABLED,
                snackbarViewModel = snackbarViewModel,
                onAction = { ok, err ->
                    viewModel.checkEmail(emailState.value, ok, err)
                },
                onSuccess = {
                    navController.navigate(
                        Screens.OTP.createRoute("reset_password", emailState.value)
                    )
                }
            )


            TextButton(onClick = { navController.popBackStack() }) {
                Text(
                    text = "Quay lại màn hình đăng nhập",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
            }
        }
    }
}

