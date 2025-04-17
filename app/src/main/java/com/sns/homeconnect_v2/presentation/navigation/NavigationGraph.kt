package com.sns.homeconnect_v2.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sns.homeconnect_v2.presentation.screen.auth.LoginScreen
import com.sns.homeconnect_v2.presentation.screen.auth.RegisterScreen
import com.sns.homeconnect_v2.presentation.screen.otp.OtpScreen
import com.sns.homeconnect_v2.presentation.screen.welcome.WelcomeScreen
import com.sns.homeconnect_v2.presentation.screen.auth.RecoverPasswordScreen

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.Welcome.route) {
        composable(Screens.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(Screens.Login.route) {
            LoginScreen(navController)
        }
        composable(Screens.RecoverPassword.route) {
            RecoverPasswordScreen(navController)
        }
        composable(Screens.Register.route) {
            RegisterScreen(navController)
        }
        composable(
            route = Screens.OTP.route,
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "reset_password"
            val email = backStackEntry.arguments?.getString("email") ?: ""
            OtpScreen(
                email = email,
                title = when (type) {
                    "reset_password" -> "Nhập mã OTP"
                    "email_verification" -> "Xác thực Email"
                    "transaction" -> "Xác thực giao dịch"
                    else -> "Nhập mã OTP"
                },
                description = when (type) {
                    "reset_password" -> "Vui lòng nhập mã OTP vừa được gửi tới Email"
                    "email_verification" -> "Nhập mã xác thực được gửi tới email của bạn"
                    "transaction" -> "Nhập mã OTP để xác nhận giao dịch"
                    else -> "Vui lòng nhập mã OTP vừa được gửi tới Email"
                },
                onVerificationSuccess = {
                    when (type) {
                        "reset_password" -> navController.navigate("${Screens.NewPassword.route}?email=$email")
                        "email_verification" -> navController.navigate(Screens.Profile.route)
                    }
                }
            )
        }
        composable(
            route = "${Screens.NewPassword.route}?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            // NewPasswordScreen(navController, hiltViewModel(), email)
        }
        composable(Screens.Home.route) {
            // HomeScreen(navController, hiltViewModel())
        }
        composable(Screens.Dashboard.route) {
            // DashboardScreen(navController, hiltViewModel())
        }
        composable(Screens.Profile.route) {
            // ProfileScreen(navController, hiltViewModel())
        }
        composable(Screens.Devices.route) {
            // DeviceScreen(navController, hiltViewModel())
        }
        composable(Screens.Settings.route) {
            // SettingsScreen(navController, hiltViewModel())
        }
        composable(
            route = "${Screens.AccessPoint.route}?id={id}&name={name}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType; defaultValue = "" },
                navArgument("name") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            // AccessPointConnectionScreen(navController, hiltViewModel(), id, name)
        }
        composable(Screens.AddDevice.route) {
            // AddDeviceScreen(navController, hiltViewModel())
        }
        composable(Screens.AllNotifications.route) {
            // NotificationScreen(navController, hiltViewModel())
        }
        composable(Screens.HouseManagement.route) {
            // HouseManagementScreen(navController, hiltViewModel())
        }
        composable(
            route = "${Screens.NotificationDetail.route}?id={id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            // DetailNotificationScreen(navController, hiltViewModel(), id)
        }
        composable(Screens.WifiConnection.route) {
            // WifiConnectionScreen(navController, hiltViewModel())
        }
        composable(Screens.Spaces.route) {
            // SpaceScreen(navController, hiltViewModel())
        }
        composable(Screens.AddSpace.route) {
            // AddSpaceScreen(navController, hiltViewModel())
        }

        composable(Screens.PasswordAuth.route) {
            // PasswordAuthenticationScreen(navController, hiltViewModel())
        }
        composable(
            route = "${Screens.UpdatePassword.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("id") ?: -1
            // UpdatePasswordScreen(navController, hiltViewModel(), userId)
        }
        composable(
            route = "${Screens.DashboardDeviceScreen.route}/{spaceID}/{id}",
            arguments = listOf(
                navArgument("spaceID") { type = NavType.IntType },
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val spaceID = backStackEntry.arguments?.getInt("spaceID") ?: -1
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            // DashboardDeviceScreen(navController, hiltViewModel(), spaceID, id)
        }
        composable(
            route = Screens.ActivityHistory.route,
            arguments = listOf(navArgument("deviceId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getInt("deviceId") ?: -1
            // ActivityHistoryScreen(navController, hiltViewModel(), deviceId)
        }
        composable(
            route = Screens.ActivityHistoryDetail.route,
            arguments = listOf(navArgument("logDetails") { type = NavType.StringType })
        ) { backStackEntry ->
            val logDetailsJson = backStackEntry.arguments?.getString("logDetails") ?: ""
            // ActivityHistoryDetailScreen(navController, hiltViewModel(), logDetailsJson)
        }
        composable(
            route = "${Screens.AddSharedUser.route}?id={id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            // ShareDeviceScreen(navController, hiltViewModel(), id)
        }
        composable(
            route = "${Screens.SharedUsers.route}?id={id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            // DeviceSharingListScreen(navController, hiltViewModel(), id)
        }
        composable("device/{typeID}/{id}") { backStackEntry ->
            val typeID = backStackEntry.arguments?.getString("typeID")?.toIntOrNull() ?: 0
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            // val screen = DeviceScreenFactory.getScreen(typeID)
            // screen(navController, id)
        }
    }
}