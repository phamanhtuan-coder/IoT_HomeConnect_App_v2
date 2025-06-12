package com.sns.homeconnect_v2.presentation.screen.group.house

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.core.util.validation.toColor
import com.sns.homeconnect_v2.core.util.validation.toIcon
import com.sns.homeconnect_v2.presentation.component.SpaceCardSwipeable
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.navigation.MenuItem
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.model.FabChild
import com.sns.homeconnect_v2.presentation.model.SpaceUi
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.space.SpaceScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HouseDetailScreen(
    navController: NavHostController,
//    houseIcon: ImageVector,
//    colorIcon: Color,
//    houseName: String,
//    houseAddress: String,
    snackbarViewModel: SnackbarViewModel = hiltViewModel(),
    spaceViewModel:SpaceScreenViewModel = hiltViewModel(),
    houseId:Int
) {

    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        spaceViewModel.getSpaces(houseId)
    }
    val spaces by spaceViewModel.spaces.collectAsState()
    Log.d("spaces", spaces.toString())
    Log.d("houseId", houseId.toString())

//    val spaces = remember {
//        mutableStateListOf(
//            SpaceUi(1, "Bathroom", 5, false, Icons.Default.BedroomBaby, Color.Blue),
//            SpaceUi(2, "Bedroom", 3, false, Icons.Default.Home, Color.Red),
//            SpaceUi(3, "Lounge", 7, false, Icons.Default.Group, Color.Green)
//        )
//    }

    IoTHomeConnectAppTheme {
        val fabOptions = listOf(
            FabChild(
                Icons.Default.Edit,
                onClick = { /* TODO: sửa */ },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ),
            FabChild(
                Icons.Default.Delete,
                onClick = { /* TODO: xoá */ },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ),
            FabChild(
                Icons.Default.Add,
                onClick = { /* TODO: share */ },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            )
        )

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type = "Back",
                    title = "House Settings"
                )
            },
            containerColor = Color.White,
            floatingActionButton = {
                RadialFab(
                    items = fabOptions,
                    radius = 120.dp,
                    startDeg = -90f,
                    sweepDeg = -90f,
                    onParentClick = { /* thêm thành viên */ }
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = {
                MenuBottom(navController)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ColoredCornerBox(cornerRadius = 40.dp) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .height(IntrinsicSize.Min)
                        ) {
                            // Trái: icon + tên + địa chỉ
                            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = "House Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text =  "Nhà của tôi",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.width(250.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EditCalendar,
                                        contentDescription = "Address Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text =  "123 Đường ABC, Quận 1, TP.HCM",
                                        modifier = Modifier.weight(1f),
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            // Phải: nút sửa
                            Column(
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                IconButton(
                                    onClick = { /* TODO: chỉnh sửa house */ },
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .size(76.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit house",
                                        tint = Color.White,
                                        modifier = Modifier.size(46.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {
                    LabeledBox(
                        label = "Không gian",
                        value = spaces.size.toString(),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                LazyColumn {
                    itemsIndexed(spaces) { index, space ->
                        Spacer(Modifier.height(8.dp))

                        SpaceCardSwipeable(
                            spaceName = space.space_name?: "không có tên",
                            deviceCount = space.space_id,
                            icon = space.icon_name?.toIcon() ?: Icons.Default.Home,
                            iconColor = space.icon_color?.toColor()?: Color.Gray,
                            isRevealed = space.isRevealed,
                            onExpandOnly = {
                                spaces.indices.forEach { i ->
                                    spaceViewModel.updateRevealState(i)
                                }
                            },
                            onCollapse = {
                                spaceViewModel.collapseItem(index)
                            },
                            onDelete = {  },
                            onEdit = { /* TODO */ },
                            onClick = { /* TODO */ }
                        )
                    }

                    item {
                        Spacer(Modifier.height(8.dp))

                        ActionButtonWithFeedback(
                            label = "Cập nhật",
                            style = HCButtonStyle.PRIMARY,
                            onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } },
                            modifier = Modifier.padding(horizontal = 16.dp),
                            snackbarViewModel = snackbarViewModel
                        )
                        Spacer(Modifier.height(8.dp))
                        ActionButtonWithFeedback(
                            label = "Xoá",
                            style = HCButtonStyle.DISABLED,
                            onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } },
                            modifier = Modifier.padding(horizontal = 16.dp),
                            snackbarViewModel = snackbarViewModel
                        )
                    }
                }
            }
        }
    }
}

