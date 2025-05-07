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
import com.sns.homeconnect_v2.data.remote.dto.response.HouseResponse
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
    onManageHouseClicked: () -> Unit = {} // Callback for managing house
) {
    val isTablet =
        isTablet(LocalContext.current) // Kiểm tra xem thiết bị có phải là tablet hay không
    IoTHomeConnectAppTheme {
        var houses by remember { mutableStateOf<List<HouseResponse>>(emptyList()) } // Lắng nghe danh sách thiết bị

        val housesListState by houseDropDownViewModel.houseListState.collectAsState()
        LaunchedEffect(1) {
//        selectedSpaceId?.let { spaceId ->
//            viewModel.loadDevices(spaceId) // Tải danh sách thiết bị khi Space thay đổi
//        }
            houseDropDownViewModel.getListHouse()
        }
        var selectedItem by remember {
            mutableStateOf(HouseResponse(HouseID = -1, Name = "Không có nhà"))
        }

        when (housesListState) {
            is HouseState.Error -> {
                Log.d("Error", (housesListState as HouseState.Error).error)
            }

            is HouseState.Success -> {
                houses = (housesListState as HouseState.Success).houseList
                Log.d("List House", houses.toString())

                // Kiểm tra nếu danh sách `houses` trống
                if (houses.isNotEmpty() && selectedItem.HouseID == -1) {
                    selectedItem = houses.first()  // Mặc định chọn item đầu tiên
                    onTabSelected(selectedItem.HouseID)
                    sharedViewModel.setHouseId(selectedItem.HouseID)
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
                        text = selectedItem.Name,
                        color = colorScheme.onSurface,
                        fontSize = 18.sp
                    )
                    Log.d("SelectedItem", "Name: ${selectedItem.Name}, ID: ${selectedItem.HouseID}")
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
                    houses.forEach { house ->
                        Text(
                            text = house.Name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedItem = house // Cập nhật `selectedItem`
                                    onTabSelected(house.HouseID) // Gọi callback để xử lý
                                    sharedViewModel.setHouseId(house.HouseID) // Lưu trạng thái
                                    isDropdownExpanded = false // Đóng dropdown
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            fontSize = 16.sp,
                            color = colorScheme.onSurface
                        )
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