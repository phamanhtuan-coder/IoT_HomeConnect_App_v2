package com.sns.homeconnect_v2.presentation.screen.profile

import IoTHomeConnectAppTheme
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.data.remote.dto.request.ChangePasswordRequest
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField
import com.sns.homeconnect_v2.presentation.viewmodel.profile.UpdatePasswordState
import com.sns.homeconnect_v2.presentation.viewmodel.profile.UpdatePasswordViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel


@Composable
fun UpdatePasswordScreen(
    navController: NavHostController,
    userId: Int,
    viewModel: UpdatePasswordViewModel = hiltViewModel(),
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    val updatePasswordState by viewModel.updatePasswordState.collectAsState()

    when(updatePasswordState){
        is UpdatePasswordState.Error ->{
            Text((updatePasswordState as UpdatePasswordState.Error).error, color = Color.Red)
            Log.e("Error",  (updatePasswordState as UpdatePasswordState.Error).error)
        }
        is UpdatePasswordState.Idle ->{
            //Todo
        }
        is UpdatePasswordState.Loading -> {
            CircularProgressIndicator()
        }
        is UpdatePasswordState.Success -> {
            Text((updatePasswordState as UpdatePasswordState.Success).message, color = Color.Red)
            //navController.navigate(Screens.Login.route)
            Log.d("Success", (updatePasswordState as UpdatePasswordState.Success).message)
        }
    }
    val context = LocalContext.current
    val isTablet = isTablet(LocalContext.current)

    // Trạng thái cho các trường mật khẩu
    var oldPasswordState by remember { mutableStateOf("") }
    var oldPasswordVisible by remember { mutableStateOf(false) }

    var newPasswordState by remember { mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(false) }

    var rePasswordState by remember { mutableStateOf("") }
    var rePasswordVisible by remember { mutableStateOf(false) }

    // Biến trạng thái lưu thông báo lỗi
    var passwordError by remember { mutableStateOf("") }
    var passwordNewError by remember { mutableStateOf("") }
    var passwordConfirmError by remember { mutableStateOf("") }

    var changePasswordRequest by remember { mutableStateOf<ChangePasswordRequest?>(null) }
    
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp


        // Xác định xem nếu chúng ta đang ở chế độ ngang
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        //  kích thước phản hồi
        val horizontalPadding = when {
            screenWidthDp < 360.dp -> 8.dp
            screenWidthDp < 600.dp -> 16.dp
            else -> 32.dp
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
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
                    //Thiếu Image
                    Text(
                        text = "Cập nhật Mật Khẩu",
                        fontSize = if (isTablet) 28.sp else 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    //Căn lại tỉ lệ chữ
                    Text(
                        text = "Mật khẩu mới của bạn phải khác với mật khẩu đã sử dụng trước đó.",
                        fontSize = 14.sp,
                        color  = colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    //MẬT KHẨU CŨ

                    StyledTextField(
                        value = oldPasswordState,
                        onValueChange = {
                            oldPasswordState = it
                            passwordError = ValidationUtils.validatePassword(it) // Kiểm tra mật khẩu
                        },
                        placeholderText = "Group name",
                        leadingIcon = Icons.Default.Person
                    )
                    //Sửa lại cac textfield cho có thêm icon ẩn hiện mật khẩu
//                    OutlinedTextField(
//                        shape = RoundedCornerShape(25),
//                        singleLine = true,
//                        value = oldPasswordState,
//                        onValueChange = {
//                            oldPasswordState = it
//                            passwordError = ValidationUtils.validatePassword(it) // Kiểm tra mật khẩu
//                        },
//                        placeholder = { Text("Mật khẩu:") },
//                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
//                        trailingIcon = {
//                            IconButton(onClick = { oldPasswordVisible = !oldPasswordVisible }) {
//                                Icon(
//                                    imageVector = if (oldPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
//                                    contentDescription = if (oldPasswordVisible) "Hide password" else "Show password"
//                                )
//                            }
//                        },
//                        visualTransformation = if (oldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                        modifier = Modifier
//                            .width(if (isTablet) 500.dp else 400.dp)
//                            .height(if (isTablet) 80.dp else 70.dp),
//                        colors = TextFieldDefaults.colors(
//                            focusedTextColor = colorScheme.onBackground,  // Màu text khi TextField được focus
//                            unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),  // Màu text khi TextField không được focus
//                            focusedContainerColor = colorScheme.onPrimary,
//                            unfocusedContainerColor = colorScheme.onPrimary,
//                            focusedIndicatorColor = colorScheme.primary,
//                            unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
//                        )
//                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // MẬT KHẨU MỚI

                    StyledTextField(
                        value = newPasswordState,
                        onValueChange = {
                            newPasswordState = it
                            passwordNewError = ValidationUtils.validatePassword(it) // Kiểm tra mật khẩu mới
                        },
                        placeholderText = "Group name",
                        leadingIcon = Icons.Default.Person
                    )
//                    OutlinedTextField(
//                        shape = RoundedCornerShape(25),
//                        singleLine = true,
//                        value = newPasswordState,
//                        onValueChange = {
//                            newPasswordState = it
//                            passwordNewError = ValidationUtils.validatePassword(it) // Kiểm tra mật khẩu mới
//                        },
//                        placeholder = { Text("Mật khẩu mới:") },
//                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
//                        trailingIcon = {
//                            IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
//                                Icon(
//                                    imageVector = if (newPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
//                                    contentDescription = if (newPasswordVisible) "Hide password" else "Show password"
//                                )
//                            }
//                        },
//                        visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                        modifier = Modifier
//                            .width(if (isTablet) 500.dp else 400.dp)
//                            .height(if (isTablet) 80.dp else 70.dp),
//                        colors = TextFieldDefaults.colors(
//                            focusedTextColor = colorScheme.onBackground,  // Màu text khi TextField được focus
//                            unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),  // Màu text khi TextField không được focus
//                            focusedContainerColor = colorScheme.onPrimary,
//                            unfocusedContainerColor = colorScheme.onPrimary,
//                            focusedIndicatorColor = colorScheme.primary,
//                            unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
//                        )
//                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    StyledTextField(
                        value = rePasswordState,
                        onValueChange = {
                            rePasswordState = it
                            passwordConfirmError = ValidationUtils.validateConfirmPassword(newPasswordState, it) // Kiểm tra mật khẩu
                        },
                        placeholderText = "Group name",
                        leadingIcon = Icons.Default.Person
                    )
                    // NHẬP LẠI MẬT KHẨU MỚI
//                    OutlinedTextField(
//                        shape = RoundedCornerShape(25),
//                        singleLine = true,
//                        value = rePasswordState,
//                        onValueChange = {
//                            rePasswordState = it
//                            passwordConfirmError = ValidationUtils.validateConfirmPassword(newPasswordState, it) // Kiểm tra mật khẩu xác nhận
//                        },
//                        placeholder = { Text("Nhập lại mật khẩu mới:") },
//                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
//                        trailingIcon = {
//                            IconButton(onClick = { rePasswordVisible = !rePasswordVisible }) {
//                                Icon(
//                                    imageVector = if (rePasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
//                                    contentDescription = if (rePasswordVisible) "Hide password" else "Show password"
//                                )
//                            }
//                        },
//                        visualTransformation = if (rePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                        modifier = Modifier
//                            .width(if (isTablet) 500.dp else 400.dp)
//                            .height(if (isTablet) 80.dp else 70.dp),
//                        colors = TextFieldDefaults.colors(
//                            focusedTextColor = colorScheme.onBackground,  // Màu text khi TextField được focus
//                            unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),  // Màu text khi TextField không được focus
//                            focusedContainerColor = colorScheme.onPrimary,
//                            unfocusedContainerColor = colorScheme.onPrimary,
//                            focusedIndicatorColor = colorScheme.primary,
//                            unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
//                        )
//                    )

                    // Hiện lỗi nếu có
                    if (passwordNewError.isNotEmpty()) {
                        Text(passwordNewError, color = Color.Red, fontSize = 12.sp)
                    }
                    if (passwordConfirmError.isNotEmpty()) {
                        Text(passwordConfirmError, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    //ToDo: Trả về các điều kiện cần đạt được cách mật khẩu

                    // Nút Đặt lại mật khẩu
                    ActionButtonWithFeedback(
                        label = "Đặt lại mật khẩu",
                        style = if (
                            oldPasswordState.isNotBlank()
                            && newPasswordState.isNotBlank()
                            && rePasswordState.isNotBlank()
                            && passwordNewError.isEmpty()
                            && passwordConfirmError.isEmpty()
                        ) HCButtonStyle.PRIMARY else HCButtonStyle.DISABLED,
                        onAction = { _, _ ->
                            // Nếu đã hợp lệ thì gọi API
                            viewModel.updatePassword(
                                userId = userId,
                                changePasswordRequest = ChangePasswordRequest(
                                    oldPassword = oldPasswordState,
                                    newPassword = newPasswordState
                                )
                            )
                        },
                        snackbarViewModel = snackbarViewModel
                    )

//                    Button(
//                        onClick = {
//                            /* TODO: Xử lý khi nhấn nút kết nối, kiểm tra và báo lỗi v.v và quây trở lại màn hình trước đó*/
//                        },
//                        modifier = Modifier
//                            .width(if (isTablet) 300.dp else 200.dp)
//                            .height(if (isTablet) 56.dp else 48.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
//                        shape = RoundedCornerShape(50)
//                    ) {
//                        Text(
//                            text = "Đặt lại mật khẩu",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = colorScheme.onPrimary
//                        )
//                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nút quay lại màn hình đăng nhập
                    TextButton(onClick = {
                        //ToDo: Sử lý xự kiện quay lại
                        if (passwordError.isEmpty() && passwordNewError.isEmpty() && passwordConfirmError.isEmpty()) {
                            // Gửi yêu cầu đổi mật khẩu
                            changePasswordRequest = ChangePasswordRequest(
                                oldPassword = oldPasswordState,
                                newPassword = newPasswordState,
                            )
                            viewModel.updatePassword(userId, changePasswordRequest!!)

                            // Hiển thị Toast thành công
                            Toast.makeText(context, "Đặt lại mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                        } else {
                            // Hiển thị lỗi bằng Toast
                            if (passwordError.isNotEmpty()) {
                                Toast.makeText(context, passwordError, Toast.LENGTH_SHORT).show()
                            } else if (passwordNewError.isNotEmpty()) {
                                Toast.makeText(context, passwordNewError, Toast.LENGTH_SHORT).show()
                            } else if (passwordConfirmError.isNotEmpty()) {
                                Toast.makeText(context, passwordConfirmError, Toast.LENGTH_SHORT).show()
                            }
                        }
                        navController.popBackStack()
                    }) {
                        Text(
                            text = "Quay lại",
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

@Preview(showBackground = true,showSystemUi = true)
@Composable
fun UpdatePasswordScreenPreview() {
    UpdatePasswordScreen(navController = rememberNavController(), -1)
}