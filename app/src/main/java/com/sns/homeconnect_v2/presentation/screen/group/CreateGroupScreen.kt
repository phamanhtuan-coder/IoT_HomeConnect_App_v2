package com.sns.homeconnect_v2.presentation.screen.group

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import com.sns.homeconnect_v2.presentation.component.widget.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateGroupScreen() {
    var groupName by remember { mutableStateOf("") }
    var groupDesc by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // ---------------- icon + color state ----------------
    val iconOptions = listOf(
        Icons.Default.Home      to "Nhà",
        Icons.Default.Work      to "Cơ quan",
        Icons.Default.School    to "Trường",
        Icons.Default.AccountBalance to "Ngân hàng",
        Icons.Default.Apartment     to "Căn hộ",
        Icons.Default.Hotel         to "Khách sạn",
        Icons.Default.Villa         to "Biệt thự",
        Icons.Default.Cottage       to "Nhà gỗ",
        Icons.Default.Castle        to "Lâu đài",
        Icons.Default.LocalLibrary  to "Thư viện"
    )
    var selectedIcon by remember { mutableStateOf(iconOptions.first().first) }

    val colorOptions = listOf(
        Color.Red,
        Color(0xFF00FF00),
        Color.Blue,
        Color.Yellow,
        Color.Cyan
    )
    var selectedColor by remember { mutableStateOf(colorOptions.first()) }

    IoTHomeConnectAppTheme {
        Scaffold(
            containerColor = Color.White,
        ) { inner ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                item {
                    ColoredCornerBox(
                        backgroundColor = Color(0xFF3A4750),
                        cornerRadius = 40.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .height(IntrinsicSize.Min)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                StyledTextField(
                                    value = groupName,
                                    onValueChange = { groupName = it },
                                    placeholderText = "Nhập tên nhóm",
                                    leadingIcon = Icons.Default.People,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                StyledTextField(
                                    value = groupDesc,
                                    onValueChange = { groupDesc = it },
                                    placeholderText = "Mô tả của group",
                                    leadingIcon = Icons.Default.NoteAlt,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                    InvertedCornerHeader(
                        backgroundColor = Color.White,
                        overlayColor = Color(0xFF3A4750)
                    ) {}
                }

                // ---------- icon picker ----------
                item {
                    IconPicker(
                        iconOptions = iconOptions,
                        selectedIcon = selectedIcon,
                        onIconSelected = { selectedIcon = it }
                    )
                }

                // ---------- color picker ----------
                item {
                    Spacer(Modifier.height(8.dp))
                    ColorPicker(
                        colorOptions = colorOptions,
                        selectedColor = selectedColor,
                        onColorSelected = { selectedColor = it }
                    )
                    Spacer(Modifier.height(32.dp))
                }

                // ---------- button ----------
                item {
                    ActionButtonWithFeedback(
                        label = "Hoàn tất",
                        style = HCButtonStyle.PRIMARY,
                        onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    ActionButtonWithFeedback(
                        label = "Huỷ bỏ",
                        style = HCButtonStyle.SECONDARY,
                        onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateGroupScreenPreview() {
    CreateGroupScreen()
}
