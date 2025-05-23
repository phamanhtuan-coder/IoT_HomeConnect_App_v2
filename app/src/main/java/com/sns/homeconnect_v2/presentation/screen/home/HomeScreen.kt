package com.sns.homeconnect_v2.presentation.screen.home

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceShare
import com.sns.homeconnect_v2.data.remote.dto.response.SharedWithResponse
import com.sns.homeconnect_v2.presentation.component.DeviceCard
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.navigation.DrawerWithContent
import com.sns.homeconnect_v2.presentation.component.WeatherInfo
import com.sns.homeconnect_v2.presentation.viewmodel.home.HomeScreenViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.home.SharedWithState
import com.sns.homeconnect_v2.presentation.viewmodel.profile.InfoProfileState
import com.sns.homeconnect_v2.presentation.viewmodel.profile.ProfileScreenViewModel


/** Giao diện màn hình Trang chủ (Home Screen)
 * -----------------------------------------
 * Người viết: Phạm Anh Tuấn
 * Ngày viết: 29/11/2024
 * Lần cập nhật cuối: 23/05/2025
 * -----------------------------------------

 * @param modifier Modifier mở rộng để áp dụng cho layout (đã gán giá trị mặc dịnh).
 * @param navController Đối tượng điều khiển điều hướng.
 * @return Scaffold chứa toàn bộ nội dung của màn hình Trang chủ.
 *
 * ---------------------------------------
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    homeViewModel: HomeScreenViewModel = hiltViewModel(),
    profileViewModel: ProfileScreenViewModel = hiltViewModel()
) {

    var userId by remember { mutableStateOf(1) } // Default userId for demo
    val infoProfileState by profileViewModel.infoProfileState.collectAsState()

    // TODO: Re-enable API call when new API is ready
    /*
    LaunchedEffect(Unit) {
        profileViewModel.getInfoProfile()
    }

    when (infoProfileState) {
        is InfoProfileState.Loading -> {

        }

        is InfoProfileState.Success -> {
            userId = (infoProfileState as InfoProfileState.Success).user.UserID
            Log.d("InfoProfileState", userId.toString())
        }

        is InfoProfileState.Error -> {
            Log.d("InfoProfileState", (infoProfileState as InfoProfileState.Error).error)
        }

        else -> {}
    }
    */

    val state by homeViewModel.sharedWithState.collectAsState()
    var sharedUsers by remember { mutableStateOf<List<SharedWithResponse>?>(emptyList()) }

    // TODO: Re-enable API call when new API is ready
    /*
    LaunchedEffect(userId) {
        homeViewModel.fetchSharedWith(userId)
    }

    when (state) {
        is SharedWithState.Loading -> {
            // Loading state handling
        }

        is SharedWithState.Success -> {
            sharedUsers = (state as SharedWithState.Success).sharedWith
            Log.d("SharedWithState", sharedUsers.toString())
        }

        is SharedWithState.Error -> {
            Log.d("SharedWithState", (state as SharedWithState.Error).error)
        }

        else -> {}
    }
    */

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        // Get username from profile state
        val username = when (infoProfileState) {
            is InfoProfileState.Success -> (infoProfileState as InfoProfileState.Success).user.Name
            else -> "Chúc bạn có một ngày tốt lành!"
        }

        // Using DrawerWithContent instead of direct Scaffold
        DrawerWithContent(
            navController = navController,
            type = "Home",
            username = username,
            content = {
                Scaffold(
                    containerColor = colorScheme.background,
                    modifier = modifier.fillMaxSize(),
                    bottomBar = {
                        /*
                        * Hiển thị Thanh Menu dưới cùng
                        */
                        MenuBottom(navController)
                    },
                    content = { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            WeatherInfo()

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Thiết bị được chia sẻ
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Thiết bị được chia sẻ",
                                            color = colorScheme.onBackground,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        TextButton(
                                            onClick = { /* TODO: Navigate to all devices */ },
                                            colors = ButtonDefaults.textButtonColors(
                                                contentColor = Color.Blue
                                            )
                                        ) {
                                            Text(text = "Xem tất cả")
                                        }
                                    }

                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(4) { index ->
                                            val device = when (index) {
                                                0 -> SharedWithResponse(
                                                    PermissionID = 1,
                                                    DeviceID = 101,
                                                    SharedWithUserID = 1,
                                                    CreatedAt = "2025-05-23",
                                                    Device = DeviceShare(
                                                        DeviceID = 101,
                                                        Name = "Đèn phòng khách",
                                                        TypeID = 2
                                                    )
                                                )
                                                1 -> SharedWithResponse(
                                                    PermissionID = 2,
                                                    DeviceID = 102,
                                                    SharedWithUserID = 1,
                                                    CreatedAt = "2025-05-23",
                                                    Device = DeviceShare(
                                                        DeviceID = 102,
                                                        Name = "Cảm biến khói",
                                                        TypeID = 1
                                                    )
                                                )
                                                2 -> SharedWithResponse(
                                                    PermissionID = 3,
                                                    DeviceID = 103,
                                                    SharedWithUserID = 1,
                                                    CreatedAt = "2025-05-23",
                                                    Device = DeviceShare(
                                                        DeviceID = 103,
                                                        Name = "Đèn ngủ",
                                                        TypeID = 2
                                                    )
                                                )
                                                else -> SharedWithResponse(
                                                    PermissionID = 4,
                                                    DeviceID = 104,
                                                    SharedWithUserID = 1,
                                                    CreatedAt = "2025-05-23",
                                                    Device = DeviceShare(
                                                        DeviceID = 104,
                                                        Name = "Cửa thông minh",
                                                        TypeID = 3
                                                    )
                                                )
                                            }

                                            DeviceCard(
                                                device = device,
                                                navigator = navController,
                                                deviceName = device.Device.Name,
                                                deviceType = when (device.Device.TypeID) {
                                                    1 -> "Báo cháy"
                                                    2 -> "Đèn led"
                                                    3 -> "Cửa thông minh"
                                                    else -> "Không xác định"
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}