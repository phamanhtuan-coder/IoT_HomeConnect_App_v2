package com.sns.homeconnect_v2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.sns.homeconnect_v2.presentation.screen.auth.ForgotPasswordScreen
import com.sns.homeconnect_v2.presentation.screen.auth.LoginScreen
import com.sns.homeconnect_v2.presentation.screen.auth.RecoverPasswordScreen
import com.sns.homeconnect_v2.presentation.screen.auth.RegisterScreen
import com.sns.homeconnect_v2.presentation.screen.auth.UserActivityManagementScreen
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
import com.sns.homeconnect_v2.presentation.screen.group.GroupScreen
import com.sns.homeconnect_v2.presentation.screen.group.CreateGroupScreen
import com.sns.homeconnect_v2.presentation.screen.group.DetailGroupScreen
import com.sns.homeconnect_v2.presentation.screen.group.house.CreateHouseScreen
import com.sns.homeconnect_v2.presentation.screen.group.house.HouseDetailScreen
import com.sns.homeconnect_v2.presentation.screen.group.house.space.CreateSpaceScreen
import com.sns.homeconnect_v2.presentation.screen.group.user.AddUserScreen
import com.sns.homeconnect_v2.presentation.screen.house.HouseSearchScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.DefaultDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.ListDeviceScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.SoftwareVersionScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.ReportLostDeviceScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.TransferOwnershipScreen
import com.sns.homeconnect_v2.presentation.viewmodel.snackbar.SnackbarViewModel
import com.sns.homeconnect_v2.presentation.viewmodel.space.SpaceScreenViewModel
import com.sns.homeconnect_v2.presentation.screen.group.house.space.DetailSpaceScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.DashboardScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.DynamicDeviceDetailScreen
import com.sns.homeconnect_v2.presentation.screen.iot_device.sharing.ShareDeviceScreen
import com.sns.homeconnect_v2.presentation.screen.ticket.CreateTicketScreen
import com.sns.homeconnect_v2.presentation.screen.ticket.TicketDetailScreen
import com.sns.homeconnect_v2.ticket_screen.TicketListScreen

@Composable
fun NavigationGraph(navController: NavHostController, snackbarViewModel: SnackbarViewModel,
                    ) {
    NavHost(navController = navController, startDestination = Screens.Welcome.route) {
        // --- Auth screens ---
        composable(Screens.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(Screens.Login.route) {
            LoginScreen(
                navController = navController,
                snackbarViewModel = snackbarViewModel
            )
        }
        composable(Screens.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screens.RecoverPassword.route) {
            RecoverPasswordScreen(
                navController,
                snackbarViewModel = snackbarViewModel
            )
        }
        composable(
            route = "${Screens.ForgotPassword.route}?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ForgotPasswordScreen(email = email, navController = navController, snackbarViewModel = snackbarViewModel)
        }

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
                        "reset_password" -> navController.navigate("${Screens.ForgotPassword.route}?email=$email")
                        "email_verification" -> navController.navigate(Screens.Profile.route)
                    }
                },
                snackbarViewModel = snackbarViewModel,
                navController = navController
            )
        }

        // Nested graph for authenticated screens
        navigation(startDestination = Screens.Home.route, route = "home_graph") {
            // --- Home/Dashboard screens ---
            composable(Screens.Home.route) {
                HomeScreen(navController)
            }
            // TODO: Add Dashboard screen

            composable(Screens.Dashboard.route) {          // ✅ NEW
                DashboardScreen(navController)
            }
            // TODO: Add DashboardDeviceScreen

            composable(Screens.UserActivity.route) {
                UserActivityManagementScreen(navController, snackbarViewModel)
            }

            // --- Profile screens ---
            composable(Screens.Profile.route) {
                ProfileScreen(navController)
            }

            // --- Setting screens ---
            composable(Screens.Settings.route) {
                SettingsScreen(
                    navController,
                    snackbarViewModel = snackbarViewModel
                )
            }

            // --- House screens ---
            composable(Screens.HouseManagement.route) {
                HouseManagementScreen(navController)
            }

            composable(Screens.AddHouse.route) {
                CreateHouseScreen(
                    navController = navController,
                    snackbarViewModel = snackbarViewModel,
                    groupId = -1 // Default value for groupId when creating a new house
                )
            }

            composable(
                route = Screens.CreateHouse.route,
                arguments = listOf(navArgument("groupId") { type = NavType.IntType })
            ) { backStackEntry ->
                val groupId = backStackEntry.arguments?.getInt("groupId") ?: -1
                CreateHouseScreen(
                    navController = navController,
                    snackbarViewModel = snackbarViewModel,
                    groupId = groupId
                )
            }

            composable(Screens.HouseSearch.route) {
                HouseSearchScreen(
                    modifier = Modifier,
                    navController = navController,
                )
            }

            // --- Space screens ---
            // TODO: Add Spaces screen
            // TODO: Add AddSpace screen
            composable(
                route = Screens.AddSpace.route,
                arguments = listOf(navArgument("houseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val houseId = backStackEntry.arguments?.getInt("houseId") ?: -1
                CreateSpaceScreen(
                    navController = navController,
                    snackbarViewModel = snackbarViewModel,
                    houseId = houseId
                )
            }
            // TODO: Add SpaceManagement screen with route "space_management/{houseId}"
            // TODO: Add AddSpaceWithHouse screen with route "add_space/{houseId}"
            // TODO: Add EditSpace screen with route "edit_space/{spaceId}"
            // TODO: Add SpaceDetail screen with route "space_detail/{spaceId}"

            // --- Group screens ---
            composable(Screens.Groups.route) {
                GroupScreen(
                    navController = navController,
                    snackbarViewModel = snackbarViewModel
                )
            }
            composable(Screens.CreateGroup.route) {
                CreateGroupScreen(
                    navController,
                    snackbarViewModel = snackbarViewModel,
                )
            }
            composable(
                route = Screens.GroupDetail.route,
                arguments = listOf(navArgument("groupId") { type = NavType.IntType })
            ) { backStackEntry ->
                val groupId = backStackEntry.arguments?.getInt("groupId") ?: -1
                DetailGroupScreen(
                    navController = navController,
                    snackbarViewModel = snackbarViewModel,
                    groupId = groupId
                )
            }

//ds space
            composable(
                route = Screens.ListSpace.route,
                arguments = listOf(navArgument("houseId") { type = NavType.IntType })
            ) {
                backStackEntry ->
                val spaceViewModel: SpaceScreenViewModel= hiltViewModel()
                val houseId = backStackEntry.arguments?.getInt("houseId") ?: -1
                val currentUserRole= backStackEntry.arguments?.getString("currentUserRole") ?: "member"
                HouseDetailScreen(
                    navController = navController,
                    snackbarViewModel = snackbarViewModel,
                    spaceViewModel = spaceViewModel,
                    houseId = houseId,
                    currentUserRole = currentUserRole
                )
            }

            //ds space detail
            composable(
                route  = Screens.SpaceDetail.route,
                arguments = listOf(
                    navArgument("spaceId")         { type = NavType.IntType },
                    navArgument("currentUserRole") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val spaceId         = backStackEntry.arguments?.getInt("spaceId") ?: -1
                val currentUserRole = backStackEntry.arguments?.getString("currentUserRole") ?: "member"

                DetailSpaceScreen(
                    modifier       = Modifier,
                    navController  = navController,
                    spaceId        = spaceId,
                    currentUserRole = currentUserRole          // 🌟 mới
                )
            }


//            composable(
//                route = Screens.AddSpace.route,
//                arguments = listOf(navArgument("houseId") { type = NavType.IntType })
//            ) { backStackEntry ->
//                CreateSpaceScreen(
//                        navController = navController,
//                        snackbarViewModel = snackbarViewModel
//                )
//            }

            // TODO: Add AddGroupUser screen with route "add_group_user/{groupId}"

            composable(
                route = Screens.AddUser.route,
                arguments = listOf(navArgument("groupId") { type = NavType.IntType })
            ) { backStackEntry ->
                val groupId = backStackEntry.arguments?.getInt("groupId") ?: -1
                AddUserScreen(
                    navController = navController,
                    snackbarViewModel = snackbarViewModel,
                    groupId = groupId
                )
            }

            // --- IoT Device screens ---
            composable(Screens.AddDevice.route) {
                LinkDeviceScreen(
                    navController,
                    snackbarViewModel = snackbarViewModel,
                )
            }
            composable(Screens.WifiConnection.route) {
                WifiConnectionScreen(navController)
            }

            composable(Screens.ListDevices.route) {
                ListDeviceScreen(navController)
            }

//            composable(
//                route = "${Screens.FireAlarmDetail.route}/{productJson}",
//                arguments = listOf(navArgument("productJson") { type = NavType.StringType })
//            ) { backStackEntry ->
//                val productJson = backStackEntry.arguments?.getString("productJson") ?: ""
//                val product = Gson().fromJson(productJson, ProductData::class.java)
//
//                FireAlarmDetailScreen(navController, , product)
//            }


            composable(
                route = Screens.DefaultDetail.route,
                arguments = listOf(navArgument("deviceId") { type = NavType.IntType })
            ) { backStackEntry ->
                val deviceId = backStackEntry.arguments?.getInt("deviceId") ?: -1
                DefaultDetailScreen(navController)
            }

            composable(
                route = Screens.SoftwareVersion.route,
                arguments = listOf(navArgument("deviceId") { type = NavType.IntType })
            ) { backStackEntry ->
                val deviceId = backStackEntry.arguments?.getInt("deviceId") ?: -1
                SoftwareVersionScreen(navController) // Todo: Pass deviceId to SoftwareVersionScreen
            }

            composable(
                route = Screens.ReportLostDevice.route,
                arguments = listOf(navArgument("deviceId") { type = NavType.IntType })
            ) { backStackEntry ->
                val deviceId = backStackEntry.arguments?.getInt("deviceId") ?: -1
                ReportLostDeviceScreen(navController) //Todo: Pass deviceId to ReportLostDeviceScreen
            }

            composable(
                route = Screens.TransferOwnership.route,
                arguments = listOf(navArgument("deviceId") { type = NavType.IntType })
            ) { backStackEntry ->
                val deviceId = backStackEntry.arguments?.getInt("deviceId") ?: -1
                TransferOwnershipScreen(navController) // Todo: Pass deviceId to TransferOwnershipScreen
            }

            // TODO: Add ActivityHistory screen with route "activity_history?deviceId={deviceId}"
            // TODO: Add ActivityHistoryDetail screen with route "activity_history_detail/{logDetails}"
            // TODO: Add DeviceSharing screen with route "device_sharing/{deviceId}"

            composable(
                route = "${Screens.SharedUsers.route}?id={id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: -1
                DeviceSharingListScreen(navController, id)
            }

//            composable("device/{typeID}/{id}") { backStackEntry ->
//                val id = backStackEntry.arguments?.getString("id") ?: ""
//                DynamicDeviceDetailScreen(
//                    productId = id,
//                    navController = navController
//                )
//            }

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

            composable(
                route = Screens.AccessPoint.route,
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType; defaultValue = "" },
                    navArgument("name") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                val name = backStackEntry.arguments?.getString("name") ?: ""
                AccessPointConnectionScreen(navController, id, name)
            }

            composable(
                route = Screens.DynamicDeviceDetail.route,
                arguments = listOf(
                    navArgument("deviceId") { type = NavType.StringType },
                    navArgument("deviceName") { type = NavType.StringType },
                    navArgument("serialNumber") { type = NavType.StringType },
                    navArgument("productId") { type = NavType.StringType },
                    navArgument("groupId") {type = NavType.IntType},
                    navArgument("permissionType") { type = NavType.StringType },
                    navArgument("deviceTypeName") { type = NavType.StringType; nullable = true },
                    navArgument("deviceTypeParentName") { type = NavType.StringType; nullable = true }
                )
            ) { backStackEntry ->
                val deviceId       = backStackEntry.arguments?.getString("deviceId") ?: ""
                val deviceName     = backStackEntry.arguments?.getString("deviceName") ?: ""
                val serialNumber   = backStackEntry.arguments?.getString("serialNumber") ?: ""
                val productId      = backStackEntry.arguments?.getString("productId") ?: ""
                val groupId        = backStackEntry.arguments?.getInt("groupId")
                val permissionType = backStackEntry.arguments?.getString("permissionType") ?: "CONTROL"
                val isViewOnly     = permissionType.uppercase() == "VIEW"
                val deviceTypeName        = backStackEntry.arguments?.getString("deviceTypeName")
                val deviceTypeParentName  = backStackEntry.arguments?.getString("deviceTypeParentName")

                DynamicDeviceDetailScreen(
                    deviceId = deviceId,
                    deviceName = deviceName,
                    serialNumber = serialNumber,
                    productId = productId,
                    isViewOnly = isViewOnly,
                    groupId = groupId ?: 0,
                    navController = navController,
                    snackbarViewModel = snackbarViewModel,
                    deviceTypeName = deviceTypeName,
                    deviceTypeParentName = deviceTypeParentName
                )
            }

            composable(
                route = Screens.ListTicket.route,
            ) {
                TicketListScreen(navController = navController)
            }

            composable(
                route = Screens.DetailTicket.route, // "detail_ticket/{ticketId}"
                arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
            ) { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getString("ticketId") ?: ""
                TicketDetailScreen(
                    navController = navController,
                    ticketId = ticketId,
                    snackbarViewModel = hiltViewModel()
                )
            }
              
            composable(
                route = Screens.CreateTicket.route
            ) {
                CreateTicketScreen(
                    navController     = navController,
                    snackbarViewModel = snackbarViewModel
                )
            }

            // --- Ticket screens ---
            composable(
                route = Screens.ShareDeviceBySerial.route,
                arguments = listOf(navArgument("serialNumber") { type = NavType.StringType })
            ) { backStackEntry ->
                val serialNumber = backStackEntry.arguments?.getString("serialNumber") ?: ""
                ShareDeviceScreen(
                    navController = navController,
                    serialNumber = serialNumber,
                    snackbarViewModel = snackbarViewModel
                )
            }
        }
    }
}
