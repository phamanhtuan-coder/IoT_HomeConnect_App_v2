package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.core.util.validation.getIconResByName
import com.sns.homeconnect_v2.data.remote.dto.response.AvgValue
import com.sns.homeconnect_v2.data.remote.dto.response.HourlyValueResponse
import com.sns.homeconnect_v2.presentation.component.*
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.GenericDropdown
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.model.DataPoint
import com.sns.homeconnect_v2.presentation.model.SpaceTab
import com.sns.homeconnect_v2.presentation.viewmodel.group.GetListHouseByGroupViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.group.GroupListViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.space.SpaceAnalyticsViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.space.SpaceScreenViewModel

/**
 * Dashboard hiển thị thống kê cảm biến theo Space.
 * Data lấy từ API /hourly-values/space/{id}
 */

fun List<HourlyValueResponse>.toChartData(
    selector: (AvgValue) -> Double?
): List<DataPoint> =
    map {
        DataPoint(
            it.hour_timestamp.substring(11, 16),       // "HH:mm"
            selector(it.avg_value)?.toFloat() ?: 0f
        )
    }.sortedBy { it.xLabel } // DataPoint(xLabel, yValue)

@Composable
fun DashboardScreen(
    navController: NavHostController
) {
    /* ---------- VIEW‑MODELS ---------- */
    val groupVm      : GroupListViewModel           = hiltViewModel()
    val houseVm      : GetListHouseByGroupViewModel = hiltViewModel()
    val spaceVm      : SpaceScreenViewModel         = hiltViewModel()
    val analyticVm   : SpaceAnalyticsViewModel      = hiltViewModel()

    /* ---------- FLOW → STATE ---------- */
    val groups       by groupVm.groupList.collectAsState()
    val houses       by houseVm.houses.collectAsState()
    val spaces       by spaceVm.spaces.collectAsState()
    val hourlyValues by analyticVm.values.collectAsState()

    /* ---------- UI STATE ---------- */
    var selectedGroup   by remember { mutableStateOf<Int?>(null) }
    var selectedHouse   by remember { mutableStateOf<Int?>(null) }
    var activeSpaceId   by remember { mutableStateOf<String?>(null) }

    /* ---------- INIT ---------- */
    LaunchedEffect(Unit) { groupVm.fetchGroups() }

    /* Khi đổi Space → gọi API */
    LaunchedEffect(activeSpaceId) {
        activeSpaceId?.toIntOrNull()?.let { id ->
            analyticVm.loadHourlyValues(id) // dùng range mặc định trong VM
        }
    }

    IoTHomeConnectAppTheme {
        val chartNames = listOf("Nhiệt độ", "Độ ẩm", "Khí gas", "Điện tiêu thụ")
        val chartColors = listOf(
            Color(0xFF26A69A), // temp
            Color(0xFF5C6BC0), // humidity
            Color(0xFFE57373), // gas
            Color(0xFFFFB300)  // power
        )
        val chartFillColors = listOf(
            Color(0x5526A69A),
            Color(0x555C6BC0),
            Color(0x55E57373),
            Color(0x55FFB300)
        )

        /* Chuyển API → DataPoint cho chart */
        val chartDataList = listOf(
            hourlyValues.toChartData { it.temperature },
            hourlyValues.toChartData { it.humidity },
            hourlyValues.toChartData { it.gas },
            hourlyValues.toChartData { it.power_consumption }
        )

        val colorScheme = MaterialTheme.colorScheme

        Scaffold(
            topBar = {
                Header(navController = navController, type = "Back", title = "Space Details")
            },
            containerColor = Color.White,
            bottomBar = { MenuBottom(navController) }
        ) { scaffoldPadding ->
            LazyColumn(modifier = Modifier.padding(scaffoldPadding)) {
                item {
                    /* Weather */
                    ColoredCornerBox(cornerRadius = 40.dp) {
                        Box(Modifier.padding(16.dp)) { WeatherInfo() }
                    }
                    InvertedCornerHeader(
                        backgroundColor = colorScheme.surface,
                        overlayColor = colorScheme.primary
                    ) {}

                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        /* ----- Dropdown Group & House ----- */
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            GenericDropdown(
                                items        = groups.map { it.name },
                                selectedItem = groups.firstOrNull { it.id == selectedGroup }?.name,
                                placeHolder  = "Chọn nhóm",
                                modifier     = Modifier.weight(1f),
                                onItemSelected = { label ->
                                    val g = groups.first { it.name == label }
                                    selectedGroup = g.id
                                    selectedHouse = null
                                    activeSpaceId = null
                                    houseVm.getHouseByGroup(g.id)
                                }
                            )
                            GenericDropdown(
                                items        = houses.map { it.house_name },
                                selectedItem = houses.firstOrNull { it.house_id == selectedHouse }?.house_name,
                                placeHolder  = "Chọn nhà",
                                modifier     = Modifier.weight(1f),
                                enabled      = houses.isNotEmpty(),
                                onItemSelected = { label ->
                                    val h = houses.first { it.house_name == label }
                                    selectedHouse = h.house_id
                                    activeSpaceId = null
                                    spaceVm.getSpaces(h.house_id)
                                }
                            )
                        }

                        /* ----- Tabs Space ----- */
                        if (spaces.isNotEmpty()) {
                            val spaceTabs = spaces.map { s ->
                                SpaceTab(
                                    id = s.space_id.toString(),
                                    name = s.space_name ?: "Unnamed",
                                    iconRes = getIconResByName(s.icon_name)
                                )
                            }
                            if (activeSpaceId == null) activeSpaceId = spaceTabs.first().id

                            RoomTabRow(
                                activeRoom   = activeSpaceId ?: "",
                                onRoomChange = { id -> activeSpaceId = id },
                                rooms        = spaceTabs,
                                tabPerScreen = 4
                            )
                        }

                        TimeRangePicker()

                        /* ----- Charts ----- */
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            chartNames.forEachIndexed { i, chartLabel ->
                                CustomLineChart(
                                    dataPoints      = chartDataList[i],
                                    chartWidth      = 280.dp,
                                    chartHeight     = 140.dp,
                                    yLabelCount     = 5,
                                    lineColor       = chartColors[i],
                                    fillColor       = chartFillColors[i],
                                    backgroundColor = Color(0xFFF7F2FA),
                                    showFill        = true,
                                    cornerRadius    = 8.dp,
                                    labelTextSize   = 12f
                                )
                                ChartLegend(color = chartColors[i], label = chartLabel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDashboardScreen() = DashboardScreen(navController = rememberNavController())