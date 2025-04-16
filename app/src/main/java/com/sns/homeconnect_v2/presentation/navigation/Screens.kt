package com.sns.homeconnect_v2.presentation.navigation

sealed class Screens(val route: String) {
    data object Home : Screens("home")
    data object Login : Screens("login")
    data object Welcome : Screens("welcome")
    data object Register : Screens("register")
    data object RecoverPassword : Screens("recover_password")
    data object UpdatePassword : Screens("update_password")
    data object NewPassword : Screens("new_password")
    data object OTP : Screens("otp/{type}/{email}") {
        fun createRoute(type: String, email: String) = "otp/$type/$email"
    }
    data object PasswordAuth : Screens("password_auth")
    data object Profile : Screens("profile")
    data object AddDevice : Screens("add_device")
    data object Devices : Screens("devices")
    data object DeviceDetail : Screens("device_detail")
    data object AccessPoint : Screens("access_point")
    data object AllNotifications : Screens("all_notifications")
    data object NotificationDetail : Screens("notification_detail")
    data object HouseManagement : Screens("house_management")
    data object ActivityHistory : Screens("activity_history?deviceId={deviceId}") {
        fun createRoute(deviceId: Int): String = "activity_history?deviceId=$deviceId"
    }
    data object ActivityHistoryDetail : Screens("activity_history_detail/{logDetails}") {
        fun createRoute(logDetails: String): String = "activity_history_detail/$logDetails"
    }
    data object WifiConnection : Screens("wifi_connection")
    data object Spaces : Screens("spaces")
    data object AddSpace : Screens("add_space")
    data object FireAlarmDetail : Screens("fire_alarm_detail")
    data object SharedUsers : Screens("shared_users")
    data object AddSharedUser : Screens("add_shared_user")
    data object DashboardDeviceScreen : Screens("dashboard_device")
    data object Settings : Screens("settings")
    data object Dashboard : Screens("dashboard")
}