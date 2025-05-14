package com.sns.homeconnect_v2.presentation.screen.group

import IoTHomeConnectAppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuItem
import com.sns.homeconnect_v2.presentation.component.widget.*

@Composable
fun DetailGroupScreen(navController: NavHostController) {
    /* ---------------------------------------------------------
       1.  Toàn bộ dữ liệu “cứng” đưa thành biến ở đầu hàm.
           Khi dùng ViewModel, bạn chỉ cần thay thế giá trị cho
           các biến này (hoặc truyền chúng qua tham số).
    ----------------------------------------------------------*/
    val selectedIcon = Icons.Default.Home

    val groupName        = "Family Group"
    val groupDescription = "Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group Mô tả group "
    val users = listOf(
        Triple("Nguyễn Văn A", "Owner",  ""),
        Triple("Trần Thị B" , "Vice" ,  "https://i.pravatar.cc/150?img=8"),
        Triple("Lê Văn C"   , "Admin",  "https://i.pravatar.cc/150?img=12"),
        Triple("Phạm Thị D" , "Member", ""),
        Triple("Hoàng Văn E", "Member", "https://i.pravatar.cc/150?img=20")
    )

    val memberCount = users.size.toString()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Thành viên", "Nhà")

    val items = listOf(
        "Dashboard" to Pair(Icons.Filled.PieChart, "dashboard"),
        "Devices" to Pair(Icons.Filled.Devices, "devices"),
        "Home" to Pair(Icons.Filled.Home, "home"),
        "Profile" to Pair(Icons.Filled.Person, "profile"),
        "Settings" to Pair(Icons.Filled.Settings, "settings")
    )
    val context = LocalContext.current
    val isTablet = isTablet(context)

    // Track the last selected route
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    /* ---------------------------------------------------------
       2.  State chỉ dùng cho UI (điều khiển swipe).
    ----------------------------------------------------------*/
    val revealStates = remember { users.map { mutableStateOf(false) } }

    /* ---------------------------------------------------------
       3.  Các nút FAB con.
    ----------------------------------------------------------*/
    val fabChildren = listOf(
        FabChild(icon = Icons.Default.Edit  , onClick = { /* sửa group */ }),
        FabChild(icon = Icons.Default.Delete, containerColor = Color.Red, onClick = { /* xoá group */ }),
        FabChild(icon = Icons.Default.Share , onClick = { /* share group */ })
    )

    /* ---------------------------------------------------------
       4.  UI chính
    ----------------------------------------------------------*/
    IoTHomeConnectAppTheme {
        val fabChildren = listOf(
            FabChild(
                icon = Icons.Default.Edit,
                onClick = { /* TODO: sửa */ },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ),
            FabChild(
                icon = Icons.Default.Delete,
                onClick = { /* TODO: xoá */ },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ),
            FabChild(
                icon = Icons.Default.Share,
                onClick = { /* TODO: share */ },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            )
        )

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type          = "Back",
                    title         = "Settings"
                )
            },
            containerColor = Color.White,
            floatingActionButton = {
                RadialFab(
                    items            = fabChildren,
                    radius           = 120.dp,
                    startDeg         = -90f,
                    sweepDeg         = -90f,
                    onParentClick    = { /* thêm thành viên */ }
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = {
                BottomAppBar(
                    tonalElevation = 4.dp,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.height(120.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Top
                    ) {
                        items.forEach { item ->
                            val isSelected = currentRoute == item.second.second
                            MenuItem(
                                text = item.first,
                                icon = item.second.first,
                                isSelected = isSelected,
                                onClick = {
                                    navController.navigate(item.second.second) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                isTablet = isTablet,
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                /* ------------ HEADER MÀU ĐẬM (có góc bo) -------------- */
                ColoredCornerBox(
                    cornerRadius    = 40.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        /* ----- Thông tin tên & mô tả group ----- */
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .height(IntrinsicSize.Min)
                        ) {
                            /*  Phần trái: icon – tên – mô tả  */
                            Column(
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                // Tên
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = selectedIcon,
                                        contentDescription = "Group Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = groupName,
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(Modifier.height(12.dp))

                                // Mô tả
                                Row(
                                    modifier = Modifier
                                        .width(250.dp),               // hàng đủ rộng
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EditCalendar,
                                        contentDescription = "Description Icon",
                                        tint  = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))

                                    Text(
                                        text = groupDescription,
                                        modifier = Modifier.weight(1f),               // <── quan trọng
                                        color  = Color.White,
                                        fontSize = 16.sp,
                                        // tuỳ chọn: tối đa 2 dòng, nếu dài hơn sẽ có dấu …
                                        maxLines  = 2,
                                        overflow  = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Column (
                                modifier = Modifier
                                    .align(Alignment.CenterEnd),
                            ) {
                                /*  Phần phải: avatar chồng lên nhau  */
                                IconButton(
                                    onClick = { /* TODO: rời group */ },
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .size(76.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ExitToApp,
                                        contentDescription = "Exit group",
                                        tint = Color.Red,
                                        modifier = Modifier.size(46.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                /* ------------ KHUNG TRẮNG BO GÓC NGƯỢC -------------- */
                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {
                    CustomTabRow(
                        tabs = tabTitles,
                        selectedTabIndex = selectedTab,
                        onTabSelected = { selectedTab = it },
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    // --- thanh tìm kiếm ---
                    SearchBar(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        onSearch = { /* TODO */ }
                    )

                    Spacer(Modifier.height(8.dp))

                    // --- hàng thống kê ---
                    LabeledBox(
                        label = "Số lượng thành viên",
                        value = memberCount,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                /* ------------ DANH SÁCH THÀNH VIÊN -------------- */
                val bottomPadding = innerPadding.calculateBottomPadding()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),                // tránh gesture-bar nếu có
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        bottom = bottomPadding-32.dp       // ❶  đẩy cao hơn BottomBar/FAB
                    )
                ) {
                    items(users.size) { index ->
                        val (name, role, avatar) = users[index]
                        UserCardSwipeable(
                            userName    = name,
                            role        = role,
                            avatarUrl   = avatar,
                            isRevealed  = revealStates[index].value,
                            onExpandOnly = {
                                revealStates.forEachIndexed { i, state ->
                                    state.value = i == index
                                }
                            },
                            onCollapse = { revealStates[index].value = false },
                            onDelete   = { /* xoá user */ },
                            onEdit     = { /* chỉnh sửa role */ }
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name          = "Phone – 360 × 800",
    showBackground = true,
    widthDp       = 360,
    heightDp      = 800
)
@Composable
fun DetailGroupPhonePreview() {
    DetailGroupScreen(navController = rememberNavController())
}
