package com.example.myapplication

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IoTHomeConnectAppTheme {
        Button(
            onClick = { onClick() },
            enabled = !isLoading,
            modifier = modifier.padding(8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp)
                )
                Text("Đang xử lý...")
            } else {
                Text(text)
            }
        }
    }
}

@Preview(showBackground = true, name = "LoadingButton - Normal")
@Composable
fun LoadingButtonPreview_Normal() {
    MaterialTheme {
        LoadingButton(
            text = "Xác nhận",
            isLoading = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "LoadingButton - Loading")
@Composable
fun LoadingButtonPreview_Loading() {
    MaterialTheme {
        LoadingButton(
            text = "Xác nhận",
            isLoading = true,
            onClick = {}
        )
    }
}