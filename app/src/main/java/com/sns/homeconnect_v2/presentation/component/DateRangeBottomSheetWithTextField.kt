package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeBottomSheetWithTextField(
    startDate: String,
    endDate: String,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onDismiss: () -> Unit,
//    onConfirm: (String, String) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Chọn khoảng ngày", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            DatePickerTextField(
                label = "Ngày bắt đầu",
                value = startDate,
                onValueChange = onStartDateChange,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth()
            )
            DatePickerTextField(
                label = "Ngày kết thúc",
                value = endDate,
                onValueChange = onEndDateChange,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    onDismiss()
                },
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2979FF),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Text(
                    "Áp dụng",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.1.sp,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDateRangeSheetDemo() {
    var showSheet by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showSheet = true }) {
            Text("Chọn khoảng ngày")
        }
        Spacer(Modifier.height(12.dp))
        if (result.isNotBlank()) {
            Text("Kết quả: $result", color = Color(0xFF2979FF))
        }
    }

    if (showSheet) {
        DateRangeBottomSheetWithTextField(
            startDate = startDate,
            endDate = endDate,
            onStartDateChange = { startDate = it },
            onEndDateChange = { endDate = it },
            onDismiss = { showSheet = false },
//            onConfirm = { start, end ->
//                result = "$start đến $end"
//                showSheet = false
//            }
        )
    }
}
