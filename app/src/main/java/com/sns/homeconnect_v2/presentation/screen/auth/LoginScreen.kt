package com.sns.homeconnect_v2.presentation.screen.login

import IoTHomeConnectAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.presentation.viewmodel.auth.LoginUiState
import com.sns.homeconnect_v2.presentation.viewmodel.auth.LoginViewModel
import com.sns.homeconnect_v2.presentation.navigation.Screens

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    IoTHomeConnectAppTheme {
        val configuration = LocalConfiguration.current
        val isTablet = configuration.screenWidthDp >= 600
        val colorScheme = MaterialTheme.colorScheme
        val emailState = remember { mutableStateOf("0306221391@caothang.edu.vn") }
        val passwordState = remember { mutableStateOf("Tu@n1234") }
        var passwordVisible by remember { mutableStateOf(false) }
        val emailErrorState = remember { mutableStateOf("") }
        val passwordErrorState = remember { mutableStateOf("") }
        val loginUiState by viewModel.loginState.collectAsState()

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

                OutlinedTextField(
                    shape = RoundedCornerShape(25),
                    singleLine = true,
                    value = emailState.value,
                    onValueChange = {
                        emailState.value = it
                        emailErrorState.value = ValidationUtils.validateEmail(it)
                    },
                    placeholder = { Text("Nhập email của bạn") },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .width(if (isTablet) 500.dp else 400.dp)
                        .height(if (isTablet) 80.dp else 70.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = colorScheme.onBackground,
                        unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),
                        focusedContainerColor = colorScheme.onPrimary,
                        unfocusedContainerColor = colorScheme.onPrimary,
                        focusedIndicatorColor = colorScheme.primary,
                        unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                )

                OutlinedTextField(
                    shape = RoundedCornerShape(25),
                    singleLine = true,
                    value = passwordState.value,
                    onValueChange = {
                        passwordState.value = it
                        passwordErrorState.value = ValidationUtils.validatePassword(it)
                    },
                    placeholder = { Text("Mật khẩu") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .width(if (isTablet) 500.dp else 400.dp)
                        .height(if (isTablet) 80.dp else 70.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = colorScheme.onBackground,
                        unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),
                        focusedContainerColor = colorScheme.onPrimary,
                        unfocusedContainerColor = colorScheme.onPrimary,
                        focusedIndicatorColor = colorScheme.primary,
                        unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                )

                Row(
                    modifier = Modifier
                        .width(if (isTablet) 500.dp else 400.dp)
                        .height(if (isTablet) 80.dp else 70.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Screens.RecoverPassword.route) }
                    ) {
                        Text(
                            text = "Quên mật khẩu?",
                            fontSize = 14.sp,
                            color = colorScheme.primary
                        )
                    }
                }

                Button(
                    onClick = { viewModel.login(emailState.value, passwordState.value) },
                    modifier = Modifier
                        .width(if (isTablet) 300.dp else 200.dp)
                        .height(if (isTablet) 56.dp else 48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Đăng nhập",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onPrimary
                    )
                }

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