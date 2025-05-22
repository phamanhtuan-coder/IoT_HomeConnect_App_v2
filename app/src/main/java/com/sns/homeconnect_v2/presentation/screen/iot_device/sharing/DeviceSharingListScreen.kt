package com.sns.homeconnect_v2.presentation.screen.iot_device.sharing

import IoTHomeConnectAppTheme
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.SharedUserCard
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.LabeledBox
import com.sns.homeconnect_v2.presentation.model.SharedUserUi
import com.sns.homeconnect_v2.presentation.navigation.Screens

/**
 * Hàm Composable cho Màn hình Danh sách Chia sẻ Thiết bị.
 * Màn hình này hiển thị danh sách người dùng mà một thiết bị được chia sẻ.
 * Nó cho phép người dùng xem những người dùng được chia sẻ và điều hướng để thêm người dùng mới.
 * -----------------------------------------
 * Người viết: Nguyễn Thanh Sang
 * Ngày viết: 21/05/2025
 * Lần cập nhật cuối: 21/05/2025
 * -----------------------------------------
 * Đầu vào:
 *  @param navController NavHostController để điều hướng.
 *  @param deviceID ID của thiết bị để hiển thị thông tin chia sẻ. Mặc định là 0.
 *  //@param deviceSharingViewModel ViewModel để quản lý dữ liệu chia sẻ thiết bị. (Đã được chú thích)
 *
 * Đầu ra: Scaffold
 *  Hàm trả về một composable Scaffold, cung cấp cấu trúc cơ bản cho màn hình,
 *  bao gồm thanh trên cùng, thanh dưới cùng, nút hành động nổi và khu vực nội dung.
 *  Khu vực nội dung hiển thị danh sách người dùng được chia sẻ hoặc một thông báo nếu không tìm thấy người dùng nào.
 * ---------------------------------------
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceSharingListScreen(
    navController: NavHostController,
    deviceID: Int = 0,
//    deviceSharingViewModel :  DeviceSharingViewModel = hiltViewModel()
) {
    val deviceSharingList = remember {
        mutableStateListOf(
            SharedUserUi(
                id = 1,
                userAvt = "https://i.pravatar.cc/150?img=8",
                userName = "Nguyễn Văn A",
                userEmail = "vana@example.com",
                sharedDate = "20/05/2025"
            ),
            SharedUserUi(
                id = 2,
                userAvt = "https://i.pravatar.cc/150?img=5",
                userName = "Trần Thị B",
                userEmail = "tranb@example.com",
                sharedDate = "19/05/2025"
            ),
            SharedUserUi(
                id = 3,
                userAvt = "https://i.pravatar.cc/150?img=2",
                userName = "Lê Văn C",
                userEmail = "le.c@example.com",
                sharedDate = "18/05/2025"
            ),
            SharedUserUi(
                id = 4,
                userAvt = "https://i.pravatar.cc/150?img=11",
                userName = "Phạm Thị D",
                userEmail = "pham.d@example.com",
                sharedDate = "17/05/2025"
            )
        )
    }


//    var isTablet = isTablet(LocalContext.current)
//    val sharedUsersState by  deviceSharingViewModel.sharedUsersState.collectAsState()
//    var sharedUsersList by remember { mutableStateOf(emptyList<SharedUser>()) }
//    var selectedPermissionId by remember { mutableIntStateOf(-1) }
//    LaunchedEffect(Unit) {
//        deviceSharingViewModel.getSharedUsers(deviceID)
//    }
//    when (sharedUsersState) {
//        is DeviceSharingState.Success -> {
//            sharedUsersList = (sharedUsersState as DeviceSharingState.Success).sharedUsers
//        }
//        is DeviceSharingState.Error -> {
//            // Show error
//            Log.e(
//                "DeviceSharingListScreen",
//                "Error: ${(sharedUsersState as DeviceSharingState.Error).error}"
//            )
//        }
//        else -> {
//            /* Do nothing */
//        }
//    }
//    val revokePermissionState by deviceSharingViewModel.revokePermissionState.collectAsState()
//    when (revokePermissionState) {
//        is DeviceSharingActionState.Success -> {
//            LaunchedEffect(Unit) {
//                deviceSharingViewModel.getSharedUsers(deviceID)
//            }
//        }
//
//        is DeviceSharingActionState.Error -> {
//            Log.e(
//                "DeviceSharingListScreen",
//                "Error: ${(revokePermissionState as DeviceSharingActionState.Error).error}"
//            )
//        }
//        else ->{
//            /* Do nothing*/
//        }
//    }

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        Scaffold(
            topBar = {
                /*
            * Hiển thị Header
             */
                Header(
                    navController = navController,
                    type = "Back",
                    title = "Chi tiết thiết bị"
                )
            },
            bottomBar = {
                /*
            * Hiển thị Thanh Menu dưới cùng
             */
                MenuBottom(navController)
            },
            floatingActionButton = {

                FloatingActionButton(
                    containerColor = colorScheme.primary,
                    onClick = {
                        navController.navigate(Screens.AddSharedUser.route + "?id=$deviceID")
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Thêm người dùng",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp),

                            )
                    }
                )

            },
            containerColor = colorScheme.background,
            modifier = Modifier.fillMaxSize(),
            content = { innerPadding ->
                Column (
                    modifier = Modifier.padding(innerPadding)
                ) {
                    ColoredCornerBox(
                        cornerRadius = 40.dp
                    ) {
                        Text(
                            "DANH SÁCH CHIA SẼ THIẾT BỊ",
                            fontSize = 34.sp, // Font size linh hoạt
                            color = colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(16.dp),
                            maxLines = 2,
                            softWrap = true,
                            textAlign = TextAlign.Center,
                            lineHeight = 42.sp
                        )
                    }
                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LabeledBox(
                                label = "Thiết bị đã chia sẽ",
                                value = deviceSharingList.size.toString(),
                            )
                        }
                    }

                    if (deviceSharingList.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp)
                        ) {
                            itemsIndexed(deviceSharingList) { index, user ->
                                SharedUserCard(
                                    userAvt = user.userAvt,
                                    userName = user.userName,
                                    userEmail = user.userEmail,
                                    sharedDate = user.sharedDate,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            item {
                                Spacer(modifier = Modifier.height(62.dp)) // padding cuối danh sách
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("Không tìm thấy")
                        }
                    }
                }
            }
        )
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DeviceSharingListScreenPreview() {
    DeviceSharingListScreen(navController = rememberNavController())
}