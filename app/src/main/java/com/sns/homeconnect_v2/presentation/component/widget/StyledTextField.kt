package com.sns.homeconnect_v2.presentation.component.widget

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.sns.homeconnect_v2.presentation.component.dialog.ConfirmationDialog

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
        val colorScheme = MaterialTheme.colorScheme
        var passwordVisible by remember { mutableStateOf(false) }

        Column(modifier = modifier) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholderText) },
                shape = RoundedCornerShape(25),
                singleLine = true,
                isError = errorText != null,
                leadingIcon = { Icon(leadingIcon, contentDescription = null) },
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
                    .width(if (isTablet) 500.dp else 400.dp)
                    .height(if (isTablet) 80.dp else 70.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorScheme.onBackground,
                    unfocusedTextColor = colorScheme.onBackground.copy(alpha = 0.7f),
                    focusedContainerColor = colorScheme.onPrimary,
                    unfocusedContainerColor = colorScheme.onPrimary,
                    focusedIndicatorColor = colorScheme.primary,
                    unfocusedIndicatorColor = colorScheme.onBackground.copy(alpha = 0.5f)
                )
            )

            if (!errorText.isNullOrEmpty()) {
                Text(
                    text = errorText,
                    color = colorScheme.error,
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
            placeholderText = "Nhập tên",
            leadingIcon = Icons.Default.Person
        )
    }
    if (showDialog) {
        ConfirmationDialog(
            title = "Xác nhận xoá",
            message = "Bạn có chắc muốn xoá thiết bị này?",
            onConfirm = { showDialog = false },
            onDismiss = { showDialog = false }
        )
    }
}