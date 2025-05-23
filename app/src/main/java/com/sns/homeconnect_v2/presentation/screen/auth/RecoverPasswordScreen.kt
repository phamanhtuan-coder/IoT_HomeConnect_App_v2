package com.sns.homeconnect_v2.presentation.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.auth.RecoverPasswordState
import com.sns.homeconnect_v2.presentation.viewmodel.auth.RecoverPasswordViewModel

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
    viewModel: RecoverPasswordViewModel = hiltViewModel()
) {
    val recoverPasswordState by viewModel.checkEmailState.collectAsState()
    val uiModel by viewModel.uiModel.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val context =  LocalContext.current

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
                .padding(horizontal = if (isTablet(context)) 32.dp else 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = "Khôi phục mật khẩu",
                fontSize = if (isTablet(context)) 28.sp else 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary
            )
            Text(
                text = "Vui lòng nhập email để nhận liên kết khôi phục.",
                fontSize = 14.sp,
                color = colorScheme.onBackground.copy(alpha = 0.6f)
            )

            OutlinedTextField(
                value = uiModel.email,
                onValueChange = {
                    viewModel.updateUiModel(
                        uiModel.copy(
                            email = it,
                            emailError = ValidationUtils.validateEmail(it),
                            errorMessage = ""
                        )
                    )
                },
                placeholder = { Text("Nhập email của bạn") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .width(if (isTablet(context)) 500.dp else 400.dp)
                    .height(if (isTablet(context)) 80.dp else 70.dp),
                shape = RoundedCornerShape(25),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorScheme.onBackground,
                    unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),
                    focusedContainerColor = colorScheme.onPrimary,
                    unfocusedContainerColor = colorScheme.onPrimary,
                    focusedIndicatorColor = colorScheme.primary,
                    unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                ),
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) }
            )

            Text(
                text = uiModel.emailError,
                fontSize = 14.sp,
                color = if (uiModel.emailError == "Email hợp lệ.") Color.Green else colorScheme.error
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // TODO: Re-enable API call when new API is ready
                    // viewModel.checkEmail()

                    // Direct navigation for demo
                    navController.navigate(
                        Screens.OTP.createRoute("reset_password", uiModel.email)
                    )
                },
                enabled = uiModel.isValid(),
                modifier = Modifier
                    .width(if (isTablet(context)) 300.dp else 200.dp)
                    .height(if (isTablet(context)) 56.dp else 48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Khôi phục mật khẩu",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onPrimary
                )
            }

            // TODO: Re-enable when API is ready
            /*
            when (recoverPasswordState) {
                is RecoverPasswordState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(
                            Screens.OTP.createRoute("reset_password", uiModel.email)
                        )
                    }
                }
                is RecoverPasswordState.Error -> {
                    LaunchedEffect(recoverPasswordState) {
                        viewModel.updateUiModel(
                            uiModel.copy(errorMessage = (recoverPasswordState as RecoverPasswordState.Error).message)
                        )
                    }
                }
                is RecoverPasswordState.Loading -> {
                    CircularProgressIndicator()
                }
                is RecoverPasswordState.Idle -> {
                    // Do nothing
                }
            }
            */

            if (uiModel.errorMessage.isNotBlank()) {
                Text(
                    text = uiModel.errorMessage,
                    fontSize = 14.sp,
                    color = colorScheme.error
                )
            }

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