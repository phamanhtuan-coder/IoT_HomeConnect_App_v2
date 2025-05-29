package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A custom Material Design date picker dialog.
 *
 * This dialog allows the user to pick a date. It uses custom colors for a personalized look and feel.
 *
 * @param show Boolean flag to control the visibility of the dialog. If false, the dialog is not displayed.
 * @param onDismiss Callback invoked when the user dismisses the dialog (e.g., by clicking outside or pressing the back button).
 * @param onDateSelected Callback invoked when the user selects a date and confirms. The selected date is provided as a Long representing milliseconds since the Unix epoch.
 * @param initialDateMillis Optional initial date to be selected in the picker, in milliseconds since the Unix epoch. If null, the picker will default to the current date.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit,
    initialDateMillis: Long? = null,
) {
    if (!show) return

    // Custom colors - Đổi nền dialog thành trắng 100%
    val customColors = MaterialTheme.colorScheme.copy(
        primary = Color(0xFF2979FF),      // màu nút OK, ngày chọn
        onPrimary = Color.White,
        secondary = Color(0xFFF4F7FB),    // màu hover (nhạt)
        onSecondary = Color.Black,
        surface = Color.White,            // màu nền dialog
        onSurface = Color(0xFF353A40),    // màu chữ trong dialog
        background = Color.White,         // màu nền
        surfaceVariant = Color.White      // nền variant (chống nền tím nhạt!)
    )

    MaterialTheme(
        colorScheme = customColors
    ) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDateMillis
        )
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = {
                        pickerState.selectedDateMillis?.let { onDateSelected(it) }
                        onDismiss()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2979FF))
                ) {
                    Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Huỷ", color = Color(0xFF353A40)) }
            },
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                DatePicker(
                    state = pickerState,
                    showModeToggle = true,
                    title = null
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomDatePickerDialog() {
    var showDialog by remember { mutableStateOf(true) }
    // Demo: hiển thị dialog ngay khi preview
    CustomDatePickerDialog(
        show = showDialog,
        onDismiss = { showDialog = false },
        onDateSelected = { /* Xử lý chọn ngày ở đây */ },
        initialDateMillis = System.currentTimeMillis()
    )
}
