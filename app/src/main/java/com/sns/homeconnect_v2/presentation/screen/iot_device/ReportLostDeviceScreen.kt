package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.ImagePickerBox
import com.sns.homeconnect_v2.presentation.component.SituationDescriptionField
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Hàm Composable để hiển thị màn hình báo mất thiết bị.
 * Màn hình này cho phép người dùng báo mất thiết bị bằng cách cung cấp họ tên đầy đủ, địa chỉ email,
 * mô tả sự cố và hình ảnh làm bằng chứng sở hữu.
 *
 * @param navController [NavHostController] được sử dụng để điều hướng.
 * @author Nguyễn Thanh Sang
 * @since 20-05-2025
 * @updatedBy Nguyễn Thanh Sang
 * @updatedDate 26-05-2025
 */

@Composable
fun ReportLostDeviceScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    IoTHomeConnectAppTheme {
        var fullName by remember { mutableStateOf("") }
        var emailAddress by remember { mutableStateOf("") }
        var incidentDescription by remember { mutableStateOf("") }

        val colorScheme = MaterialTheme.colorScheme
        val scope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type          = "Back",
                    title         = "Settings"
                )
            },
            containerColor = Color.White,
            bottomBar = {
                MenuBottom(navController = navController)
            }
        ) { paddingValues ->
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    ColoredCornerBox(
                        cornerRadius = 40.dp
                    ) {
                        Column (
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text          = "",
                                color         = Color.White,
                                fontSize      = 30.sp,
                                fontWeight    = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ) {

                    }

                    Column (
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StyledTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            placeholderText = "Họ và tên",
                            leadingIcon = Icons.Default.Person
                        )

                        StyledTextField(
                            value = emailAddress,
                            onValueChange = { emailAddress = it },
                            placeholderText = "Nhập email",
                            leadingIcon = Icons.Default.Email
                        )

                        SituationDescriptionField(
                            value         = incidentDescription,
                            onValueChange = { incidentDescription = it }
                        )

                        Text(
                            text          = "Hình ảnh minh chứng thiết bị là của shop",
                            color         = Color.Black,
                            fontSize      = 15.sp,
                            fontWeight    = FontWeight.Normal,
                            letterSpacing = 0.5.sp
                        )

                        ImagePickerBox(
                            width = 200.dp,
                            height = 200.dp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally))

                        ActionButtonWithFeedback(
                            label = "Gửi",
                            style = HCButtonStyle.PRIMARY,
                            onAction = { onSend, _ -> scope.launch { delay(1000); onSend("Done") } },
                            snackbarViewModel = snackbarViewModel
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "CreateGroupScreen Preview - Phone")
@Composable
fun ReportLostDeviceScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        ReportLostDeviceScreen(navController = rememberNavController())
    }
}
