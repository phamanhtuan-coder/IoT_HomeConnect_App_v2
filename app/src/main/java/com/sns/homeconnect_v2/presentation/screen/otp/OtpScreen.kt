package com.sns.homeconnect_v2.presentation.screen.otp

import IoTHomeConnectAppTheme
import android.net.Uri
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
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.viewmodel.otp.OTPState
import com.sns.homeconnect_v2.presentation.viewmodel.otp.OTPViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant
import com.sns.homeconnect_v2.presentation.navigation.Screens

@Composable
fun OtpScreen(
    email: String,
    title: String = "Nhập mã OTP",
    description: String = "Vui lòng nhập mã OTP vừa được gửi tới Email",
    onVerificationSuccess: () -> Unit,
    navController: NavHostController,
    viewModel: OTPViewModel = hiltViewModel(),
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    val colorScheme = MaterialTheme.colorScheme
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    val otpLength = 6
    val otpValue = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = List(otpLength) { FocusRequester() }

    val verifyOTPState by viewModel.verifyOtpState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendOTP(email)
    }

    LaunchedEffect(verifyOTPState) {
        when (verifyOTPState) {
            is OTPState.Success -> {
                snackbarViewModel.showSnackbar(
                    (verifyOTPState as OTPState.Success).message,
                    SnackbarVariant.SUCCESS
                )
                onVerificationSuccess()
            }
            is OTPState.Error -> {
                snackbarViewModel.showSnackbar(
                    (verifyOTPState as OTPState.Error).message,
                    SnackbarVariant.ERROR
                )
            }
            else -> Unit
        }
    }

    IoTHomeConnectAppTheme {
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
                    text = "Mã OTP đã được gửi tới Email của bạn.",
                    fontSize = 14.sp,
                    color = Color.Green
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
                        viewModel.sendOTP(email)
                        otpValue.indices.forEach { otpValue[it] = "" }
                        focusRequesters[0].requestFocus()
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

                Spacer(modifier = Modifier.height(16.dp))

                ActionButtonWithFeedback(
                    label = "Xác nhận",
                    style = HCButtonStyle.PRIMARY,
                    snackbarViewModel = snackbarViewModel,
                    onAction = { _, _ ->
                        val otpString = otpValue.joinToString("")
                        if (otpString.length == otpLength) {
                            viewModel.verifyOTP(email, otpString)
                        } else {
                            snackbarViewModel.showSnackbar("Vui lòng nhập đủ mã OTP", SnackbarVariant.ERROR)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp),
                    height = if (isTablet) 56.dp else 48.dp,
                    width = if (isTablet) 300.dp else 200.dp,
                    textSize = 16.sp
                )
            }
        }
    }
}

