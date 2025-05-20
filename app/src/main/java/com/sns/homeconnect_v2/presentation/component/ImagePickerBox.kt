package com.sns.homeconnect_v2.presentation.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale

/**
 * Một hàm có thể kết hợp hiển thị một hộp để chọn ảnh từ thư viện và hiển thị nó.
 *
 * @param width Chiều rộng của hộp chọn ảnh.
 * @param height Chiều cao của hộp chọn ảnh.
 * @param modifier Bộ điều chỉnh sẽ được áp dụng cho hộp chọn ảnh.
 * @author Nguyễn Thanh Sang
 * @since 20-05-2025
 */

@Composable
fun ImagePickerBox(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    /* launcher mở thư viện */
    val pickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    Box(
        modifier = modifier
            .size(width, height)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
            .clickable { pickerLauncher.launch("image/*") },      // click để chọn ảnh
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUri)
                    .crossfade(true)
                    .scale(Scale.FILL)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Chọn ảnh", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                if (imageUri == null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        BrowseButton { pickerLauncher.launch("image/*") }
                    }
                }
            }
        }
    }
}

/* Nút được tách riêng để tái sử dụng */
@Composable
private fun BrowseButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),                  // pill
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,             // nền theo theme
            contentColor   = Color.White                // chữ trắng
        ),
        contentPadding = PaddingValues(
            horizontal = 16.dp, vertical = 8.dp         // padding rộng
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp                      // bóng nhẹ
        )
    ) {
        Text(
            "Browse",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
        )
    }
}


/* ---------------- Preview ---------------- */
@Preview(showBackground = true, widthDp = 220, heightDp = 240)
@Composable
fun ImagePickerBoxPreview() {
    ImagePickerBox(width = 200.dp, height = 200.dp, modifier = Modifier.padding(12.dp))
}
