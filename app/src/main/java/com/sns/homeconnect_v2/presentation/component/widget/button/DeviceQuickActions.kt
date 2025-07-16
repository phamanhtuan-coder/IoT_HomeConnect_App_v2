package com.sns.homeconnect_v2.presentation.component.widget.button

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sns.homeconnect_v2.presentation.navigation.Screens
import com.sns.homeconnect_v2.presentation.screen.iot_device.DeviceAction
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel

/**
 * Nhóm các nút thao tác nhanh cho thiết bị (Chia sẻ, Gỡ kết nối, Khoá, Reset …).
 *
 * @param serialNumber  Serial của thiết bị – dùng cho màn hình chia sẻ
 * @param navController NavController điều hướng
 * @param snackbarVM    Để hiển thị snackbar khi thao tác cục bộ (không qua Confirm dialog)
 * @param onRequestConfirm Hàm callback gọi lên màn hình cha mỗi khi cần confirm.
 *        Truyền vào (action, title, message) để cha hiện `WarningDialog`.
 */
@Composable
fun DeviceQuickActions(
    serialNumber: String,
    navController: NavHostController,
    snackbarVM: SnackbarViewModel,
    onRequestConfirm: (DeviceAction, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* ───────── HÀNG 1 ───────── */
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DeviceActionButton(
                icon = Icons.Default.Share,
                label = "Chia sẻ",
                backgroundColor = Color(0xFF03A9F4),
                modifier = Modifier.weight(1f)
            ) {
                navController.navigate(
                    Screens.ShareDeviceBySerial.createRoute(serialNumber)
                )
            }

            DeviceActionButton(
                icon = Icons.Default.LinkOff,
                label = "Gỡ kết nối",
                backgroundColor = Color(0xFFF44336),
                modifier = Modifier.weight(1f)
            ) {
                onRequestConfirm(
                    DeviceAction.UNLINK,
                    "Gỡ kết nối",
                    "Bạn có chắc chắn muốn gỡ thiết bị này không?"
                )
            }
        }

        Spacer(Modifier.height(12.dp))

//        /* ───────── HÀNG 2 ───────── */
//        Row(
//            Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            DeviceActionButton(
//                icon = Icons.Default.Lock,
//                label = "Khoá",
//                backgroundColor = Color(0xFF4CAF50),
//                modifier = Modifier.weight(1f)
//            ) {
//                onRequestConfirm(
//                    DeviceAction.LOCK,
//                    "Khóa thiết bị",
//                    "Bạn có chắc muốn khoá thiết bị này?"
//                )
//            }
//
//            DeviceActionButton(
//                icon = Icons.Default.Restore,
//                label = "Reset",
//                backgroundColor = Color(0xFFFF9800),
//                modifier = Modifier.weight(1f)
//            ) {
//                onRequestConfirm(
//                    DeviceAction.RESET,
//                    "Reset thiết bị",
//                    "Bạn muốn reset thiết bị này?"
//                )
//            }
//        }
//
//        Spacer(Modifier.height(12.dp))
//
//        /* ───────── HÀNG 3 ───────── */
//        Row(
//            Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            DeviceActionButton(
//                icon = Icons.Default.CompareArrows,
//                label = "Chuyển quyền",
//                backgroundColor = Color(0xFF9C27B0),
//                modifier = Modifier.weight(1f)
//            ) {
//                onRequestConfirm(
//                    DeviceAction.TRANSFER,
//                    "Chuyển quyền sở hữu",
//                    "Bạn có chắc muốn chuyển quyền sở hữu thiết bị?"
//                )
//            }
//
//            /* ───────── NÚT BÁO MẤT ───────── */
//            DeviceActionButton(
//                icon = Icons.Default.Report,
//                label = "Báo mất thiết bị",
//                backgroundColor = Color(0xFFB71C1C),
//                modifier = Modifier.weight(1f)
//            ) {
//                onRequestConfirm(
//                    DeviceAction.REPORT_LOST,
//                    "Báo mất thiết bị",
//                    "Bạn có chắc chắn muốn báo mất thiết bị?"
//                )
//            }
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        DeviceActionButton(
//            icon = Icons.Default.Info,
//            label = "Phiên bản",
//            backgroundColor = Color(0xFF607D8B),
//            modifier = Modifier.fillMaxWidth(0.8f)
//        ) {
//            onRequestConfirm(
//                DeviceAction.VERSION,
//                "Xem phiên bản",
//                "Bạn muốn xem phiên bản firmware hiện tại?"
//            )
//        }
    }
}
