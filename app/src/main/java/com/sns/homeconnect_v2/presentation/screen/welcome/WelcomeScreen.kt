package com.sns.homeconnect_v2.presentation.screen.welcome

import IoTHomeConnectAppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sns.homeconnect_v2.R
import com.sns.homeconnect_v2.presentation.navigation.Screens

/**
 * Màn hình Welcome hiển thị logo và thông điệp chào mừng
 * -----------------------------------------
 * Người viết: Nguyễn Thanh Sang
 * Ngày viết: 30/11/2024
 * Lần cập nhật cuối: 15/12/2024
 * -----------------------------------------
 * @param navController: Đối tượng điều khiển điều hướng
 *
 * @return Scaffold chứa logo, thông điệp chào mừng và các nút truy cập
 *
 * ---------------------------------------
 * Người cập nhật: Phạm Anh Tuấn
 * Ngày cập nhật: 15/12/2024
 *
 * Nội dung cập nhật:
 *  - Cải thiện giao diện hiển thị text và thêm nút truy cập
 *
 *  --------------------------------------
 *  Người cập nhật: Nguyễn Thanh Sang
 *  Ngày cập nhật: 31/12/2024
 *
 *  Nội dung cập nhật:
 *   - Cho phép cuộn
 */

@Composable
fun WelcomeScreen(
    navController: NavHostController
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    IoTHomeConnectAppTheme { // Gói trong AppTheme để đảm bảo áp dụng theme
        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo ứng dụng HomeConnect",
                    modifier = Modifier.size(500.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))


                Text(
                    text = "Chào mừng bạn đến với HomeConnect!",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = if (isTablet) 32.dp else 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            navController.navigate(Screens.Login.route)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .width(if (isTablet) 300.dp else 200.dp)
                            .height(if (isTablet) 56.dp else 48.dp)

                    ) {
                        Text(
                            text = "Đăng nhập",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Button(
                        onClick = { navController.navigate(Screens.Register.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .width(if (isTablet) 300.dp else 200.dp)
                            .height(if (isTablet) 56.dp else 48.dp)
                    ) {
                        Text(
                            text = "Đăng ký",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(navController = rememberNavController())
}