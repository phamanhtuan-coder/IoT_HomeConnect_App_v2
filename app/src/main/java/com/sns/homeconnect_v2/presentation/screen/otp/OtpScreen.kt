package com.sns.homeconnect_v2.presentation.screen.otp

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.otp.OTPState
import com.sns.homeconnect_v2.presentation.viewmodel.otp.OTPViewModel
import kotlinx.coroutines.launch

@Composable
fun OtpScreen(
    navController: NavHostController,
    email: String,
    title: String = "Nhập mã OTP",
    description: String = "Vui lòng nhập mã OTP vừa được gửi tới Email",
    viewModel: OTPViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    var sendOTPState by remember { mutableStateOf<OTPState?>(null) }
    var verifyOTPState by remember { mutableStateOf<OTPState?>(null) }

    // Send OTP when screen launches
    LaunchedEffect(Unit) {
        viewModel.sendOTP(email)
    }

    val otpLength = 6
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        val configuration = LocalConfiguration.current
        val isTablet = configuration.screenWidthDp >= 600
        val otpValue = remember { mutableStateListOf(*Array(otpLength) { "" }) }

        Surface(
            color = colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = if (isTablet) 48.dp else 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Top Section with Title and Description
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        fontSize = if (isTablet) 32.sp else 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Status message
                    when (val state = viewModel.sendOtpState.collectAsState().value) {
                        is OTPState.Success -> {
                            Text(
                                text = state.message,
                                fontSize = if (isTablet) 16.sp else 14.sp,
                                color = Color(0xFF4CAF50),
                                textAlign = TextAlign.Center
                            )
                        }
                        is OTPState.Error -> {
                            Text(
                                text = state.message,
                                fontSize = if (isTablet) 16.sp else 14.sp,
                                color = colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                        else -> {
                            Text(
                                text = description,
                                fontSize = if (isTablet) 16.sp else 14.sp,
                                color = colorScheme.onBackground.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Email display
                    Text(
                        text = email,
                        fontSize = if (isTablet) 18.sp else 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.primary
                    )
                }

                // OTP Input Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(otpLength) { index ->
                            OutlinedTextField(
                                value = otpValue[index],
                                onValueChange = { input ->
                                    if (input.length <= 1 && input.all { it.isDigit() }) {
                                        otpValue[index] = input
                                        if (input.isNotEmpty() && index < otpLength - 1) {
                                            focusRequesters[index + 1].requestFocus()
                                        } else if (input.isEmpty() && index > 0) {
                                            focusRequesters[index - 1].requestFocus()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .size(if (isTablet) 64.dp else 52.dp)
                                    .focusRequester(focusRequesters[index]),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = colorScheme.onBackground,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isTablet) 24.sp else 20.sp
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = if (index == otpLength - 1) ImeAction.Done else ImeAction.Next
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = colorScheme.primary,
                                    unfocusedTextColor = colorScheme.onBackground,
                                    focusedContainerColor = colorScheme.surface,
                                    unfocusedContainerColor = colorScheme.surface,
                                    focusedIndicatorColor = colorScheme.primary,
                                    unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }

                    // Verify Button
                    ActionButtonWithFeedback(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (isTablet) 56.dp else 48.dp),
                        label = "Xác nhận",
                        style = HCButtonStyle.PRIMARY,
                        textSize = if (isTablet) 18.sp else 16.sp,
                        onSuccess = {
                            navController.navigate("home_graph") {
                                popUpTo(Screens.Login.route) { inclusive = true }
                            }
                        },
                        onAction = { ok, err ->
                            val otp = otpValue.joinToString("")
                            if (otp.length != otpLength) {
                                err("Vui lòng nhập đủ $otpLength ký tự OTP!")
                                return@ActionButtonWithFeedback
                            }

                            // KHÔNG CẦN coroutineScope.launch
                            try {
                                val result = viewModel.verifyOTPAndReturnResult(email, otp)
                                result.fold(
                                    onSuccess = { ok("Xác thực OTP thành công!") },
                                    onFailure = { e -> err(e.message ?: "Xác thực OTP thất bại!") }
                                )
                            } catch (e: Exception) {
                                err("Lỗi không xác định: ${e.message}")
                            }
                        }
                    )

                    // Divider with "hoặc"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.width(80.dp),
                            thickness = 1.dp, color = colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                        Text(
                            text = "hoặc",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = colorScheme.onBackground.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                        HorizontalDivider(
                            modifier = Modifier.width(80.dp),
                            thickness = 1.dp, color = colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                    }

                    // Resend Button
                    TextButton(
                        onClick = {
                            otpValue.forEachIndexed { index, _ -> otpValue[index] = "" }
                            focusRequesters[0].requestFocus()
                            viewModel.sendOTP(email)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = colorScheme.primary
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Gửi lại mã OTP",
                            fontSize = if (isTablet) 16.sp else 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview
@Composable
fun OtpScreenPreview() {
    val navController = rememberNavController()
    OtpScreen(
        navController = navController,
        email = "example@email.com"
    )
}

