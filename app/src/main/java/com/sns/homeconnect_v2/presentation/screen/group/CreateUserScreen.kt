package com.sns.homeconnect_v2.presentation.screen.group

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import com.sns.homeconnect_v2.presentation.component.widget.ActionButtonWithFeedback
import com.sns.homeconnect_v2.presentation.component.widget.ColoredCornerBox
import com.sns.homeconnect_v2.presentation.component.widget.GenericDropdown
import com.sns.homeconnect_v2.presentation.component.widget.HCButtonStyle
import com.sns.homeconnect_v2.presentation.component.widget.InvertedCornerHeader
import com.sns.homeconnect_v2.presentation.component.widget.SearchBar
import com.sns.homeconnect_v2.presentation.component.widget.SimpleUserCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateUserScreen() {
    val scope = rememberCoroutineScope()
    var current by remember { mutableStateOf<String?>(null) }
    val list = listOf("Owner", "Vice", "Admin", "Member")
    val users = listOf(
        "Trần Thị B" to "https://i.pravatar.cc/150?img=8"
    )

    IoTHomeConnectAppTheme {
        Scaffold(
            containerColor = Color.White,
        ) { inner ->
            Column (
                modifier= Modifier.padding(inner)
            ) {
                ColoredCornerBox(
                    backgroundColor = Color(0xFF3A4750),
                    cornerRadius = 40.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        SearchBar(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onSearch = { query ->
                                /* TODO: điều kiện search */
                            }
                        )
                    }
                }
                InvertedCornerHeader(
                    backgroundColor = Color.White,
                    overlayColor = Color(0xFF3A4750)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    users.forEach { (name, avatar) ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFD8E4E8), // cùng màu như trước
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SimpleUserCard(
                                userName = name,
                                avatarUrl = avatar,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    GenericDropdown(
                        items = list,
                        selectedItem = current,
                        onItemSelected = { current = it }
                    )
                    Spacer(Modifier.height(8.dp))
                    ActionButtonWithFeedback(
                        label = "Thêm",
                        style = HCButtonStyle.PRIMARY,
                        onAction = { onS, _ -> scope.launch { delay(1000); onS("Done") } }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreateUserScreenPreview() {
    CreateUserScreen()
}
