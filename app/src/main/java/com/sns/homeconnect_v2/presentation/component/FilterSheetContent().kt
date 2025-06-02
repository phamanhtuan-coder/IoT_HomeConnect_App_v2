package com.sns.homeconnect_v2.presentation.component

import IoTHomeConnectAppTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FilterSheetContent(
    modifier: Modifier = Modifier,
    titleContent: @Composable () -> Unit = {
        Text(
            "Bộ lọc",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
    },
    dateContent: @Composable () -> Unit,
    statusContent: @Composable () -> Unit,
    actionContent: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Title
        titleContent()
        Spacer(modifier = Modifier.height(24.dp))

        // Ngày gửi
        dateContent()
        Spacer(modifier = Modifier.height(28.dp))

        // Trạng thái xử lý
        statusContent()
        Spacer(modifier = Modifier.height(38.dp))

        // Nút thao tác
        actionContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 360, heightDp = 700)
@Composable
fun PreviewCustomFilterSheetContent() {
    var selectedDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    val options = listOf("Tất cả", "Đã xử lý", "Chưa xử lý")
    var selectedStatus by remember { mutableStateOf(options[0]) }

    IoTHomeConnectAppTheme {
        Surface {
            FilterSheetContent(
                titleContent = {
                    // Tuỳ chỉnh tiêu đề (thêm icon hoặc đổi style nếu muốn)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "Lọc danh sách báo lỗi",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                dateContent = {
                    Column {
                        Text(
                            "Ngày gửi",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = selectedDate,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Chọn ngày"
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showDatePicker = true },
                            placeholder = { Text("Chọn ngày", style = MaterialTheme.typography.bodyLarge) },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                statusContent = {
                    Column {
                        Text(
                            "Trạng thái xử lý",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        options.forEach { option ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedStatus = option }
                                    .padding(vertical = 6.dp)
                            ) {
                                RadioButton(
                                    selected = selectedStatus == option,
                                    onClick = { selectedStatus = option }
                                )
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                },
                actionContent = {
                    ActionButtonWithFeedback(
                        label = "Yes",
                        style = HCButtonStyle.PRIMARY,
                        onAction = { _, _ -> }
                    )
                }
            )
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let {
                                selectedDate = dateFormat.format(Date(it))
                            }
                            showDatePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Huỷ") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}

