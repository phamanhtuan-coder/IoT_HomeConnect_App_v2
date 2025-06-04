package com.sns.homeconnect_v2.presentation.screen.home

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BorderAll
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.DeviceStatCardCarousel
import com.sns.homeconnect_v2.presentation.component.FeatureButtonRow
import com.sns.homeconnect_v2.presentation.component.FeatureButtonSection
import com.sns.homeconnect_v2.presentation.component.SlideShowBanner
import com.sns.homeconnect_v2.presentation.component.navigation.DrawerWithContent
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.model.DeviceStatCardItem
import com.sns.homeconnect_v2.presentation.model.FeatureButtonItem
import com.sns.homeconnect_v2.presentation.model.SlideShowItem


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
//    homeViewModel: HomeScreenViewModel = hiltViewModel(),
//    profileViewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val dummyList = listOf(
        SlideShowItem("https://picsum.photos/id/1/600/300"),
        SlideShowItem("https://picsum.photos/id/2/600/300"),
        SlideShowItem("https://picsum.photos/id/3/600/300")
    )
    val featureButtons = listOf(
        FeatureButtonItem(Icons.Default.Add, "Thêm thiết bị") {},
        FeatureButtonItem(Icons.Default.Wifi, "Kết nối Wifi") {},
        FeatureButtonItem(Icons.Default.Upload, "Chia sẻ thiết bị") {},
        FeatureButtonItem(Icons.Default.PhoneAndroid, "Thiết bị của tôi") {}
    )
    val deviceStats = listOf(
        DeviceStatCardItem(Icons.Default.Memory , Color(0xFFF54B63), "Tổng số thiết bị", "12 thiết bị"),
        DeviceStatCardItem(Icons.Default.Wifi   , Color(0xFF23D37F), "Đang hoạt động" ,  "10 thiết bị"),
        DeviceStatCardItem(Icons.Default.WifiOff, Color(0xFFF54B63), "Mất kết nối"     ,  "2 thiết bị")
    )
    val sampleItems = listOf(
        FeatureButtonItem(Icons.Default.Warning, "Báo mất\nthiết bị") {},
        FeatureButtonItem(Icons.Default.Sync, "Chuyển\nquyền sở hữu") {},
        FeatureButtonItem(Icons.Default.Home, "Quản lý nhà") {},
        FeatureButtonItem(Icons.Default.GridView, "Phòng") {},
        FeatureButtonItem(Icons.Default.Folder, "Nhóm thiết bị") {},
        FeatureButtonItem(Icons.Default.BorderAll, "Lịch sử\nhoạt động") {},
        FeatureButtonItem(Icons.Default.Download, "Cập nhật\nphần mềm") {}
    )

//    var sharedUsers by remember { mutableStateOf<List<SharedWithResponse>?>(emptyList()) }
//    var userId by remember { mutableIntStateOf(0) }
//    val infoProfileState by profileViewModel.infoProfileState.collectAsState()
//    LaunchedEffect(Unit) {
//        profileViewModel.getInfoProfile()
//    }
//    when (infoProfileState) {
//        is InfoProfileState.Loading -> {
//
//        }
//
//        is InfoProfileState.Success -> {
//            userId = (infoProfileState as InfoProfileState.Success).user.UserID
//            Log.d("InfoProfileState", userId.toString())
//        }
//
//        is InfoProfileState.Error -> {
//            Log.d("InfoProfileState", (infoProfileState as InfoProfileState.Error).error)
//        }
//
//        else -> {}
//    }
//    val state by homeViewModel.sharedWithState.collectAsState()
//    LaunchedEffect(userId) {
//        homeViewModel.fetchSharedWith(userId)
//    }
//    when (state) {
//        is SharedWithState.Loading -> {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                CircularProgressIndicator()
//            }
//        }
//        is SharedWithState.Success -> {
//           sharedUsers = (state as SharedWithState.Success).sharedWithResponse
//            Log.e("SharedWithState.Success", "Thành công")
//        }
//        is SharedWithState.Error -> {
//            val errorMessage = (state as SharedWithState.Error).error
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
//            }
//            Log.e("SharedWithState.Error", "Thất bại")
//        }
//        else -> {}
//    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        DrawerWithContent(
            navController = navController,
            type="Home",
            username="Sang",
            content = {
                Scaffold(
                    containerColor = colorScheme.background,
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        /*
                        * Hiển thị Thanh Menu dưới cùng
                         */
                        MenuBottom(navController)
                    },
                    content = {innerPadding ->
                        LazyColumn (
                            modifier = Modifier
                                .padding(innerPadding).fillMaxSize()
                        ) {
                            item {
                                ColoredCornerBox(
                                    cornerRadius = 24.dp
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(16.dp)
                                    ) {
                                        SlideShowBanner(listSlideShow = dummyList)
                                    }
                                }
                            }
                            item {
                                InvertedCornerHeader(
                                    backgroundColor = colorScheme.surface,
                                    overlayColor = colorScheme.primary
                                ) {}
                            }
                            item {
                                FeatureButtonRow(
                                    items = featureButtons,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            item {
                                DeviceStatCardCarousel(items = deviceStats)
                            }
                            item {
                                FeatureButtonSection(
                                    items = sampleItems,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                )
            }
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}