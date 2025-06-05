package com.sns.homeconnect_v2.presentation.viewmodel.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.sns.homeconnect_v2.presentation.component.TopSnackbar

@Composable
fun GlobalSnackbarHost(snackbarViewModel: SnackbarViewModel) {
    val message by snackbarViewModel.snackbarMessage.collectAsState()
    val variant by snackbarViewModel.snackbarVariant.collectAsState()

    // Nếu có message thì show TopSnackbar
    message?.let {
        TopSnackbar(
            message = it,
            variant = variant,
            onDismiss = { snackbarViewModel.hideSnackbar() }
        )
    }
}
