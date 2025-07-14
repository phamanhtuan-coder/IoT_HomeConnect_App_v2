package com.sns.homeconnect_v2.presentation.component

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.ApprovePermissionViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.iot_device.PermissionUiState

@Composable
fun ApprovePermissionDialog(
    ticketId: String,
    onDismiss: () -> Unit,
    onApproved: () -> Unit,
    viewModel: ApprovePermissionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is PermissionUiState.Success) {
            onApproved()
            onDismiss()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Xác nhận chia sẻ quyền")
        },
        text = {
            when (state) {
                is PermissionUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is PermissionUiState.Error -> {
                    Text(text = "Lỗi: ${(state as PermissionUiState.Error).message}")
                }
                else -> {
                    Text("Bạn có muốn chấp nhận yêu cầu chia sẻ thiết bị không?")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.approve(ticketId) },
                enabled = state !is PermissionUiState.Loading
            ) {
                Text("Chấp nhận")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Huỷ")
            }
        }
    )
}
