package com.sns.homeconnect_v2.presentation.screen.iot_device.sharing

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.sharing.DeviceSharingActionState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.sharing.DeviceSharingState
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.sharing.DeviceSharingViewModel


/** Giao diện màn hình Share Device (ShareDeviceScreen)
 * -----------------------------------------
 * Người viết: Nguyễn Thanh Sang
 * Ngày viết: 07/12/2024
 * Lần cập nhật cuối: 07/12/2024
 * -----------------------------------------
 * Input:
 *
 * Output: Row
 *
 * ---------------------------------------
 */
@Composable
fun ShareDeviceScreen(
    navController: NavHostController,
    id: Int,
    viewModel: DeviceSharingViewModel = hiltViewModel(),
) {


    val addSharedUserState by viewModel.addSharedUserState.collectAsState()

    when (addSharedUserState) {
        DeviceSharingActionState.Idle -> {
            viewModel.getSharedUsers(id)
        }

        DeviceSharingActionState.Loading -> {
            CircularProgressIndicator()
        }

        is DeviceSharingActionState.Success -> {
            LaunchedEffect(Unit) {
                navController.popBackStack(
                    Screens.SharedUsers.route + "?id=${id}",
                    inclusive = false
                )
            }
        }

        is DeviceSharingActionState.Error -> {
            Log.e(
                "ShareDeviceScreen",
                "Error: ${(addSharedUserState as DeviceSharingState.Error).error}"
            )
        }
    }


    val deviceIdState = remember { mutableIntStateOf(id) }
    val deviceIdErrorState = remember { mutableStateOf("") }

    val emailState = remember { mutableStateOf("") }
    val emailErrorState = remember { mutableStateOf("") }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        val isTablet = isTablet(LocalContext.current)
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
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            ) {
                // Tiêu đề chính
                Text(
                    text = "CHIA SẼ THIẾT BỊ",
                    fontSize = if (isTablet) 28.sp else 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )

                Text(
                    text = "Vui lòng nhập ID thiết bị và Email tài khoản.",
                    fontSize = 14.sp,
                    color = colorScheme.onBackground.copy(alpha = 0.6f)
                )

                // Cột chứa các ô nhập liệu và nút gửi yêu cầu
                Column(
                    modifier = Modifier
                        .padding(horizontal = if (isTablet) 32.dp else 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Ô nhập ID thiết bị
                    OutlinedTextField(
                        //ToDo: Bổ sung biến lưu giá trị
                        value = deviceIdState.value.toString(), // Giá trị hiện tại (để trống)
                        onValueChange = {
                            deviceIdState.value = it.toInt()
                            // Kiểm tra lỗi ngay khi nhập
                            deviceIdErrorState.value = ValidationUtils.validateDeviceId(it)
                        }, // Hàm xử lý khi nhập liệu (chưa triển khai)
                        leadingIcon = { // Biểu tượng ở bên trái ô nhập liệu
                            Icon(Icons.Filled.Devices, contentDescription = null) // Icon người dùng
                        },
                        shape = RoundedCornerShape(25),
                        singleLine = true,
                        placeholder = { Text("ID Thiết bị") }, // Nhãn cho ô nhập liệu
                        modifier = Modifier
                            .width(if (isTablet) 500.dp else 400.dp)
                            .height(if (isTablet) 80.dp else 70.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = colorScheme.onBackground,  // Màu text khi TextField được focus
                            unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),  // Màu text khi TextField không được focus
                            focusedContainerColor = colorScheme.onPrimary,
                            unfocusedContainerColor = colorScheme.onPrimary,
                            focusedIndicatorColor = colorScheme.primary,
                            unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                        ),
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách dưới ô nhập ID

                    // Ô nhập Email tài khoản
                    OutlinedTextField(
                        value = emailState.value, // Giá trị hiện tại (để trống)
                        onValueChange = {
                            emailState.value = it
                            // Kiểm tra lỗi ngay khi nhập
                            emailErrorState.value = ValidationUtils.validateEmail(it)
                        }, // Hàm xử lý khi nhập liệu (chưa triển khai)
                        leadingIcon = { // Biểu tượng ở bên trái ô nhập liệu
                            Icon(Icons.Filled.Email, contentDescription = null) // Icon email
                        },
                        shape = RoundedCornerShape(25),
                        singleLine = true,
                        placeholder = { Text("Email tài khoản") }, // Nhãn cho ô nhập liệu
                        modifier = Modifier
                            .width(if (isTablet) 500.dp else 400.dp)
                            .height(if (isTablet) 80.dp else 70.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = colorScheme.onBackground,  // Màu text khi TextField được focus
                            unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),  // Màu text khi TextField không được focus
                            focusedContainerColor = colorScheme.onPrimary,
                            unfocusedContainerColor = colorScheme.onPrimary,
                            focusedIndicatorColor = colorScheme.primary,
                            unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                        ),
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách dưới ô nhập Email

                    // Nút gửi yêu cầu
                    Button(
                        onClick = {
                            viewModel.addSharedUser(deviceIdState.value, emailState.value)
                        },
                        modifier = Modifier
                            .width(if (isTablet) 300.dp else 200.dp)
                            .height(if (isTablet) 56.dp else 48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("GỬI YÊU CẦU", color = Color.White) // Nội dung và màu chữ của nút
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShareDeviceScreenPreview() {
    ShareDeviceScreen(rememberNavController(), 1)
}