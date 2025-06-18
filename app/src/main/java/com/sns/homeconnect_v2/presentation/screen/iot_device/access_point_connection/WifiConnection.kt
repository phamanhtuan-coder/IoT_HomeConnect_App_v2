package com.sns.homeconnect_v2.presentation.screen.iot_device.access_point_connection

import IoTHomeConnectAppTheme
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.rememberResponsiveLayoutConfig
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.access_point_connection.WifiConnectionViewModel
import kotlinx.coroutines.launch

@Composable
fun WifiConnectionScreen(
    navController: NavHostController,
    viewModel: WifiConnectionViewModel = hiltViewModel()
) {
    val layoutConfig = rememberResponsiveLayoutConfig()
    val isTablet = isTablet(LocalContext.current)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        val ssidState = remember { mutableStateOf("") }
        val ssidErrorState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }
        val passwordErrorState = remember { mutableStateOf("") }
        val passwordVisible = remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = colorScheme.background,
            topBar = { Header(navController, "Back", "Kết nối wifi") },
            bottomBar = { MenuBottom(navController) },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    horizontal = layoutConfig.outerPadding,
                                    vertical = layoutConfig.textFieldSpacing
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Kết nối wifi",
                                fontSize = layoutConfig.headingFontSize,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))
                            OutlinedTextField(
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(25),
                                singleLine = true,
                                value = ssidState.value,
                                onValueChange = {
                                    ssidState.value = it
                                    ssidErrorState.value = ValidationUtils.validateSSID(it)
                                },
                                placeholder = { Text("SSID:") },
                                leadingIcon = { Icon(Icons.Filled.Wifi, contentDescription = null) },
                                isError = ssidErrorState.value.isNotEmpty() && ssidErrorState.value != "SSID hợp lệ",
                                supportingText = { if (ssidErrorState.value.isNotEmpty() && ssidErrorState.value != "SSID hợp lệ") Text(ssidErrorState.value) },
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
                            Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))
                            OutlinedTextField(
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(25),
                                singleLine = true,
                                value = passwordState.value,
                                onValueChange = {
                                    passwordState.value = it
                                },
                                placeholder = { Text("Mật khẩu:") },
                                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                                        Icon(
                                            imageVector = if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                            contentDescription = if (passwordVisible.value) "Hide password" else "Show password"
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                isError = passwordErrorState.value.isNotEmpty() && passwordErrorState.value != "Mật khẩu hợp lệ.",
                                supportingText = { if (passwordErrorState.value.isNotEmpty() && passwordErrorState.value != "Mật khẩu hợp lệ.") Text(passwordErrorState.value) },
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
                            Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))
                            Button(
                                onClick = {
                                    if (ssidState.value.isEmpty() || passwordState.value.isEmpty()) {
                                        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin Wi-Fi!", Toast.LENGTH_SHORT).show()
                                    } else if (ssidErrorState.value != "SSID hợp lệ") {
                                        Toast.makeText(context, "Vui lòng sửa lỗi trong thông tin Wi-Fi!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        coroutineScope.launch {
                                            val success = viewModel.sendCredentials(context, "192.168.4.1", 4210, ssidState.value, passwordState.value)
                                            if (success) {
                                                navController.popBackStack()
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .width(if (isTablet) 300.dp else 200.dp)
                                    .height(if (isTablet) 56.dp else 48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                                enabled = !viewModel.isLoading
                            ) {
                                if (viewModel.isLoading) {
                                    CircularProgressIndicator()
                                } else {
                                    Text(
                                        text = "Kết Nối",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}