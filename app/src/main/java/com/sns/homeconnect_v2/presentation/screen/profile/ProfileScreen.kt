package com.sns.homeconnect_v2.presentation.screen.profile

import IoTHomeConnectAppTheme
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.validation.ValidationUtils
import com.sns.homeconnect_v2.data.remote.dto.response.User
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.profile.ProfileScreenViewModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import com.sns.homeconnect_v2.core.util.processing.ImageProcessing
import com.sns.homeconnect_v2.data.remote.dto.request.UserRequest
import com.sns.homeconnect_v2.data.remote.dto.response.UserResponse
import com.sns.homeconnect_v2.presentation.component.dialog.WarningDialog
import com.sns.homeconnect_v2.presentation.viewmodel.profile.InfoProfileState
import com.sns.homeconnect_v2.presentation.viewmodel.profile.ProfileState
import com.sns.homeconnect_v2.presentation.viewmodel.profile.PutInfoProfileState

/***
 * Người viết: Nguyễn Thanh Sang
 * Ngày viết: 01/12/2024
 * --------------------------------
 * Người cập nhật: Phạm Anh Tuấn
 * Lần cập nhật cuối: 12/1/2025
 * --------------------------------
 * @param navController: Đối tượng điều khiển điều hướng
 * @return Scaffold chứa toàn bộ giao diện trang cá nhân
 * --------------------------------
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileScreenViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val isTablet = isTablet(context)
    val nameState = remember { mutableStateOf("Nguyễn Thanh Sang") }
    val phoneState = remember { mutableStateOf("0123456789") }
    val locationState = remember { mutableStateOf("TP. Hồ Chí Minh") }
    val emailState = remember { mutableStateOf("example@gmail.com") }
    var selectedDate by remember { mutableStateOf("01/01/2004") }
    val imageUrl = remember { mutableStateOf("") }
    val isVerified = remember { mutableStateOf(false) }
    val dateCreated = remember { mutableStateOf("01/12/2024") }
    var showDatePicker by remember { mutableStateOf(false) }
    val initialDateMillis = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        .parse(dateCreated.value)?.time ?: System.currentTimeMillis()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)
    var showAlertDialog by remember { mutableStateOf(false) }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var profileImage by remember { mutableStateOf("") }
    val displayedAvatarBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val avatarBitmapState = remember { mutableStateOf<Bitmap?>(null) }

    val logoutState by profileViewModel.logoutState.collectAsState()

    // Show toast for logout state
    LaunchedEffect(logoutState) {
        when (logoutState) {
            is ProfileState.Success -> {
                navController.navigate(Screens.Login.route) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }

            is ProfileState.Error -> {
                Toast.makeText(
                    context,
                    (logoutState as ProfileState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> { /* Do nothing */
            }
        }
    }


    var profile by remember { mutableStateOf<User?>(null) } // Lắng nghe danh sách thiết bị
    val infoProfileState by profileViewModel.infoProfileState.collectAsState()

    when (infoProfileState) {
        is InfoProfileState.Error -> {
            Log.d("Error Profile", (infoProfileState as InfoProfileState.Error).error)
        }

        is InfoProfileState.Success -> {
            val successState = infoProfileState as InfoProfileState.Success
            profile = successState.user
            Log.d("Thành công", "Dữ liệu user: ${profile.toString()}")
        }

        else -> {/* Do nothing */
        }
    }

    LaunchedEffect(1) {
        profileViewModel.getInfoProfile()
    }

    LaunchedEffect(profile) { // Lắng nghe thay đổi của profile
        profile?.let {
            nameState.value = it.Name
            phoneState.value = it.Phone
            locationState.value = it.Address
            emailState.value = it.Email
            dateCreated.value = it.DateOfBirth
            Log.e("dateCreated.value 1", dateCreated.value.toString())
            isVerified.value = it.EmailVerified
            if (it.DateOfBirth.isNotEmpty()) {
                try {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
                        timeZone = TimeZone.getTimeZone("UTC") // Xác định múi giờ của server
                    }
                    val parsedDate = dateFormat.parse(it.DateOfBirth)

                    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                        timeZone = TimeZone.getDefault() // Múi giờ hiển thị trên client
                    }
                    dateCreated.value = outputFormat.format(parsedDate!!)
                    Log.e("FormattedDate", dateCreated.value) // Kiểm tra kết quả
                } catch (e: Exception) {
                    Log.e("DateError", "Lỗi chuyển đổi ngày: ${e.message}")
                }
            }

            Log.d("ServerDate", "Ngày từ server: ${it.DateOfBirth}")
            Log.d("FormattedDate", "Ngày hiển thị: ${dateCreated.value}")

            // Xử lý ảnh đại diện (Base64)
            if (it.ProfileImage.isNotEmpty()) {
                val bitmap =
                    ImageProcessing.base64ToBitmap(it.ProfileImage) // Chuyển đổi Base64 thành Bitmap
                if (bitmap != null) {
                    avatarBitmapState.value = bitmap // Cập nhật trạng thái Bitmap
                    displayedAvatarBitmap.value = bitmap
                }
            }
        }
    }

    var userResponse by remember { mutableStateOf<UserResponse?>(null) } // Lắng nghe user
    val putInfoProfileState by profileViewModel.putInfoProfileState.collectAsState()

    when (putInfoProfileState) {
        is PutInfoProfileState.Error -> {
            Log.d("Error Profile", (putInfoProfileState as PutInfoProfileState.Error).error)
        }
        is PutInfoProfileState.Success -> {
            val successState = putInfoProfileState as PutInfoProfileState.Success
            userResponse = successState.userResponse
            Log.d("Thành công", "Dữ liệu user: ${userResponse.toString()}")
        }
        else ->{
            /*Do nothing */
        }
    }

    LaunchedEffect(userResponse) { // Lắng nghe thay đổi của userResponse
        userResponse?.let { response ->
            // Kiểm tra xem response.user có null không
            response.user.let { user ->
                nameState.value = user.Name
                phoneState.value = user.Phone
                locationState.value = user.Address
                emailState.value = user.Email
                dateCreated.value = user.DateOfBirth
                Log.e("dateCreated.value 3", dateCreated.value.toString())
                isVerified.value = user.EmailVerified

                if (user.DateOfBirth.isNotEmpty()) {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    dateFormat.timeZone = TimeZone.getTimeZone("UTC") // Hoặc múi giờ phù hợp
                    val parsedDate = dateFormat.parse(user.DateOfBirth)

                    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                    outputFormat.timeZone = TimeZone.getDefault() // Hoặc múi giờ bạn muốn
                    dateCreated.value = outputFormat.format(parsedDate!!)

                    Log.e("dateCreated.value 4", dateCreated.value.toString())
                }

                // Xử lý ảnh đại diện (Base64)
                if (user.ProfileImage.isNotEmpty()) {
                    val bitmap =
                        ImageProcessing.base64ToBitmap(user.ProfileImage) // Chuyển đổi Base64 thành Bitmap
                    if (bitmap != null) {
                        avatarBitmapState.value = bitmap // Cập nhật trạng thái Bitmap
                    }
                }
            }
        } ?: run {
            Log.e("LaunchedEffect", "UserResponse is null.")
        }
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
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
        ) { /* Không cần xử lý thêm */ }

        val imagePickerLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    val mimeType = context.contentResolver.getType(it)
                    if (mimeType == "image/jpeg" || mimeType == "image/png") {
                        avatarUri = it // Lưu URI của ảnh
                        errorMessage = ""

                        val maxSizeInKB = 25 // Giới hạn kích thước ảnh 92KB

                        // Chuyển URI thành ByteArray
                        val inputImage = ImageProcessing.uriToByteArray(context, it)

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
                                val base64Image =
                                    Base64.encodeToString(compressedImage, Base64.NO_WRAP)
                                profileImage = base64Image
                                Log.d("Base64", base64Image) // Log Base64 hoặc gửi lên API

                                val bitmap = BitmapFactory.decodeByteArray(
                                    compressedImage,
                                    0,
                                    compressedImage.size
                                )
                                displayedAvatarBitmap.value = bitmap
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

        // Họ và tên
        val nameErrorState = remember { mutableStateOf("") }

        // Số điện thoại
        val phoneErrorState = remember { mutableStateOf("") }

        // Nơi sống hiện tại
        val locationErrorState = remember { mutableStateOf("") }

        if (showAlertDialog) {
            WarningDialog(
                title = "Cảnh báo",
                text = "Hành động này sẽ đăng xuất bạn ra khỏi ứng dụng. Bạn có chắc chắn không?",
                onConfirm = {
                    profileViewModel.logout()
                },
                onDismiss = { showAlertDialog = false }
            )
        }

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Trang cá nhân"
                )
            },
            bottomBar = {
                MenuBottom(navController = navController)
            },
            containerColor = colorScheme.background
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .background(colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Cột chứa thông tin cá nhân và biểu tượng
                Column {
                    // Hộp chính với nền trắng
                    Box(
                        modifier = Modifier
                            .padding(top = if (isTablet) 120.dp else 64.dp)
                            .size(
                                width = if (isTablet) 480.dp else 350.dp,
                                height = if (isTablet) 280.dp else 180.dp
                            )
                            .shadow(
                                elevation = 1.dp,
                                shape = RoundedCornerShape(25),
                                clip = false,
                                ambientColor = colorScheme.secondary.copy(alpha = 0.2f),
                                spotColor = colorScheme.primary.copy(alpha = 0.2f)
                            )
                            .background(
                                color = colorScheme.secondary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(25)
                            ),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        // Vòng tròn ngoài màu trắng
                        Box(
                            modifier = Modifier
                                .size(if (isTablet) 130.dp else 80.dp)
                                .offset(y = if (isTablet) (-40).dp else (-20).dp)
                                .background(color = Color.White, shape = CircleShape)
                                .clickable {
                                    if (isPermissionGranted.value) {
                                        // Nếu đã có quyền, mở bộ chọn ảnh
                                        imagePickerLauncher.launch("image/*")
                                    } else {
                                        // Nếu chưa có quyền, yêu cầu cấp quyền
                                        permissionLauncher.launch(requiredPermissions)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            displayedAvatarBitmap.value?.let { bitmap ->
                                Box(
                                    modifier = Modifier
                                        .size(if (isTablet) 120.dp else 70.dp)
                                        .background(
                                            color = colorScheme.primary.copy(alpha = 0.8f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "Avatar Preview",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, colorScheme.primary, CircleShape)
                                            .background(colorScheme.onSurface.copy(alpha = 0.1f))
                                    )
                                }
                            } ?: Box(
                                modifier = Modifier
                                    .size(if (isTablet) 120.dp else 70.dp)
                                    .background(
                                        color = colorScheme.primary.copy(alpha = 0.8f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Avatar",
                                    colorFilter = ColorFilter.tint(colorScheme.onPrimary),
                                    modifier = Modifier.size(if (isTablet) 70.dp else 40.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .offset(
                                        x = (if (isTablet) 10 else 10).dp,
                                        y = (if (isTablet) 40 else 25).dp
                                    ),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                IconButton(
                                    onClick = {
                                        navController.navigate(
                                            Screens.OTP.createRoute(
                                                "email_verification",
                                                emailState.value
                                            )
                                        )
                                    },
                                    enabled = !isVerified.value,
                                ) {
                                    Icon(
                                        imageVector = if (isVerified.value) Icons.Default.CheckCircle else Icons.Default.Error,
                                        contentDescription = "Verified",
                                        tint = if (isVerified.value) Color.Green else Color.Red,
                                        modifier = Modifier
                                            .size((if (isTablet) 70 else 40).dp)
                                            .padding(
                                                start = if (isTablet) 16.dp else 8.dp,
                                                end = if (isTablet) 16.dp else 8.dp,
                                                top = if (isTablet) 8.dp else 2.dp,
                                                bottom = if (isTablet) 8.dp else 2.dp
                                            )
                                    )
                                }
                            }
                        }

                        // Cột chứa thông tin tên và email
                        Column(
                            modifier = Modifier.padding(top = if (isTablet) 120.dp else 70.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Hiển thị tên người dùng
                            Text(
                                text = nameState.value,
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isTablet) 40.sp else 25.sp,
                                color = colorScheme.onSecondary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            // Hiển thị ngày tham gia
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Ngày sinh: " + dateCreated.value,
                                    color = colorScheme.onSecondary,
                                    fontSize = if (isTablet) 24.sp else 20.sp
                                )
                            }
                            // Hộp chứa các nút
                            Box(
                                modifier = Modifier
                                    .padding(top = if (isTablet) 24.dp else 14.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(
                                            width = if (isTablet) 480.dp else 350.dp,
                                            height = (if (isTablet) 90.dp else 60.dp) * 2
                                        )
                                        .align(Alignment.Center),
                                ) {
                                    // Nút "Đổi mât khẩu"
                                    Box(
                                        modifier = Modifier
                                            .size(
                                                width = if (isTablet) 260.dp else 190.dp,
                                                height = if (isTablet) 90.dp else 60.dp
                                            )
                                            .clip(CutCornerShape(topEnd = if (isTablet) 90.dp else 60.dp))
                                            .align(Alignment.CenterStart)
                                            .background(
                                                colorScheme.primary,
                                                shape = RoundedCornerShape(
                                                    topStart = 0.dp,
                                                    topEnd = 0.dp,
                                                    bottomStart = 12.dp,
                                                    bottomEnd = 0.dp
                                                )
                                            ),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Button(
                                            onClick = {
                                                //ToDo: Xử lý khi bấm đổi mật khẩu
                                                navController.navigate("${Screens.UpdatePassword.route}/${profile!!.UserID}")
                                            },
                                            modifier = Modifier.fillMaxSize(),
                                            shape = RoundedCornerShape(bottomStart = 12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = colorScheme.primary,
                                                contentColor = colorScheme.onPrimary
                                            )
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(horizontal = if (isTablet) 30.dp else 20.dp),
                                                text = "Đổi mật khẩu",
                                                textAlign = TextAlign.Center,
                                                fontSize = if (isTablet) 18.sp else 14.sp
                                            )
                                        }
                                    }
                                    // Nút "Đăng xuất"
                                    Box(
                                        modifier = Modifier
                                            .size(
                                                width = if (isTablet) 260.dp else 190.dp,
                                                height = if (isTablet) 90.dp else 60.dp
                                            )
                                            .clip(CutCornerShape(bottomStart = (if (isTablet) 90.dp else 60.dp) / 1.5f))
                                            .align(Alignment.CenterEnd)
                                            .background(
                                                colorScheme.error,
                                                shape = RoundedCornerShape(
                                                    topStart = 0.dp,
                                                    bottomStart = 0.dp,
                                                    bottomEnd = 12.dp
                                                )
                                            ),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Button(
                                            onClick = {
                                                /* TODO: Xử lý sự kiện nút bấm đăng xuất */
                                                showAlertDialog = true
                                            },
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CutCornerShape(bottomStart = (if (isTablet) 90.dp else 60.dp) / 1.5f)),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = colorScheme.error,
                                                contentColor = colorScheme.onError
                                            ),
                                            shape = RoundedCornerShape(bottomEnd = 12.dp),
                                            elevation = ButtonDefaults.buttonElevation(0.dp)
                                        ) {
                                            Text(
                                                text = "Đăng xuất",
                                                textAlign = TextAlign.Center,
                                                fontSize = if (isTablet) 18.sp else 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))


                // Information Fields
                Column(
                    modifier = Modifier
                        .padding(horizontal = if (isTablet) 32.dp else 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OutlinedTextField(
                        value = nameState.value,
                        onValueChange = {
                            nameState.value = it
                            nameErrorState.value = ValidationUtils.validateFullName(it)
                        },
                        placeholder = { Text("Họ và tên") },
                        shape = RoundedCornerShape(25),
                        singleLine = true,
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
                                imageVector = Icons.Filled.Person,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                    )

                    OutlinedTextField(
                        value = phoneState.value,
                        onValueChange = {
                            phoneState.value = it
                            phoneErrorState.value = ValidationUtils.validatePhoneNumber(it)
                        },
                        placeholder = { Text("Số điện thoại") },
                        shape = RoundedCornerShape(25),
                        singleLine = true,
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
                                imageVector = Icons.Filled.PhoneAndroid,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                    )

                    OutlinedTextField(
                        value = locationState.value,
                        onValueChange = {
                            locationState.value = it
                            locationErrorState.value = ValidationUtils.validateAddress(it)
                        },
                        placeholder = { Text("Nơi sống hiện tại") },
                        shape = RoundedCornerShape(25),
                        singleLine = true,
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
                                imageVector = Icons.Filled.AddLocation,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                    )

                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = {
                            //ToDo: Bổ sung sự kiện
                        },
                        placeholder = { Text("Email") },
                        shape = RoundedCornerShape(25),
                        singleLine = true,
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = if (isVerified.value) Icons.Default.CheckCircle else Icons.Default.Error,
                                contentDescription = "Verified",
                                modifier = Modifier.clickable(
                                    onClick = {
                                        if (!isVerified.value) {
                                            navController.navigate(
                                                Screens.OTP.createRoute(
                                                    "email_verification",
                                                    emailState.value
                                                )
                                            )
                                        }
                                    }
                                ),
                                tint = if (isVerified.value) Color.Green else Color.Red
                            )
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
                                imageVector = Icons.Filled.Email,
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                    )


                    // Trường ngày sinh
                    OutlinedTextField(
                        value = dateCreated.value, // Chỗ này bị bug chưa load được ngày từ biến
                        onValueChange = { /* Không cần xử lý vì đây là trường chỉ đọc */ },
                        placeholder = { Text("Ngày sinh (dd/MM/yyyy)") },
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

                    Log.e("dateCreated.value 5", dateCreated.value.toString())

                    if (showDatePicker) {
                        Popup(
                            onDismissRequest = { showDatePicker = false },
                            alignment = Alignment.Center
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

                    //Nếu thay đổi ngày thì update
                    LaunchedEffect(datePickerState.selectedDateMillis) {
                        datePickerState.selectedDateMillis?.let { millis ->
                            try {
                                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                selectedDate =
                                    formatter.format(Date(millis)) // Hiển thị theo định dạng
                                dateCreated.value = selectedDate // Cập nhật giá trị trường nhập
                                Log.e("SelectedDate", "Ngày chọn: $selectedDate")
                            } catch (e: Exception) {
                                Log.e("DatePickerError", "Lỗi xử lý ngày chọn: ${e.message}")
                            }
                        }
                    }

                    // Save Button
                    Button(
                        onClick = {
                            try {
                                val inputFormat =
                                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                val outputFormat =
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val formattedDate = outputFormat.format(
                                    inputFormat.parse(dateCreated.value) ?: Date()
                                )

                                // Chuyển đổi ảnh hiện tại thành Base64
                                val profileImageBase64 = displayedAvatarBitmap.value?.let {
                                    ImageProcessing.bitmapToBase64(it)
                                } ?: ""

                                val userRequest = UserRequest(
                                    Name = nameState.value.ifBlank { "Tên không được để trống" },
                                    Email = emailState.value.ifBlank { "example@gmail.com" },
                                    Phone = phoneState.value.ifBlank { "0123456789" },
                                    Address = locationState.value.ifBlank { "Chưa nhập địa chỉ" },
                                    DateOfBirth = formattedDate,
                                    ProfileImage = profileImage
                                )
                                Log.e("DateToServer", "Ngày gửi lên server: $formattedDate")
                                profileViewModel.putInfoProfile(profile!!.UserID, userRequest)
                            } catch (e: Exception) {
                                Log.e("SaveError", "Lỗi lưu thông tin: ${e.message}")
                            }
                        },
                        modifier = Modifier
                            .width(if (isTablet) 300.dp else 200.dp)
                            .height(if (isTablet) 56.dp else 48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            "Lưu thông tin",
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
            bitmap = ImageProcessing.resizeBitmap(
                bitmap,
                bitmap.width / 2,
                bitmap.height / 2
            ) // Thu nhỏ ảnh
        }

        currentQuality -= 10 // Giảm chất lượng ảnh
    } while (outputStream.size() / 1024 > maxFileSizeKB && currentQuality > 10)

    return outputStream.toByteArray()
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}