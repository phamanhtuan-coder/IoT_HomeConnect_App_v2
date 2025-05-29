package com.sns.homeconnect_v2.presentation.component.widget

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Text
import com.sns.homeconnect_v2.presentation.component.dialog.ConfirmationDialog

/**
 * Một hàm composable hiển thị một trường văn bản được tạo kiểu.
 *
 * @param value Giá trị hiện tại của trường văn bản.
 * @param onValueChange Một hàm callback được gọi khi giá trị của trường văn bản thay đổi.
 * @param placeholderText Văn bản hiển thị làm trình giữ chỗ khi trường văn bản trống.
 * @param leadingIcon Biểu tượng hiển thị ở đầu trường văn bản.
 * @param modifier Một [Modifier] để áp dụng cho trường văn bản.
 * @param isPassword Trường văn bản có phải là mật khẩu không. Nếu đúng, một biểu tượng chuyển đổi sẽ được hiển thị để hiển thị/ẩn mật khẩu.
 * @param errorText Một thông báo lỗi tùy chọn để hiển thị bên dưới trường văn bản.
 * @param isTablet Thiết bị có phải là máy tính bảng không. Điều này ảnh hưởng đến chiều rộng của trường văn bản.
 */
@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    errorText: String? = null,
    isTablet: Boolean = false
) {
    IoTHomeConnectAppTheme {
        var passwordVisible by remember { mutableStateOf(false) }

        Column(modifier = modifier) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        placeholderText,
                        fontSize = 16.sp,
                        color = if (errorText != null) Color(0xFFD32F2F) else Color(0xFF212121)
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    color = if (errorText != null) Color(0xFFD32F2F) else Color(0xFF212121)
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                isError = errorText != null,
                leadingIcon = {
                    Row(modifier = Modifier.padding(start = 12.dp, end = 12.dp)) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = Color(0xFF212121)
                        )
                    }
                },
                trailingIcon = if (isPassword) {
                    {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                } else null,
                visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .height(56.dp)
                    .width(if (isTablet) 500.dp else 400.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF212121),
                    unfocusedTextColor = Color(0xFF212121),
                    disabledTextColor = Color(0xFF9E9E9E),
                    errorTextColor = Color(0xFFD32F2F),

                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color(0xFFF5F5F5),
                    errorContainerColor = Color.White,

                    focusedIndicatorColor = if (value.isEmpty()) Color(0xFFB71C1C) else Color(0xFFD32F2F),
                    unfocusedIndicatorColor = Color(0xFF9E9E9E),
                    disabledIndicatorColor = Color(0xFFBDBDBD),
                    errorIndicatorColor = if (value.isEmpty()) Color(0xFFB71C1C) else Color(0xFFD32F2F)
                )
            )

            if (!errorText.isNullOrEmpty()) {
                Text(
                    text = errorText,
                    color = Color(0xFFD32F2F),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTextFieldAndDialog() {
    var text by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(16.dp)) {
        StyledTextField(
            value = text,
            onValueChange = { text = it },
            placeholderText = "Group name",
            leadingIcon = Icons.Default.Person
        )
    }
//    if (showDialog) {
//        ConfirmationDialog(
//            title = "Xác nhận xoá",
//            message = "Bạn có chắc muốn xoá thiết bị này?",
//            onConfirm = { showDialog = false },
//            onDismiss = { showDialog = false }
//        )
//    }
}