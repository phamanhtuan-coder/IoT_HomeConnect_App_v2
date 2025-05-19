package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.StyledTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Composable function hiển thị màn hình thông tin chi tiết cho một thiết bị IoT.
 * Màn hình này bao gồm một header, một khu vực nội dung với một trường văn bản được tạo kiểu và một nút hành động,
 * và một menu điều hướng ở dưới cùng.
 *
 * Bố cục màn hình bao gồm:
 * - Một thanh trên cùng với nút quay lại và tiêu đề "Space Details".
 * - Một khu vực nội dung chính bao gồm:
 *     - Một `ColoredCornerBox` (hiện tại trống).
 *     - Một `InvertedCornerHeader`.
 *     - Một `Column` chứa:
 *         - Một `StyledTextField` để nhập email.
 *         - Một `ActionButtonWithFeedback` có nhãn "Chuyển quyền".
 * - Một thanh dưới cùng với điều hướng `MenuBottom`.
 *
 * `StyledTextField` cho phép người dùng nhập văn bản (có lẽ là địa chỉ email).
 * `ActionButtonWithFeedback` mô phỏng một hành động mất 1 giây để hoàn thành
 * và sau đó hiển thị "Done".
 *
 * @param navController NavHostController được sử dụng cho các hành động điều hướng, chẳng hạn như quay lại.
 * @author Nguyễn Thanh Sang
 * @since 19-05-24
 */

@Composable
fun DetailInfoDeviceScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()

    IoTHomeConnectAppTheme {
        val colorScheme = MaterialTheme.colorScheme
        var text by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type          = "Back",
                    title         = "Space Details"
                )
            },
            containerColor = Color.White,
            bottomBar = {
                MenuBottom(navController)
            }
        ) { scaffoldPadding ->
            Column (
                modifier= Modifier.padding(scaffoldPadding)
            ) {
                ColoredCornerBox(
                    cornerRadius = 40.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {}
                }

                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {}

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StyledTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholderText = "Nhập email",
                        leadingIcon = Icons.Default.Email
                    )

                    ActionButtonWithFeedback(
                        label = "Chuyển quyền",
                        style = HCButtonStyle.PRIMARY,
                        onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "GroupScreen - Phone")
@Composable
fun DetailInfoDeviceScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        DetailInfoDeviceScreen(navController = rememberNavController())
    }
}