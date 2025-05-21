package com.sns.homeconnect_v2.presentation.component.dialog

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WarningDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmText: String = "Xác nhận",
    dismissText: String = ""
) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        AlertDialog(
            containerColor = colorScheme.background,
            titleContentColor = colorScheme.error,
            textContentColor = colorScheme.onBackground,
            onDismissRequest = { onDismiss() },
            title = { Text(text = title) },
            text = {
                Text(text = text)
            },
            confirmButton = {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    /* ---------- nút Huỷ ---------- */
                    if (dismissText.isNotBlank()) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(25),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(dismissText, color = colorScheme.primary)
                        }
                    }

                    /* ---------- nút Xác nhận ---------- */
                    if (confirmText.isNotBlank()) {
                        Button(
                            onClick = onConfirm,
                            shape = RoundedCornerShape(25),
                            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(confirmText, color = colorScheme.onPrimary)
                        }
                    }
                }
            }

        )
    }
}

@Preview(showBackground = true, widthDp = 340, heightDp = 240)
@Composable
fun WarningDialogPreview() {
    WarningDialog(
        title    = "Xác nhận xoá",
        text  = "Bạn chắc chắn muốn xoá thiết bị này?",
        onConfirm = {},
        onDismiss = {}
    )
}