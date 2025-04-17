package com.sns.homeconnect_v2.presentation.component.dialog

import IoTHomeConnectAppTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun WarningDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
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
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colorScheme.onError,
                        containerColor = colorScheme.error
                    ),
                    onClick = onConfirm
                ) {
                    Text("Xác nhận")
                }
            },

            dismissButton = {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colorScheme.onPrimary,
                        containerColor = colorScheme.primary
                    ),
                    onClick = onDismiss
                ) {
                    Text("Hủy")
                }
            }
        )
    }

}