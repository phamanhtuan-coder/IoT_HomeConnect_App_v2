package com.sns.homeconnect_v2.presentation.screen.auth

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.auth.LoginUiState
import com.sns.homeconnect_v2.presentation.viewmodel.auth.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    IoTHomeConnectAppTheme {
        val configuration = LocalConfiguration.current
        val isTablet = configuration.screenWidthDp >= 600
        val colorScheme = MaterialTheme.colorScheme
        val emailState = remember { mutableStateOf("khangdev") }
        val passwordState = remember { mutableStateOf("Dev!123456") }
//        var passwordVisible by remember { mutableStateOf(false) }
        val emailErrorState = remember { mutableStateOf("") }
        val passwordErrorState = remember { mutableStateOf("") }
        val loginUiState by viewModel.loginState.collectAsState()
//        val isLoading = remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.fillMaxSize().background(colorScheme.background),
            containerColor = colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = if (isTablet) 32.dp else 16.dp)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Đăng nhập",
                    fontSize = if (isTablet) 28.sp else 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
                Text(
                    text = "Hãy đăng nhập để tiếp tục",
                    fontSize = 14.sp,
                    color = colorScheme.onBackground.copy(alpha = 0.6f)
                )

                StyledTextField(
                    value = emailState.value,
                    onValueChange = {
                        emailState.value = it
                        emailErrorState.value = ValidationUtils.validateEmail(it)
                    },
                    placeholderText = "Mật khẩu",
                    leadingIcon = Icons.Default.Email
                )

                StyledTextField(
                    value = passwordState.value,
                    onValueChange = {
                        passwordState.value = it
                        passwordErrorState.value = ValidationUtils.validatePassword(it)
                    },
                    placeholderText = "Mật khẩu",
                    leadingIcon = Icons.Default.Password,
                    isPassword = true
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(), // Bắt buộc phải dùng fillMaxWidth
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            /* TODO */
                            navController.navigate(Screens.RecoverPassword.route)
                        },
                        contentPadding = PaddingValues(0.dp) // Không bị thụt vào trong
                    ) {
                        Text(
                            text = "Quên mật khẩu?",
                            color = Color(0xFF2979FF), // Hoặc colorScheme.primary
                            fontWeight = FontWeight.Medium
                        )
                    }
                }


                ActionButtonWithFeedback(
                    label = "Đăng nhập",
                    style = HCButtonStyle.PRIMARY,
                    onSuccess = {          // chạy SAU khi user bấm OK dialog thành công
                        navController.navigate("home_graph") {
                            popUpTo(Screens.Login.route) { inclusive = true }
                        }
                    },
                    onAction = { ok, err ->
                        val result = viewModel.quickLogin(emailState.value, passwordState.value)   // suspend
                        result.fold(
                            onSuccess = { ok("Đăng nhập thành công!") },
                            onFailure = { e -> err(e.message ?: "Sai tài khoản hoặc mật khẩu!") }
                        )
                    }
                )
                // Comment out the UI state handling since we're bypassing API calls
                // TODO: Re-enable when API is ready
                /*
                when (loginUiState) {
                    is LoginUiState.Idle -> {}
                    is LoginUiState.Loading -> {}
                    is LoginUiState.Success -> {
                        LaunchedEffect(Unit) {
                            navController.navigate("home_graph") {
                                popUpTo(Screens.Welcome.route) { inclusive = true }
                            }
                        }
                    }
                    is LoginUiState.Error -> {
                        Text(
                            text = (loginUiState as LoginUiState.Error).message,
                            color = colorScheme.error
                        )
                    }
                }
                */

                Row(
                    modifier = Modifier.fillMaxWidth(if (isTablet) 0.8f else 0.9f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chưa có tài khoản?",
                        fontSize = 14.sp,
                        color = colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = { navController.navigate(Screens.Register.route) }) {
                        Text(
                            text = "Đăng ký",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}