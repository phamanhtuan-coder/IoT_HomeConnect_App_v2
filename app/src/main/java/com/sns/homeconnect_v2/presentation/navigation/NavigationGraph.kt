package com.sns.homeconnect_v2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.sns.homeconnect_v2.presentation.screen.auth.LoginScreen
import com.sns.homeconnect_v2.presentation.screen.auth.NewPasswordScreen
import com.sns.homeconnect_v2.presentation.screen.auth.RecoverPasswordScreen
import com.sns.homeconnect_v2.presentation.screen.auth.RegisterScreen
import com.sns.homeconnect_v2.presentation.screen.home.HomeScreen
import com.sns.homeconnect_v2.presentation.screen.house.HouseManagementScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.LinkDeviceScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.access_point_connection.AccessPointConnectionScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.access_point_connection.WifiConnectionScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.sharing.DeviceSharingListScreen
import com.sns.homeconnect_v2.presentation.screen.notification.DetailNotificationScreen
import com.sns.homeconnect_v2.presentation.screen.notification.NotificationScreen
import com.sns.homeconnect_v2.presentation.screen.otp.OtpScreen
import com.sns.homeconnect_v2.presentation.screen.profile.ProfileScreen
import com.sns.homeconnect_v2.presentation.screen.profile.UpdatePasswordScreen
import com.sns.homeconnect_v2.presentation.screen.setting.SettingsScreen
import com.sns.homeconnect_v2.presentation.screen.welcome.WelcomeScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.Welcome.route) {
        // --- Auth screens ---
        composable(Screens.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(Screens.Login.route) {
            LoginScreen(navController)
        }
        composable(Screens.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screens.RecoverPassword.route) {
            RecoverPasswordScreen(navController)
        }
        composable(
            route = "${Screens.NewPassword.route}?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            NewPasswordScreen(navController, email)
        }
        // TODO: Add PasswordAuth screen

        // --- OTP screens ---
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

        // Nested graph for authenticated screens
        navigation(startDestination = Screens.Home.route, route = "home_graph") {
            // --- Home/Dashboard screens ---
            composable(Screens.Home.route) {
                HomeScreen(navController)
            }
            // TODO: Add Dashboard screen
            // TODO: Add DashboardDeviceScreen

            // --- Profile screens ---
            composable(Screens.Profile.route) {
                ProfileScreen(navController)
            }

            // --- Setting screens ---
            composable(Screens.Settings.route) {
                SettingsScreen(navController)
            }

            // --- House screens ---
            composable(Screens.HouseManagement.route) {
                HouseManagementScreen(navController)
            }
            // TODO: Add AddHouse screen
            // TODO: Add EditHouse screen with route "edit_house/{houseId}"

            // --- Space screens ---
            // TODO: Add Spaces screen
            // TODO: Add AddSpace screen
            // TODO: Add SpaceManagement screen with route "space_management/{houseId}"
            // TODO: Add AddSpaceWithHouse screen with route "add_space/{houseId}"
            // TODO: Add EditSpace screen with route "edit_space/{spaceId}"
            // TODO: Add SpaceDetail screen with route "space_detail/{spaceId}"

            // --- Group screens ---
            // TODO: Add Groups screen
            // TODO: Add CreateGroup screen
            // TODO: Add GroupDetail screen with route "group_detail/{groupId}"
            // TODO: Add AddGroupUser screen with route "add_group_user/{groupId}"

            // --- IoT Device screens ---
            composable(Screens.AddDevice.route) {
                LinkDeviceScreen(navController)
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
                AccessPointConnectionScreen(navController, id, name)
            }
            composable(Screens.WifiConnection.route) {
                WifiConnectionScreen(navController)
            }

            // TODO: Add ActivityHistory screen with route "activity_history?deviceId={deviceId}"
            // TODO: Add ActivityHistoryDetail screen with route "activity_history_detail/{logDetails}"
            // TODO: Add FireAlarmDetail screen
            // TODO: Add DefaultDetail screen with route "default_detail/{deviceId}"
            // TODO: Add SoftwareVersion screen with route "software_version/{deviceId}"
            // TODO: Add ReportLostDevice screen with route "report_lost_device/{deviceId}"
            // TODO: Add DeviceSharing screen with route "device_sharing/{deviceId}"

            composable(
                route = "${Screens.SharedUsers.route}?id={id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: -1
                DeviceSharingListScreen(navController, id)
            }
            composable("device/{typeID}/{id}") { backStackEntry ->
                val typeID = backStackEntry.arguments?.getString("typeID")?.toIntOrNull() ?: 0
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                val screen = DeviceScreenFactory.getScreen(typeID)
                screen(navController, id)
            }

            // --- Notification screens ---
            composable(Screens.AllNotifications.route) {
                NotificationScreen(navController)
            }
            composable(
                route = "${Screens.NotificationDetail.route}?id={id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                DetailNotificationScreen(navController, id)
            }

            // --- Password Update screen ---
            composable(
                route = "${Screens.UpdatePassword.route}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("id") ?: -1
                UpdatePasswordScreen(navController, userId)
            }
        }
    }
}