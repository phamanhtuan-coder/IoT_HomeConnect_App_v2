package com.sns.homeconnect_v2.presentation.screen.otp

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.domain.usecase.otp.OTPState
import com.sns.homeconnect_v2.domain.usecase.otp.OTPViewModel
import com.sns.homeconnect_v2.domain.usecase.otp.VerifyEmailState

@Composable
fun OtpScreen(
    navController: NavHostController,
    email: String,
    title: String = "Nhập mã OTP",
    description: String = "Vui lòng nhập mã OTP vừa được gửi tới Email",
    onVerificationSuccess: () -> Unit,
    viewModel: OTPViewModel = hiltViewModel()
) {
    val sendOTPState by viewModel.sendOtpState.collectAsState()
    val verifyOTPState by viewModel.verifyOtpState.collectAsState()
    val verifyEmailState by viewModel.verifyEmailState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendOTP(email)
    }

    LaunchedEffect(verifyOTPState, verifyEmailState) {
        when {
            verifyOTPState is OTPState.Success && title != "Xác thực Email" -> onVerificationSuccess()
            verifyEmailState is VerifyEmailState.Success -> onVerificationSuccess()
        }
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        val configuration = LocalConfiguration.current
        val isTablet = configuration.screenWidthDp >= 600
        val otpLength = 6
        val otpValue = remember { mutableStateListOf(*Array(otpLength) { "" }) }
        val focusRequesters = List(otpLength) { FocusRequester() }

        Scaffold(
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
                    text = title,
                    fontSize = if (isTablet) 28.sp else 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )

                Text(
                    text = when (sendOTPState) {
                        is OTPState.Success -> "Mã OTP đã được gửi tới Email của bạn."
                        is OTPState.Error -> (sendOTPState as OTPState.Error).message
                        is OTPState.Loading -> "Đang gửi mã OTP..."
                        else -> ""
                    },
                    fontSize = 14.sp,
                    color = when (sendOTPState) {
                        is OTPState.Success -> Color.Green
                        is OTPState.Loading -> Color.Yellow
                        else -> colorScheme.error
                    }
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = colorScheme.onBackground.copy(alpha = 0.6f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                                .size(if (isTablet) 60.dp else 50.dp)
                                .focusRequester(focusRequesters[index]),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = if (index == otpLength - 1) ImeAction.Done else ImeAction.Next
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = colorScheme.onBackground,
                                unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),
                                focusedContainerColor = colorScheme.onPrimary,
                                unfocusedContainerColor = colorScheme.onPrimary,
                                focusedIndicatorColor = colorScheme.primary,
                                unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                TextButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 16.dp),
                    onClick = {
                        otpValue.forEachIndexed { index, _ -> otpValue[index] = "" }
                        focusRequesters[0].requestFocus()
                        viewModel.sendOTP(email)
                    }
                ) {
                    Text(
                        text = "Gửi lại",
                        fontSize = 14.sp,
                        color = colorScheme.primary
                    )
                }

                LaunchedEffect(Unit) {
                    focusRequesters[0].requestFocus()
                }

                Text(
                    text = when {
                        verifyOTPState is OTPState.Error -> (verifyOTPState as OTPState.Error).message
                        verifyEmailState is VerifyEmailState.Error -> (verifyEmailState as VerifyEmailState.Error).message
                        verifyOTPState is OTPState.Loading || verifyEmailState is VerifyEmailState.Loading -> "Đang xác thực..."
                        else -> ""
                    },
                    fontSize = 14.sp,
                    color = when {
                        verifyOTPState is OTPState.Loading || verifyEmailState is VerifyEmailState.Loading -> Color.Yellow
                        else -> colorScheme.error
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (title == "Xác thực Email") {
                            viewModel.confirmEmail(email)
                        } else {
                            viewModel.verifyOTP(email, otpValue.joinToString(""))
                        }
                    },
                    modifier = Modifier.size(
                        width = if (isTablet) 300.dp else 200.dp,
                        height = if (isTablet) 56.dp else 48.dp
                    ),
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = "Xác nhận",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onPrimary
                    )
                }
            }
        }
    }
}