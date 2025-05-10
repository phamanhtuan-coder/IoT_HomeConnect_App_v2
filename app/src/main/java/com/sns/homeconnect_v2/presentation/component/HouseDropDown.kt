package com.sns.homeconnect_v2.presentation.component

import IoTHomeConnectAppTheme
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.common.util.DeviceProperties.isTablet
import com.sns.homeconnect_v2.data.remote.dto.response.house.House
import com.sns.homeconnect_v2.data.remote.dto.response.house.HouseResponse
import com.sns.homeconnect_v2.presentation.viewmodel.component.HouseDropDownViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.component.HouseState
import com.sns.homeconnect_v2.presentation.viewmodel.component.SharedViewModel

/**
 * Thanh dropdown chọn House
 * -----------------------------------------
 * - Người viết: Phạm Anh Tuấn
 * - Ngày viết: 10/12/2024
 * - Lần cập nhật cuối: 11/12/2024
 *
 * -----------------------------------------
 *
 * @param onManageHouseClicked: Hàm xử lý khi click vào nút "Quản lý danh sách nhà"
 * @return Column chứa thanh dropdown chọn House
 *
 * ---------------------------------------
 *
 */
@Composable
fun HouseSelection(
    sharedViewModel: SharedViewModel = hiltViewModel(),
    houseDropDownViewModel: HouseDropDownViewModel = hiltViewModel(),
    onTabSelected: (Int) -> Unit,
    onManageHouseClicked: () -> Unit = {}
) {
    val isTablet = isTablet(LocalContext.current)
    IoTHomeConnectAppTheme {
        var houses by remember { mutableStateOf<List<HouseResponse>>(emptyList()) }
        val housesListState by houseDropDownViewModel.houseListState.collectAsState()

        LaunchedEffect(Unit) {
            houseDropDownViewModel.getListHouse()
        }

        var selectedItem by remember {
            mutableStateOf(House(houseId = -1, name = "Không có nhà", address = ""))
        }

        when (housesListState) {
            is HouseState.Error -> {
                Log.d("Error", (housesListState as HouseState.Error).error)
            }
            is HouseState.Success -> {
                houses = (housesListState as HouseState.Success).houseList
                Log.d("List House", houses.toString())

                if (houses.isNotEmpty() && selectedItem.houseId == -1) {
                    val firstHouse = houses.first().data
                    if (firstHouse != null) {
                        selectedItem = firstHouse
                        onTabSelected(firstHouse.houseId)
                        sharedViewModel.setHouseId(firstHouse.houseId)
                    }
                }
            }
            else -> {
                /* Do nothing */
            }
        }

        var isDropdownExpanded by remember { mutableStateOf(false) }
        val colorScheme = MaterialTheme.colorScheme
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(6.dp))
                    .background(colorScheme.surface)
                    .clickable {
                        isDropdownExpanded = !isDropdownExpanded
                    }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedItem.name,
                        color = colorScheme.onSurface,
                        fontSize = 18.sp
                    )
                    Log.d("SelectedItem", "Name: ${selectedItem.name}, ID: ${selectedItem.houseId}")
                    Icon(
                        imageVector = if (isDropdownExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = colorScheme.primary
                    )
                }
            }

            if (isDropdownExpanded) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorScheme.surfaceVariant)
                        .padding(vertical = 4.dp)
                ) {
                    houses.forEach { houseResponse ->
                        val house = houseResponse.data
                        if (house != null) {
                            Text(
                                text = house.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedItem = house
                                        onTabSelected(house.houseId)
                                        sharedViewModel.setHouseId(house.houseId)
                                        isDropdownExpanded = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                fontSize = 16.sp,
                                color = colorScheme.onSurface
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                onManageHouseClicked()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (isTablet) 56.dp else 48.dp)
                                .background(colorScheme.primary, RoundedCornerShape(18.dp)),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Quản lý danh sách nhà",
                                color = colorScheme.onPrimary,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}