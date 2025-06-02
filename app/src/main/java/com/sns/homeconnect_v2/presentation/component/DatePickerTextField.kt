package com.sns.homeconnect_v2.presentation.component
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPicker by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val displayFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val currentDateMillis = remember(value) {
        try {
            displayFormat.parse(value)?.time
        } catch (_: Exception) {
            null
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        readOnly = true,
        label = { Text(label) },
        placeholder = { Text("dd/mm/yyyy") },
        leadingIcon = {
            Icon(
                Icons.Default.CalendarToday,
                contentDescription = null,
                tint = Color(0xFF2979FF) // Màu icon brand
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFD1D5DB),
            focusedBorderColor = Color(0xFF2979FF)
        ),
        interactionSource = interactionSource
    )

    // Click để mở picker
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                showPicker = true
            }
        }
    }

    if (showPicker) {
        CustomDatePickerDialog(
            show = true,
            onDismiss = { showPicker = false },
            onDateSelected = { millis ->
                val dateStr = displayFormat.format(Date(millis))
                onValueChange(dateStr)
            },
            initialDateMillis = currentDateMillis,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDatePickerTextField() {
    var date by remember { mutableStateOf("29/05/2025") }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            DatePickerTextField(
                label = "Ngày sinh",
                value = date,
                onValueChange = { date = it }
            )
        }
    }
}





