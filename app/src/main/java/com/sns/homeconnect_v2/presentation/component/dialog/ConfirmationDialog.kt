package com.sns.homeconnect_v2.presentation.component.dialog

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmText: String = "Xác nhận",
    dismissText: String = ""
) {
    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = colorScheme.onBackground.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    /* --------- nút Huỷ (Outlined) --------- */
                    if (dismissText.isNotBlank()) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(25),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(dismissText, color = colorScheme.primary)
                        }
                    }

                    /* --------- nút Xác nhận (Filled) --------- */
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
            },
            containerColor = colorScheme.background
        )
    }
}

@Preview(showBackground = true, widthDp = 340, heightDp = 240)
@Composable
fun ConfirmationDialogPreview() {
    ConfirmationDialog(
        title    = "Xác nhận xoá",
        message  = "Bạn chắc chắn muốn xoá thiết bị này?",
        onConfirm = {},
        onDismiss = {}
    )
}
