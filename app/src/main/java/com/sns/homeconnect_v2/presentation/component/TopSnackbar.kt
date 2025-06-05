package com.sns.homeconnect_v2.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.platform.LocalDensity
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButton
import com.sns.homeconnect_v2.core.util.validation.SnackbarVariant

@Composable
fun TopSnackbar(
    message: String?,
    duration: Long = 5000L,
    onDismiss: () -> Unit,
    variant: SnackbarVariant = SnackbarVariant.INFO, // Thêm biến "variant"
) {
    val background = when (variant) {
        SnackbarVariant.SUCCESS -> Color(0xFF43A047)  // Màu xanh lá cho thành công
        SnackbarVariant.ERROR -> Color(0xFFEF5350)    // Màu đỏ cho lỗi
        SnackbarVariant.WARNING -> Color(0xFFFFC107)  // Màu vàng cho cảnh báo
        SnackbarVariant.INFO -> Color(0xFF2196F3)     // Màu xanh dương cho thông tin
    }

    val textColor = Color.White

    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val density = LocalDensity.current

    val yOffset = with(density) { (topInset - 12.dp).roundToPx() }

    LaunchedEffect(message) {
        delay(duration)
        onDismiss()
    }

    Popup(
        alignment = Alignment.TopCenter,
        offset = IntOffset(0, yOffset)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Tạo Card hiển thị snackbar
            Card(
                colors = CardDefaults.cardColors(containerColor = background),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp , vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Nội dung message của snackbar
                    Text(
                        text = message.orEmpty(),
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f) // Text chiếm hết chiều rộng còn lại
                    )

                    // Dấu "X" để đóng snackbar
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}





