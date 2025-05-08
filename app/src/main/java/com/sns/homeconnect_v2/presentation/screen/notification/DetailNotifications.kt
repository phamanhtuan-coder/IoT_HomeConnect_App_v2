package com.sns.homeconnect_v2.presentation.screen.notification

import IoTHomeConnectAppTheme
import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.core.util.rememberResponsiveLayoutConfig
import com.sns.homeconnect_v2.data.remote.dto.response.AlertDetail
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.viewmodel.notification.NotificationDetailState
import com.sns.homeconnect_v2.presentation.viewmodel.notification.NotificationDetailViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.notification.NotificationReadState

/** Giao diện màn hình Chi tiết thông báo (DetailNotification)
 * -----------------------------------------
 * Người viết: Nguyễn Thanh Sang
 * Ngày viết: 10/12/2024
 * Lần cập nhật cuối: 31/12/2024
 * -----------------------------------------
 * @param navController Đối tượng điều khiển điều hướng.
 * @param alertId ID của thông báo.
 * @param viewModel ViewModel cho chi tiết thông báo.
 * @return Scaffold chứa toàn bộ nội dung
 * ---------------------------------------
 */
@Composable
fun DetailNotificationScreen(
    navController: NavHostController,
    alertId: Int,
    viewModel: NotificationDetailViewModel = hiltViewModel(),
) {
    val layoutConfig = rememberResponsiveLayoutConfig()
    val notificationState by viewModel.alertState.collectAsState()
    val notificationReadState by viewModel.alertReadState.collectAsState()

    // Handle navigation after reading notification
    when (notificationReadState) {
        is NotificationReadState.Success -> {
            navController.popBackStack(Screens.AllNotifications.route, inclusive = false)
        }
        is NotificationReadState.Error -> {
            val error = (notificationReadState as NotificationReadState.Error).error
            Log.d("Lỗi: ", error)
        }
        else -> {
            /* Do nothing */
        }
    }

    // Fetch alert details
    LaunchedEffect(Unit) {
        viewModel.getAlertById(alertId)
    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = colorScheme.background,
            topBar = {
                Header(
                    type = "Notification",
                    title = when (notificationState) {
                        is NotificationDetailState.Success -> {
                            val alert = (notificationState as NotificationDetailState.Success).alert
                            if (alert.typeId == 1) "Chi tiết cảnh báo" else "Chi tiết thông báo"
                        }
                        else -> "Chi tiết thông báo"
                    },
                    navController = navController
                )
            },
            bottomBar = {
                MenuBottom(navController = navController)
            },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(bottom = layoutConfig.outerPadding),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        when (notificationState) {
                            is NotificationDetailState.Success -> {
                                val alert = (notificationState as NotificationDetailState.Success).alert
                                NotificationDetailContent(alert, viewModel, layoutConfig)
                            }
                            is NotificationDetailState.Error -> {
                                Text(
                                    text = (notificationState as NotificationDetailState.Error).error,
                                    color = colorScheme.error,
                                    fontSize = layoutConfig.textFontSize,
                                    modifier = Modifier.padding(layoutConfig.outerPadding)
                                )
                            }
                            is NotificationDetailState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = colorScheme.primary)
                            }
                        }
                            is NotificationDetailState.Idle -> {
                                // Do nothing or show placeholder
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun NotificationDetailContent(
    alert: AlertDetail,
    viewModel: NotificationDetailViewModel,
    layoutConfig: com.sns.homeconnect_v2.core.util.LayoutConfig
) {
    val colorScheme = MaterialTheme.colorScheme
    // Box lớn chứa phần tiêu đề và các thành phần bên trong
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = colorScheme.background)
    ) {
        Column {
            // Hộp màu xanh dương bo tròn góc dưới bên trái
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = colorScheme.primary,
                        shape = RoundedCornerShape(bottomStart = 40.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(layoutConfig.outerPadding)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = alert.alertType.alertTypeName,
                            fontSize = layoutConfig.headingFontSize,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))

                    // Hàng chứa tiêu đề và thông tin ngày giờ
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = layoutConfig.outerPadding)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "Status",
                                tint = colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (alert.status) "Đã xem" else "Chưa xem",
                                fontSize = layoutConfig.textFontSize,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AccessTime,
                                contentDescription = "Time",
                                tint = colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = alert.timestamp,
                                fontSize = layoutConfig.textFontSize,
                                color = colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
            // Box chứa góc lõm màu xám
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(
                    modifier = Modifier
                        .size(layoutConfig.cornerBoxSize)
                        .align(Alignment.TopEnd)
                        .background(color = colorScheme.primary)
                        .zIndex(1f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(layoutConfig.cornerBoxSize)
                        .background(
                            color = colorScheme.background,
                            shape = RoundedCornerShape(topEndPercent = layoutConfig.cornerBoxRadius)
                        )
                        .zIndex(2f),
                    contentAlignment = Alignment.Center
                ) {}
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(
                    1.dp,
                    colorScheme.onBackground,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(layoutConfig.outerPadding)
        ) {
            Text(
                text = alert.message,
                fontSize = layoutConfig.textFontSize,
                color = colorScheme.onBackground,
                softWrap = true,
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.height(layoutConfig.textFieldSpacing))

        // Nút "ĐỌC THÔNG BÁO"
        Button(
            onClick = {
                viewModel.readNotification(alertId = alert.alertId)
            },
            modifier = Modifier
                .width(200.dp)
                .height(48.dp),
            shape = RoundedCornerShape(50),
            enabled = !alert.status,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary
            )
        ) {
            Text(
                text = "Xác nhận đã đọc",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorScheme.onPrimary
            )
        }
    }
}