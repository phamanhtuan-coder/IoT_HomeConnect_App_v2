package com.sns.homeconnect_v2.presentation.component

import IoTHomeConnectAppTheme
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Một hàm Composable hiển thị một bộ chọn thời gian kiểu khóa số cuộn vô hạn.
 *
 * Bộ chọn thời gian này cho phép người dùng chọn giờ, phút và AM/PM bằng cách cuộn
 * qua các danh sách vô hạn. Nó có phong cách trực quan gợi nhớ đến sự kết hợp khóa số.
 *
 * Thời gian ban đầu được hiển thị là 9:00 SA.
 *
 * Khi người dùng ngừng cuộn trên bất kỳ cột nào (giờ, phút hoặc AM/PM),
 * callback `onTimeSelected` sẽ được gọi với thời gian mới được chọn.
 *
 * @param modifier [Modifier] tùy chọn để áp dụng cho bộ chọn thời gian.
 * @param onTimeSelected Một hàm callback được gọi khi người dùng hoàn tất
 *                       việc cuộn và một thời gian mới được chọn. Nó cung cấp giờ
 *                       đã chọn (1-12), phút (0-59) và chuỗi AM/PM ("AM" hoặc "PM").
 * @throws IllegalArgumentException nếu `initialAmPm` không phải là "AM" hoặc "PM".
 *
 * @author Nguyễn Thanh Sang
 * @since 19-05-2025
 */

@SuppressLint("FrequentlyChangedStateReadInComposition")
@Composable
fun EndlessRollingPadlockTimePicker(
    modifier: Modifier = Modifier,
    onTimeSelected: (hour: Int, minute: Int, amPm: String) -> Unit
) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        val hoursList = (1..12).toList()
        val minutesList = (0..59).toList()
        val amPmList = listOf("AM", "PM")

        //Giá trị muốn hiên khi mở đầu
        val initialHour = 9
        val initialMinute = 0
        val initialAmPm = "AM" // Hoặc giá trị mong muốn

        // Kiểm tra giá trị AM/PM
        val amPmIndex = amPmList.indexOf(initialAmPm)
        if (amPmIndex == -1) {
            throw IllegalArgumentException("Invalid value for AM/PM: $initialAmPm. Allowed values: ${amPmList.joinToString()}")
        }

        // Tính toán chỉ số khởi tạo
        val hourState = rememberLazyListState(
            initialFirstVisibleItemIndex = (100 * hoursList.size / 2) + hoursList.indexOf(
                initialHour
            ) - 1
        )
        val minuteState = rememberLazyListState(
            initialFirstVisibleItemIndex = (100 * minutesList.size / 2) + minutesList.indexOf(
                initialMinute
            ) - 1
        )
        val amPmState = rememberLazyListState(
            initialFirstVisibleItemIndex = (100 * amPmList.size / 2) + amPmIndex - 1
        )

        Box(
            modifier = modifier
                .wrapContentHeight()
                .background(colorScheme.background, shape = RoundedCornerShape(16.dp))
                .border(2.dp, Color.Gray, RoundedCornerShape(16.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Row {
                // Cột giờ
                RollingColumn(
                    state = hourState,
                    items = hoursList,
                    modifier = Modifier.width(70.dp),
                    label = { hour -> hour.toString() }
                )

                // Dấu :
                LazyColumn(
                    modifier = modifier
                        .height(140.dp) // Chiều cao đồng bộ
                        .background(colorScheme.background.copy(alpha = 0.8f)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .wrapContentWidth()
                                .offset(y = 5.dp)
                                .height(50.dp), // Chiều cao cố định
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ":",
                                fontSize = 36.sp,
                                color = colorScheme.onBackground,
                                modifier = Modifier
                                    .wrapContentSize()// Chiều rộng cố định
                                    .padding(horizontal = 4.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Cột phút
                RollingColumn(
                    state = minuteState,
                    items = minutesList,
                    modifier = Modifier.width(70.dp),
                    label = { minute -> minute.toString().padStart(2, '0') }
                )

                // Cột AM/PM
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(70.dp)
                        .height(140.dp) // Đồng bộ chiều cao với các cột khác
                ) {
                    RollingColumn(
                        state = amPmState,
                        items = amPmList,
                        modifier = Modifier.align(Alignment.Center),
                        label = { amPm -> amPm },
                        fontSize = 36.sp // Đảm bảo AM/PM nổi bật
                    )
                }
            }
        }

        // Snap Effect
        LaunchedEffect(
            hourState.isScrollInProgress,
            minuteState.isScrollInProgress,
            amPmState.isScrollInProgress
        ) {
            if (!hourState.isScrollInProgress && !minuteState.isScrollInProgress && !amPmState.isScrollInProgress) {
                val selectedHour = hoursList[(hourState.firstVisibleItemIndex + 1) % hoursList.size]
                val selectedMinute =
                    minutesList[(minuteState.firstVisibleItemIndex + 1) % minutesList.size]
                val selectedAmPm = amPmList[(amPmState.firstVisibleItemIndex + 1) % amPmList.size]

                // Trả về kết quả khi cuộn xong
                onTimeSelected(selectedHour, selectedMinute, selectedAmPm)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EndlessRollingPadlockTimePickerPreview() {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        var selectedTimeBegin by remember { mutableStateOf("12:00 AM") }
        Box(
            modifier = Modifier
                .background(
                    colorScheme.background,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
                .wrapContentSize()
        ) {
            EndlessRollingPadlockTimePicker { hour, minute, amPm ->
                selectedTimeBegin =
                    "$hour:${minute.toString().padStart(2, '0')} $amPm"
            }
        }
    }
}