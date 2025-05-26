package com.sns.homeconnect_v2.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetWithDynamicBottomBar(
    navController: NavHostController,
    sheetContent: @Composable ColumnScope.() -> Unit,
    mainContent: @Composable () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    // ✅ CHO PEEK HEIGHT khác 0.dp để user có thể vuốt lên được
    val peekHeight = 64.dp

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = peekHeight,
        sheetContent = sheetContent
    ) {
        val isSheetVisible = scaffoldState.bottomSheetState.currentValue != SheetValue.Hidden

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                mainContent()
            }

            AnimatedVisibility(visible = !isSheetVisible) {
                MenuBottom(navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomSheetWithDynamicBottomBar() {
    val navController = rememberNavController()

    BottomSheetWithDynamicBottomBar(
        navController = navController,
        sheetContent = {
            // Phần đầu của sheet luôn hiển thị => Vuốt được
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Vuốt lên để mở Sheet", style = MaterialTheme.typography.titleMedium)
            }

            // Nội dung chi tiết hơn nếu người dùng vuốt
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Chi tiết nội dung trong Bottom Sheet")
                Spacer(Modifier.height(12.dp))
                Button(onClick = {}) {
                    Text("Xác nhận")
                }
            }
        },
        mainContent = {
            Text("Trang chính", style = MaterialTheme.typography.headlineSmall)
        }
    )
}

