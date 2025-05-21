package com.sns.homeconnect_v2.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.common.util.DeviceProperties.isTablet

const val PADDING_OFFSET = 20f

data class LayoutConfig(
    val outerPadding: Dp,  // Padding bên ngoài, thường được tính theo tỉ lệ chiều rộng màn hình
    val textFieldSpacing: Dp, // Khoảng cách giữa các thành phần, dựa trên chiều cao màn hình
    val headingFontSize: TextUnit, // Kích thước chữ tiêu đề, tỉ lệ theo chiều rộng màn hình
    val textFontSize: TextUnit, // Kích thước chữ mô tả hoặc nội dung
    val contentWidth: Dp, // Chiều rộng của nội dung chính (ví dụ: TextField)
    val iconSize: Dp, // Kích thước của icon trong giao diện
    val boxHeight: Dp, // Chiều cao của các Box (ví dụ: hộp chứa góc lõm)
    val cornerBoxSize: Dp,          // Kích thước cho Box góc lõm
    val cornerBoxRadius: Int,       // Bo tròn góc cho Box góc lõm
    val dialogPadding: Dp           // Padding cho AlertDialog
)

// Tính toán LayoutConfig dựa trên kích thước màn hình
@Composable
fun rememberResponsiveLayoutConfig(): LayoutConfig {
    var isTablet = isTablet(LocalContext.current) // Kiểm tra xem thiết bị có phải là tablet hay không
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // Lấy chiều rộng màn hình
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // Lấy chiều cao màn hình

    return LayoutConfig(
        outerPadding = screenWidth * 0.05f,                   // Padding bên ngoài bằng 5% chiều rộng màn hình
        textFieldSpacing = 8.dp + screenHeight * 0.01f,       // Khoảng cách giữa các thành phần = 8dp + 1% chiều cao màn hình
        headingFontSize = (12 + screenWidth.value * 0.04f).sp,// Font size tiêu đề dựa trên chiều rộng màn hình
        textFontSize = (10 + screenWidth.value * 0.03f).sp,   // Font size nội dung dựa trên chiều rộng màn hình
        contentWidth = if(isTablet) 400.dp + screenWidth * 0.1f else 300.dp,                    // Chiều rộng của nội dung chính bằng 80% chiều rộng màn hình
        iconSize = screenWidth * 0.04f,                       // Kích thước icon bằng 8% chiều rộng màn hình
        boxHeight = screenHeight * 0.1f,                      // Chiều cao Box là 10% chiều cao màn hình
        cornerBoxSize = screenWidth * 0.1f,                   // Kích thước cho Box góc lõm bằng 10% chiều rộng màn hình
        cornerBoxRadius = 50,                                 // Độ bo góc cho Box góc lõm (theo phần trăm)
        dialogPadding = screenWidth * 0.04f                   // Padding cho AlertDialog bằng 4% chiều rộng màn hình
    )
}