package com.sns.homeconnect_v2.presentation.screen.house

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.core.util.validation.toHouseUi
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.HouseCardSwipeable
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.GenericDropdown
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.LabeledBox
import com.sns.homeconnect_v2.presentation.component.widget.RadialFab
import com.sns.homeconnect_v2.presentation.model.FabChild
import com.sns.homeconnect_v2.presentation.viewmodel.house.HouseSearchViewModel

@Composable
fun HouseSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: HouseSearchViewModel = hiltViewModel(),
    navController: NavHostController
) {
    var current by remember { mutableStateOf<String?>(null) }

    val houses by viewModel.houses.collectAsState()
    val housesUi = houses.map { it.toHouseUi() }

    LaunchedEffect(Unit) {
        viewModel.loadHousesByGroup() // mặc định groupId = 5
    }

    Log.d("HouseSearchScreen", "Houses: $houses")

    //    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    //    val isLoading by viewModel.isLoading.collectAsState()

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
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
                    title         = "Danh sách nhà"
                )
            },
            containerColor = Color.White,
            floatingActionButton = {
                RadialFab(
                    items      = fabChildren,
                    radius     = 120.dp,        // ↑ bán kính ≥ mainSize + miniSize
                    startDeg   = -90f,          // góc bắt đầu –90° (mở thẳng lên)
                    sweepDeg   = -90f,         // quét 90°
                    onParentClick = { /* add new group */ }
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = {
                MenuBottom(navController)
            }
        ) { inner ->
            Column (
                modifier= Modifier.padding(inner)
            ) {
                ColoredCornerBox(
                    cornerRadius = 40.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        GenericDropdown(
                            items = listOf("Phòng khách", "Phòng ngủ", "Nhà bếp"),
                            selectedItem = current,
                            onItemSelected = { current = it },
                            isTablet = false,
                            leadingIcon = Icons.Default.Home
                        )
                    }
                }

                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LabeledBox(
                            label = "Số nhà",
                            value = "${housesUi.size}",
                        )
                    }
                }

                LazyColumn(modifier = modifier
                    .fillMaxSize()
                ) {
                    itemsIndexed( housesUi) { index, house ->
                        Spacer(Modifier.height(8.dp))
                        HouseCardSwipeable(
                            houseName  = house.name,        // HouseUi.name
                            spaceCount = house.spaces,      // HouseUi.spaces
                            icon       = house.icon,        // HouseUi.icon
                            iconColor  = house.iconColor,   // HouseUi.iconColor
                            isRevealed = house.isRevealed,  // HouseUi.isRevealed
                            onExpandOnly = { /* ... */ },
                            onCollapse   = { /* ... */ },
                            onDelete     = { /* ... */ },
                            onEdit       = { /* ... */ }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "GroupScreen - Phone")
@Composable
fun HouseSearchScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        HouseSearchScreen(navController = rememberNavController())
    }
}