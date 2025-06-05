package com.sns.homeconnect_v2.presentation.screen.iot_device

import IoTHomeConnectAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.presentation.component.ChangeLogCard
import com.sns.homeconnect_v2.presentation.component.VersionInfo
import com.sns.homeconnect_v2.presentation.component.VersionTable
import com.sns.homeconnect_v2.presentation.component.dialog.WarningDialog
import com.sns.homeconnect_v2.presentation.component.navigation.Header
import com.sns.homeconnect_v2.presentation.component.navigation.MenuBottom
import com.sns.homeconnect_v2.presentation.component.widget.*
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Hàm Composable hiển thị màn hình phiên bản phần mềm.
 * Màn hình này hiển thị phiên bản phần mềm hiện tại, tóm tắt các thay đổi,
 * tùy chọn cập nhật phiên bản và danh sách các phiên bản trước đó.
 *
 * @param navController [NavHostController] được sử dụng để điều hướng.
 * @author Nguyễn Thanh Sang
 * @since 20-05-2025
 */

@Composable
fun SoftwareVersionScreen(
    navController: NavHostController,
    snackbarViewModel : SnackbarViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    var pendingOnSuccess by remember { mutableStateOf<((String) -> Unit)?>(null) }
    var pendingOnError   by remember { mutableStateOf<((String) -> Unit)?>(null) }
    var showConfirm      by remember { mutableStateOf(false) }
    var isLoading        by remember { mutableStateOf(false) }

    IoTHomeConnectAppTheme {
        val versionText  = "V0.015"
        val summaryText  = "Tóm tắt nội dung thay đổi"
        val sample = listOf(
            VersionInfo("V0.020", "01/01/2026"),
            VersionInfo("V0.019", "01/12/2025"),
            VersionInfo("V0.018", "01/10/2025"),
            VersionInfo("V0.017", "01/08/2025"),
            VersionInfo("V0.016", "01/06/2025"),
            VersionInfo("V0.015", "01/01/2025"),
            VersionInfo("V0.014", "01/01/2024"),
            VersionInfo("V0.013", "01/01/2017"),
            VersionInfo("V0.012", "01/01/2015"),
            VersionInfo("V0.011", "01/01/2011"),
            VersionInfo("V0.010", "01/01/2009"),
            VersionInfo("V0.009", "01/01/2007"),
            VersionInfo("V0.008", "01/01/2005"),
            VersionInfo("V0.007", "01/01/2003"),
            VersionInfo("V0.006", "01/01/2001"),
            VersionInfo("V0.005", "01/01/1999"),
            VersionInfo("V0.004", "01/01/1997"),
            VersionInfo("V0.003", "01/01/1995"),
            VersionInfo("V0.002", "01/01/1993"),
            VersionInfo("V0.001", "01/01/1990")
        )

        val colorScheme = MaterialTheme.colorScheme

        Scaffold(
            topBar = {
                Header(
                    navController = navController,
                    type          = "Back",
                    title         = "Settings"
                )
            },
            containerColor = Color.White,
            bottomBar = {
                MenuBottom(navController = navController)
            }
        ) { inner ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ColoredCornerBox(
                    cornerRadius = 40.dp
                ) {
                    Column (
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text        = "Bản cập mới",
                            color       = Color.White,
                            fontSize    = 20.sp,
                            fontWeight  = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                        )

                        ChangeLogCard(
                            version = versionText,
                            summary = summaryText
                        )
                        // Nút chính
                        ActionButtonWithFeedback(
                            label  = "Cập nhật phiên bản",
                            style  = HCButtonStyle.PRIMARY,
                            isLoadingFromParent = isLoading,
                            onAction = { onS, onE ->
                                /* ⇢ chỉ lưu callback, CHƯA gọi API */
                                pendingOnSuccess = onS
                                pendingOnError   = onE
                                showConfirm      = true
                            },
                            snackbarViewModel = snackbarViewModel
                        )

                        // Dialog xác nhận
                        if (showConfirm) {
                            WarningDialog(
                                title       = "Xác nhận cập nhật",
                                text        = "Bạn có chắc muốn cập nhật phiên bản không?",
                                confirmText = "Cập nhật",
                                dismissText = "Huỷ",
                                onConfirm = {
                                    showConfirm = false
                                    isLoading   = true                // spinner ngay lập tức

                                    scope.launch {
                                        delay(1000)                   // giả lập API
                                        val ok = true                 // kết quả thật
                                        if (ok) pendingOnSuccess?.invoke("Đã cập nhật thành công!")
                                        else     pendingOnError?.invoke("Cập nhật thất bại!")
                                        isLoading = false             // tắt spinner
                                        pendingOnSuccess = null
                                        pendingOnError   = null
                                    }
                                },
                                onDismiss = {
                                    showConfirm      = false
                                    pendingOnSuccess = null
                                    pendingOnError   = null
                                }
                            )
                        }
                    }
                }
                InvertedCornerHeader(
                    backgroundColor = colorScheme.surface,
                    overlayColor = colorScheme.primary
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()          // phủ ngang
                            .height(40.dp)           // cao cố định
                            .padding(start = 16.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text          = "Bản cập mới",
                            color         = Color.Black,
                            fontSize      = 20.sp,
                            fontWeight    = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                VersionTable(versions = sample, modifier = Modifier.padding(horizontal = 16.dp), maxHeight= 380.dp)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "CreateGroupScreen Preview - Phone")
@Composable
fun SoftwareVersionScreenPhonePreview() {
    IoTHomeConnectAppTheme {
        SoftwareVersionScreen(navController = rememberNavController())
    }
}