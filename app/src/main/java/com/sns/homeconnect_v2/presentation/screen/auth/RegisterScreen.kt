package com.sns.homeconnect_v2.presentation.screen.auth

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.sns.homeconnect_v2.R
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.data.remote.dto.request.RegisterRequest
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.auth.RegisterState
import com.sns.homeconnect_v2.presentation.viewmodel.auth.RegisterViewModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.core.graphics.scale
import com.sns.homeconnect_v2.presentation.component.DatePickerTextField
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("InlinedApi")
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val colorScheme = MaterialTheme.colorScheme
    val registerState by viewModel.registerState.collectAsState()

//    var passwordVisible by remember { mutableStateOf(false) }
//    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var selectedDate by remember { mutableStateOf("01/01/2004") }
    var profileImage by remember { mutableStateOf("") }
    var stage by remember { mutableIntStateOf(1) }
    var errorMessage by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }

    val initialDateMillis = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        .parse(selectedDate)?.time ?: System.currentTimeMillis()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)
    var showDatePicker by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val mimeType = context.contentResolver.getType(it)
            if (mimeType == "image/jpeg" || mimeType == "image/png") {
                avatarUri = it
                errorMessage = ""

                val maxSizeInKB = 25
                val inputImage = uriToByteArray(context, it)
                if (inputImage != null) {
                    val compressedImage = compressImage(inputImage, quality = 90, maxFileSizeKB = 25)
                    if (compressedImage != null && compressedImage.size / 1024 <= maxSizeInKB) {
                        profileImage = Base64.encodeToString(compressedImage, Base64.NO_WRAP)
                    } else {
                        errorMessage = "Ảnh quá lớn, không thể nén đủ nhỏ!"
                    }
                } else {
                    errorMessage = "Không thể đọc ảnh từ thiết bị!"
                }
            } else {
                errorMessage = "Chỉ chấp nhận định dạng JPEG hoặc PNG."
            }
        }
    }

    fun validateInput(): Boolean {
        errorMessage = ""
        when {
            name.isBlank() || !name.matches(Regex("^[a-zA-Z\\s'-]+$")) ->
                errorMessage = "Tên không được chứa ký tự đặc biệt hoặc số."
            email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                errorMessage = "Email không hợp lệ."
            phoneNumber.isBlank() || !phoneNumber.matches(Regex("^[0-9]{10,11}$")) ->
                errorMessage = "Số điện thoại không hợp lệ."
            password.length < 8 || !password.matches(Regex(".*[A-Z].*")) || !password.matches(Regex(".*[a-z].*")) ||
                    !password.matches(Regex(".*\\d.*")) || !password.matches(Regex(".*[@#$%^&+=].*")) ->
                errorMessage = "Mật khẩu cần ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt."
            confirmPassword != password ->
                errorMessage = "Mật khẩu nhập lại không khớp."
//            avatarUri == null ->
//                errorMessage = "Vui lòng chọn ảnh đại diện."
        }
        return errorMessage.isEmpty()
    }

    // TODO: Re-enable when API is ready
    /*
    when (registerState) {
        is RegisterState.Error -> {
            Text((registerState as RegisterState.Error).error, color = Color.Red)
        }
        is RegisterState.Idle -> {}
        is RegisterState.Loading -> {
            CircularProgressIndicator()
        }
        is RegisterState.Success -> {
            LaunchedEffect(Unit) {
                navController.navigate(Screens.Login.route) {
                    popUpTo(Screens.Welcome.route) { inclusive = false }
                }
            }
        }
    }
    */

    // Function to handle registration for demo
    fun handleRegistrationDemo() {
        // Direct navigation for demo purposes without API call
        navController.navigate(Screens.Login.route) {
            popUpTo(Screens.Welcome.route) { inclusive = false }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(colorScheme.background),
        containerColor = colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
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
                StyledTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = ValidationUtils.validateFullName(it)
                    },
                    placeholderText = "Họ tên",
                    leadingIcon = Icons.Default.Person,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                StyledTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = ValidationUtils.validateEmail(it)
                    },
                    placeholderText = "Nhập email",
                    leadingIcon = Icons.Default.Email,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                StyledTextField(
                    value = phoneNumber,
                    onValueChange = {
                        phoneNumber = it
                        phoneError = ValidationUtils.validatePhoneNumber(it)
                    },
                    placeholderText = "Nhập số điện thoại",
                    leadingIcon = Icons.Default.Phone,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                StyledTextField(
                    value = address,
                    onValueChange = {
                        address = it
                        addressError = ValidationUtils.validateAddress(it)
                    },
                    placeholderText = "Nhập địa chỉ",
                    leadingIcon = Icons.Default.LocationOn,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                DatePickerTextField(
                    label = "Ngày sinh",
                    value = selectedDate,
                    modifier = Modifier
                        .padding(horizontal = 0.dp),
                    onValueChange = { selectedDate = it }
                )

//                if (showDatePicker) {
//                    Popup(onDismissRequest = { showDatePicker = false }, alignment = Alignment.TopStart) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .offset(y = 64.dp)
//                                .shadow(elevation = 4.dp)
//                                .background(colorScheme.background)
//                                .padding(16.dp)
//                        ) {
//                            DatePicker(state = datePickerState, showModeToggle = false)
//                        }
//                    }
//                }

                LaunchedEffect(datePickerState.selectedDateMillis) {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        selectedDate = formatter.format(Date(millis))
                        showDatePicker = false
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
                    } ?: Box(
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

                Spacer(modifier = Modifier.height(8.dp))

                val requiredPermissions = when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                    else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

                val isPermissionGranted = remember {
                    mutableStateOf(requiredPermissions.all {
                        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                    })
                }

                val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    isPermissionGranted.value = permissions.all { it.value }
                }

                val openSettingsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

                if (!isPermissionGranted.value) {
                    errorMessage = "Ứng dụng cần quyền truy cập bộ nhớ để chọn ảnh đại diện."
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = { permissionLauncher.launch(requiredPermissions) },
                            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Yêu cầu cấp quyền", color = colorScheme.onPrimary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                                openSettingsLauncher.launch(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Mở cài đặt", color = colorScheme.onSecondary)
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = { imagePickerLauncher.launch("image/*") },
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

                StyledTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    placeholderText = "Nhập mật khẩu",
                    leadingIcon = Icons.Default.LocationOn,
                    isPassword = true,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                StyledTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                    },
                    placeholderText = "Nhập mật khẩu",
                    leadingIcon = Icons.Default.LocationOn,
                    isPassword = true,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(if (isTablet) 0.8f else 1f)
            ) {
                if (stage == 2) {
                    ActionButtonWithFeedback(
                        label = "Quay lại",
                        style = HCButtonStyle.SECONDARY,
                        onAction = { _, _ ->
                            stage = 1
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp)
                            .weight(1f)
                    )
                }


                ActionButtonWithFeedback(
                    label = if (stage == 1) "Tiếp tục" else "Đăng ký",
                    style = HCButtonStyle.PRIMARY,
                    onAction = { _, _ ->
                        if (stage == 1) {
                            stage = 2
                            errorMessage = ""
                        } else if (stage == 2 && validateInput()) {
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            val localDate = LocalDate.parse(selectedDate, formatter)
                            // Nếu user không chọn ảnh thì lấy base64 ảnh mặc định từ drawable
                            val imageBase64 = if (profileImage.isNotBlank()) {
                                profileImage
                            } else {
                                drawableToBase64(context, R.drawable.person)
                            }
                            val user = RegisterRequest(
                                Name = name,
                                Email = email,
                                PasswordHash = password,
                                Phone = phoneNumber,
                                Address = address,
                                ProfileImage = imageBase64,
                                DateOfBirth = localDate.toString()
                            )
                            viewModel.register(user)
                        }
                    },
                    modifier = Modifier
                        .then(
                            if (stage == 2) {
                                Modifier.padding(start = 8.dp, end = 16.dp)
                            } else {
                                Modifier.padding(horizontal = 16.dp) // hoặc giá trị mặc định khác nếu muốn
                            }
                        )
                        .weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

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


fun drawableToBase64(context: android.content.Context, drawableRes: Int, maxSizeKB: Int = 25): String {
    val bitmap = BitmapFactory.decodeResource(context.resources, drawableRes)
    val outputStream = ByteArrayOutputStream()
    var currentQuality = 90
    var tempBitmap = bitmap

    // Nén giống logic compressImage
    do {
        outputStream.reset()
        tempBitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, outputStream)
        if (outputStream.size() / 1024 > maxSizeKB) {
            tempBitmap = tempBitmap.scale(tempBitmap.width / 2, tempBitmap.height / 2, filter = true)
        }
        currentQuality -= 10
    } while (outputStream.size() / 1024 > maxSizeKB && currentQuality > 10)

    return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
}

fun compressImage(inputImage: ByteArray, quality: Int, maxFileSizeKB: Int): ByteArray? {
    var bitmap = BitmapFactory.decodeByteArray(inputImage, 0, inputImage.size)
    val outputStream = ByteArrayOutputStream()
    var currentQuality = quality

    do {
        outputStream.reset()
        bitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, outputStream)
        if (outputStream.size() / 1024 > maxFileSizeKB) {
            bitmap = resizeBitmap(bitmap, bitmap.width / 2, bitmap.height / 2)
        }
        currentQuality -= 10
    } while (outputStream.size() / 1024 > maxFileSizeKB && currentQuality > 10)

    return outputStream.toByteArray()
}

fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    return bitmap.scale(newWidth, newHeight)
}

fun uriToByteArray(context: android.content.Context, uri: Uri): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val byteArray = inputStream?.readBytes()
        inputStream?.close()
        byteArray
    } catch (_: Exception) {
        null
    }
}