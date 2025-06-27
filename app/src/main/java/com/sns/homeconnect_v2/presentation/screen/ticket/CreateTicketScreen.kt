package com.sns.homeconnect_v2.presentation.screen.ticket

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Support
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.data.remote.dto.request.CreateTicketRequest
import com.sns.homeconnect_v2.data.remote.dto.request.Evidence
import com.sns.homeconnect_v2.presentation.component.ImagePickerBox
import com.sns.homeconnect_v2.presentation.component.SituationDescriptionField
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.ticket.CreateTicketViewModel

@Composable
fun CreateTicketScreen(
    navController: NavHostController,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        var title by remember { mutableStateOf("") }
        var deviceSerial by remember { mutableStateOf("") }
        var incidentDescription by remember { mutableStateOf("") }
        var current by remember { mutableStateOf<String?>(null) }
        var imageUrl by remember { mutableStateOf("") }
        var showDialog by remember { mutableStateOf(false) }

        val createTicketViewModel: CreateTicketViewModel = hiltViewModel()
        val createResult by createTicketViewModel.createResult.collectAsState()

        createResult?.let { result ->
            when {
                result.isSuccess -> {
                    showDialog = true
                    createTicketViewModel.resetResult()
                }
                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    val message = when {
                        exception?.message?.contains("ticket_type_id") == true ->
                            "Không tìm thấy người nhận thiết bị được yêu cầu"
                        exception?.message?.contains("device_serial") == true ->
                            "Không tìm thấy thiết bị với serial được yêu cầu"
                        else -> exception?.message ?: "Đã xảy ra lỗi khi tạo yêu cầu"
                    }
                    snackbarViewModel.showSnackbar(message)
                    createTicketViewModel.resetResult()
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    navController.popBackStack()
                },
                title = {
                    Text(text = "Thành công")
                },
                text = {
                    Text(text = "Yêu cầu của bạn đã được gửi đi!")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            navController.popBackStack()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Tạo hỗ trợ"
                )
            },
            containerColor = Color.White,
            bottomBar = {
                MenuBottom(navController = navController)
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    ColoredCornerBox(cornerRadius = 40.dp) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Tạo hỗ trợ",
                                color = Color.White,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ) {}

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StyledTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholderText = "Tiêu đề",
                            leadingIcon = Icons.Default.Person
                        )

                        GenericDropdown(
                            items = listOf("Phòng khách", "Phòng ngủ", "Nhà bếp"),
                            selectedItem = current ?: "Chọn loại hỗ trợ",
                            onItemSelected = { current = it },
                            isTablet = false,
                            placeHolder = "Loại hỗ trợ",
                            leadingIcon = Icons.Default.Support
                        )

                        StyledTextField(
                            value = deviceSerial,
                            onValueChange = { deviceSerial = it },
                            placeholderText = "Serial thiết bị",
                            leadingIcon = Icons.Default.Support
                        )

                        SituationDescriptionField(
                            value = incidentDescription,
                            onValueChange = { incidentDescription = it }
                        )

                        Text(
                            text = "Minh chứng thiết bị là của shop.",
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.5.sp
                        )

                        ImagePickerBox(
                            width = 200.dp,
                            height = 200.dp,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onImagePicked = { url -> imageUrl = url }
                        )

                        Button(
                            onClick = {
                                val ticketTypeId = when (current) {
                                    "Phòng khách" -> 2
                                    "Phòng ngủ" -> 1
                                    "Nhà bếp" -> 2
                                    else -> 0 // Giá trị không hợp lệ
                                }
                                val request = CreateTicketRequest(
                                    title = title,
                                    description = incidentDescription,
                                    ticket_type_id = ticketTypeId,
                                    device_serial = deviceSerial,
                                    evidence = Evidence(
                                        images = listOf(imageUrl),
                                        logs = emptyList<String>()
                                    ),
                                    assigned_to = ""
                                )
                                Log.d("CreateTicketScreen", "Request: $request")
                                createTicketViewModel.createTicket(request)
                            },
                            enabled = title.isNotBlank() && incidentDescription.isNotBlank() && deviceSerial.isNotBlank() && current != null
                        ) {
                            Text("Gửi")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "CreateTicketScreen Preview - Phone")
@Composable
fun CreateTicketScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        CreateTicketScreen(navController = rememberNavController())
    }
}