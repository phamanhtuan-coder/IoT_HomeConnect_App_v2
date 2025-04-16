package com.sns.homeconnect_v2.presentation.screen.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.ungdungquanlyvadieukhieniot_homeconnect.data.remote.dto.RegisterRequest
import com.example.ungdungquanlyvadieukhieniot_homeconnect.ui.navigation.Screens
import com.example.ungdungquanlyvadieukhieniot_homeconnect.ui.theme.AppTheme
import com.example.ungdungquanlyvadieukhieniot_homeconnect.ui.validation.ValidationUtils
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("InlinedApi")
@Composable
fun SignUpScreen(navController: NavHostController,
) {

//    // Họ tên
//    var name by remember { mutableStateOf("") }
      var nameError by remember { mutableStateOf("") }
//
//    // Email
//    var email by remember { mutableStateOf("") }
      var emailError by remember { mutableStateOf("") }
//
//    // Số điện thoại
//    var phoneNumber by remember { mutableStateOf("") }
      var phoneError by remember { mutableStateOf("") }
//
//    // Địa chỉ
//    var address by remember { mutableStateOf("") }
      var addressError by remember { mutableStateOf("") }

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel = remember {
        SignUpViewModel(application, context)
    }

    val signUpState by viewModel.signUpState.collectAsState()

    when(signUpState){
        is SignUpState.Error ->{
            Text((signUpState as SignUpState.Error).error, color = Color.Red)
            Log.e("Error",  (signUpState as SignUpState.Error).error)
        }
        is SignUpState.Idle ->{
            //Todo
        }
        is SignUpState.Loading -> {
            CircularProgressIndicator()
        }
        is SignUpState.Success -> {
            LaunchedEffect(Unit) {
                navController.navigate(Screens.Login.route) {
                    popUpTo(Screens.Register.route) { inclusive = true }
                }
            }
        }
    }

    AppTheme {
        val context = LocalContext.current
        val configuration = LocalConfiguration.current
        val isTablet = configuration.screenWidthDp >= 600

        val colorScheme = MaterialTheme.colorScheme
        var passwordVisible by remember { mutableStateOf(false) }
        var confirmPasswordVisible by remember { mutableStateOf(false) }

        var user by remember { mutableStateOf<RegisterRequest?>(null) }

        var selectedDate by remember { mutableStateOf("01/01/2004") }
        val initialDateMillis = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .parse(selectedDate)?.time ?: System.currentTimeMillis()
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

        var showDatePicker by remember { mutableStateOf(false) }
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }
        var address by remember { mutableStateOf("") }
        var avatarUri by remember { mutableStateOf<Uri?>(null) }

        var stage by remember { mutableStateOf(1) }
        var errorMessage by remember { mutableStateOf("") }

        var profileImage by remember {mutableStateOf("")}

        val imagePickerLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    val mimeType = context.contentResolver.getType(it)
                    if (mimeType == "image/jpeg" || mimeType == "image/png") {
                        avatarUri = it // Lưu URI của ảnh
                        errorMessage = ""

                        val maxSizeInKB = 25 // Giới hạn kích thước ảnh 92KB

                        // Chuyển URI thành ByteArray
                        val inputImage = uriToByteArray(context, it)

                        if (inputImage != null) {
                            // Nén ảnh
                            val compressedImage =
                                compressImage(
                                    inputImage = inputImage, // Ảnh gốc dạng ByteArray
                                    quality = 90, // Chất lượng khởi đầu (90%)
                                    maxFileSizeKB = 25 // Kích thước mong muốn (25 KB)
                                )
                            if (compressedImage != null && compressedImage.size / 1024 <= maxSizeInKB) {
                                // Chuyển đổi ảnh đã nén sang Base64
                                val base64Image = Base64.encodeToString(compressedImage, Base64.NO_WRAP)
                                profileImage = base64Image
                                Log.d("Base64", base64Image) // Log Base64 hoặc gửi lên API
                            } else {
                                // Ảnh vượt kích thước hoặc không thể nén đủ nhỏ
                                errorMessage = "Ảnh quá lớn, không thể nén đủ nhỏ!"
                                Log.e("ImagePicker", "Không thể nén ảnh")
                            }
                        } else {
                            errorMessage = "Không thể đọc ảnh từ thiết bị!"
                            Log.e("ImagePicker", "Không thể chuyển URI thành ByteArray")
                        }
                    } else {
                        // MIME type không hợp lệ
                        errorMessage = "Chỉ chấp nhận định dạng JPEG hoặc PNG."
                        Log.e("ImagePicker", "Định dạng file không hợp lệ: $mimeType")
                    }
                }
            }

        fun validateInput(): Boolean {
            errorMessage = ""
            when {
                name.isBlank() || !name.matches(Regex("^[a-zA-Z\\s'-]+\$")) -> errorMessage =
                    "Tên không được chứa ký tự đặc biệt hoặc số."

                email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches() -> errorMessage = "Email không hợp lệ."

                phoneNumber.isBlank() || !phoneNumber.matches(Regex("^[0-9]{10,11}\$")) -> errorMessage =
                    "Số điện thoại không hợp lệ."

                password.length < 8 || !password.matches(Regex(".*[A-Z].*")) && !password.matches(
                    Regex(".*[a-z].*")
                )
                        || !password.matches(Regex(".*\\d.*")) || !password.matches(Regex(".*[@#$%^&+=].*")) -> errorMessage =
                    "Mật khẩu cần ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt."

                confirmPassword != password -> errorMessage = "Mật khẩu nhập lại không khớp."
                avatarUri == null -> errorMessage = "Vui lòng chọn ảnh đại diện."
            }
            return errorMessage.isEmpty()
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background),
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(horizontal = if (isTablet) 32.dp else 16.dp)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = if (stage == 1) "Đăng ký - Bước 1" else "Đăng ký - Bước 2",
                    fontSize = if (isTablet) 28.sp else 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
                Text(
                    text = "Hãy hoàn thành thông tin để tạo tài khoản",
                    fontSize = 14.sp,
                    color = colorScheme.onBackground.copy(alpha = 0.6f)
                )

                if (stage == 1) {
                    OutlinedTextField(
                        shape = RoundedCornerShape(25),
                        singleLine = true,
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = ValidationUtils.validateFullName(it) // Gọi hàm kiểm tra
                                        },
                        placeholder = { Text("Họ tên") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
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
                        )
                    )

                    OutlinedTextField(
                        shape = RoundedCornerShape(25),
                        singleLine = true,
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = ValidationUtils.validateEmail(it) // Gọi hàm kiểm tra
                                        },
                        placeholder = { Text("Email") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                        )
                    )

                    OutlinedTextField(
                        shape = RoundedCornerShape(25),
                        singleLine = true,
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it
                            phoneError = ValidationUtils.validatePhoneNumber(it) // Gọi hàm kiểm tra
                                        },
                        placeholder = { Text("Số điện thoại") },
                        leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                        )
                    )

                    OutlinedTextField(
                        shape = RoundedCornerShape(25),
                        singleLine = true,
                        value = address,
                        onValueChange = {
                            address = it
                            addressError = ValidationUtils.validateAddress(it) // Gọi hàm kiểm tra
                                        },
                        placeholder = { Text("Địa chỉ") },
                        leadingIcon = { Icon(Icons.Filled.Home, contentDescription = null) },
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
                        )
                    )

                    OutlinedTextField(
                        value = selectedDate, // Chỗ này bị bug chưa load được ngày từ biến
                        onValueChange = { /* Not needed for read-only fields */ },
                        placeholder = { Text("Ngày sinh (dd/mm/yyyy)") },
                        shape = RoundedCornerShape(25),
                        singleLine = true,
                        readOnly = true,
                        trailingIcon = {
                            // Nút chọn ngày
                            IconButton(onClick = { showDatePicker = !showDatePicker }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Chọn ngày"
                                )
                            }
                        },
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
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                    if (showDatePicker) {
                        Popup(
                            onDismissRequest = { showDatePicker = false },
                            alignment = Alignment.TopStart
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(y = 64.dp)
                                    .shadow(elevation = 4.dp)
                                    .background(colorScheme.background)
                                    .padding(16.dp)
                            ) {
                                DatePicker(
                                    state = datePickerState,
                                    showModeToggle = false
                                )
                            }
                        }
                    }

                    //Nếu thay dđổi ngày thì update
                    LaunchedEffect(datePickerState.selectedDateMillis) {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            selectedDate = formatter.format(Date(millis))
                            showDatePicker = false //Ẩn date picker
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(horizontal = if (isTablet) 32.dp else 16.dp)
                            .imePadding()
                            .fillMaxWidth(if (isTablet) 0.8f else 0.9f)
                    ) {
                        Text(
                            text = "Chọn ảnh đại diện",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        avatarUri?.let {
                            Image(
                                painter = rememberAsyncImagePainter(it),
                                contentDescription = "Avatar Preview",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, colorScheme.primary, CircleShape)
                                    .background(colorScheme.onSurface.copy(alpha = 0.1f))
                            )
                        } ?: Box( // Hiển thị placeholder khi chưa chọn ảnh
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, colorScheme.primary.copy(alpha = 0.5f), CircleShape)
                                .background(colorScheme.onSurface.copy(alpha = 0.05f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Avatar",
                                tint = colorScheme.onBackground.copy(alpha = 0.5f),
                                modifier = Modifier.size(64.dp)
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    val requiredPermissions = when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                        }

                        else -> {
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }

                    val isPermissionGranted = remember {
                        mutableStateOf(
                            requiredPermissions.all {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    it
                                ) == PackageManager.PERMISSION_GRANTED
                            }
                        )
                    }

                    val permissionLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) { permissions ->
                        isPermissionGranted.value = permissions.all { it.value }
                    }

                    val openSettingsLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult()
                    ) { /* No action needed; simply opens the settings */ }

                    if (!isPermissionGranted.value) {
                        errorMessage = "Ứng dụng cần quyền truy cập bộ nhớ để chọn ảnh đại diện."
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Button(
                                onClick = {
                                    permissionLauncher.launch(requiredPermissions)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    "Yêu cầu cấp quyền",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                            data =
                                                Uri.fromParts("package", context.packageName, null)
                                        }
                                    openSettingsLauncher.launch(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("Mở cài đặt", color = MaterialTheme.colorScheme.onSecondary)
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                imagePickerLauncher.launch("image/*")
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .width(200.dp)
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, colorScheme.primary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Upload,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tải lên ảnh", color = colorScheme.primary, fontSize = 16.sp)
                        }
                    }




                    // Password Input
                    OutlinedTextField(
                        shape = RoundedCornerShape(25),
                        singleLine = true,
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Mật khẩu") },
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
                            focusedTextColor = colorScheme.onBackground,  // Màu text khi TextField được focus
                            unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),  // Màu text khi TextField không được focus
                            focusedContainerColor = colorScheme.onPrimary,
                            unfocusedContainerColor = colorScheme.onPrimary,
                            focusedIndicatorColor = colorScheme.primary,
                            unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    )

                    // Confirm Password Input
                    OutlinedTextField(
                        shape = RoundedCornerShape(25),
                        singleLine = true,
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Nhập lại mật khẩu") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = {
                                confirmPasswordVisible = !confirmPasswordVisible
                            }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                        )
                    )
                }

                // Error Message Display
                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(if (isTablet) 0.8f else 1f)
                        .padding(horizontal = 16.dp)
                ) {
                    if (stage == 2) {
                        OutlinedButton(
                            onClick = { stage = 1 },
                            modifier = Modifier
                                .weight(1f) // Chia đều không gian
                                .width(if (isTablet) 300.dp else 200.dp)
                                .height(if (isTablet) 56.dp else 48.dp),
                        ) {
                            Text("Quay lại")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    OutlinedButton(
                        onClick = {
                            if (stage == 1) {
                                stage = 2
                                errorMessage = ""
                            } else if (stage == 2 && validateInput()) {
                                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                val localDate = LocalDate.parse(selectedDate, formatter)
                                Log.e("date", localDate.toString())
                                // TODO: Xử lý sang màn hình xác thực OTP
                                user = RegisterRequest(Name = name,
                                    Email = email,
                                    PasswordHash = password,
                                    Phone = phoneNumber,
                                    Address = address,
                                    ProfileImage = profileImage,
                                    DateOfBirth = localDate.toString())
                                Log.e("User: ",user.toString())
                                viewModel.signUp(user = user!!)

                            }
                        },
                        enabled = (stage == 1 || (stage == 2 && validateInput())),
                        modifier = Modifier
                            .weight(1f) // Chia đều không gian
                            .width(if (isTablet) 300.dp else 200.dp)
                            .height(if (isTablet) 56.dp else 48.dp)
                            .align(Alignment.CenterVertically),
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(if (stage == 1) "Tiếp tục" else "Đăng ký")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                // Chuyển tới đăng nhập
                Row(
                    modifier = Modifier.fillMaxWidth(if (isTablet) 0.8f else 0.9f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Đã có tài khoản?",
                        fontSize = 14.sp,
                        color = colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = { navController.navigate(Screens.Login.route) }) {
                        Text(
                            text = "Đăng nhập",
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

//private fun compressImage(context: Context, uri: Uri, maxSizeInKB: Int): ByteArray? {
//    return try {
//        val inputStream = context.contentResolver.openInputStream(uri)
//        val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
//        inputStream?.close()
//
//        val outputStream = java.io.ByteArrayOutputStream()
//        var quality = 100
//
//        // Xác định định dạng gốc
//        val mimeType = context.contentResolver.getType(uri)
//        val compressFormat = when (mimeType) {
//            "image/png" -> android.graphics.Bitmap.CompressFormat.PNG
//            "image/jpeg" -> android.graphics.Bitmap.CompressFormat.JPEG
//            else -> return null // Không hỗ trợ định dạng khác
//        }
//
//        // Nén ảnh giữ nguyên định dạng
//        bitmap.compress(compressFormat, quality, outputStream)
//
//        // Nếu định dạng là JPEG, giảm chất lượng nếu vượt kích thước
//        if (compressFormat == android.graphics.Bitmap.CompressFormat.JPEG) {
//            while (outputStream.toByteArray().size / 1024 > maxSizeInKB && quality > 10) {
//                quality -= 10
//                outputStream.reset()
//                bitmap.compress(compressFormat, quality, outputStream)
//            }
//        }
//
//        if (outputStream.toByteArray().size / 1024 <= maxSizeInKB) {
//            outputStream.toByteArray()
//        } else {
//            null // Không thể nén ảnh đủ nhỏ
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//        null
//    }
//}

fun compressImage(inputImage: ByteArray, quality: Int, maxFileSizeKB: Int): ByteArray? {
    // Decode ảnh từ byte array
    var bitmap = BitmapFactory.decodeByteArray(inputImage, 0, inputImage.size)
    val outputStream = ByteArrayOutputStream()

    var currentQuality = quality

    do {
        outputStream.reset() // Xóa dữ liệu cũ trong bộ nhớ

        // Nén ảnh với chất lượng hiện tại
        bitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, outputStream)

        // Nếu kích thước vẫn lớn hơn maxFileSizeKB, giảm độ phân giải
        if (outputStream.size() / 1024 > maxFileSizeKB) {
            bitmap = resizeBitmap(bitmap, bitmap.width / 2, bitmap.height / 2) // Thu nhỏ ảnh
        }

        currentQuality -= 10 // Giảm chất lượng ảnh
    } while (outputStream.size() / 1024 > maxFileSizeKB && currentQuality > 10)

    return outputStream.toByteArray()
}

// Hàm thu nhỏ độ phân giải của ảnh
fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
}

fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val byteArray = inputStream?.readBytes()
        inputStream?.close()
        byteArray
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = rememberNavController())
}