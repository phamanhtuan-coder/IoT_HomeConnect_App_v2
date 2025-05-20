package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun DeviceDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String = "Smart Lamp",
    model: String = "Model 123",
    location: String = "Home",
    group: String = "Living Room",
    wifiStatus: String = "Connected",
    firmwareVersion: String = "1.0.4",
    lockStatus: String = "Unlock"
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("Close")
                }
            },
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    InfoRow(label = "Model", value = model)
                    InfoRow(label = "Location", value = location)
                    InfoRow(label = "Group", value = group)
                    InfoRow(label = "Wi-Fi", value = wifiStatus)
                    InfoRow(label = "Firmware", value = firmwareVersion)
                    InfoRow(label = "Lock", value = lockStatus)
                }
            },
            containerColor = Color(0xFFD8E4E8)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceDialogPreview() {
    DeviceDialog(
        showDialog = true,
        onDismiss = { /* close logic */ },
        title = "Smart Lamp",
        model = "ABC123",
        location = "Bedroom",
        group = "Ceiling",
        wifiStatus = "Disconnected",
        firmwareVersion = "2.0.1",
        lockStatus = "Locked"
    )

}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}
