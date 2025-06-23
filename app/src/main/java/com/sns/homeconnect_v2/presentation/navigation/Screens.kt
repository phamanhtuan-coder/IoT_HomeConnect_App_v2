package com.sns.homeconnect_v2.presentation.navigation

import android.net.Uri

sealed class Screens(val route: String) {
    // --- Auth screens ---
    data object Login : Screens("login")
    data object Register : Screens("register")
    data object RecoverPassword : Screens("recover_password")
    data object UpdatePassword : Screens("update_password/{id}") {
        fun createRoute(id: Int) = "update_password/$id"
    }
    data object ForgotPassword : Screens("ForgotPassword?email={email}") {
        fun createRoute(email: String) = "ForgotPassword?email=$email"
    }
    data object PasswordAuth : Screens("password_auth")
    data object Welcome : Screens("welcome")

    // --- OTP screens ---
    data object OTP : Screens("otp/{type}/{email}") {
        fun createRoute(type: String, email: String) = "otp/$type/$email"
    }

    // --- Home/Dashboard screens ---
    data object Home : Screens("home")
    data object Dashboard : Screens("dashboard")
    data object DashboardDeviceScreen : Screens("dashboard_device")

    // --- Profile screens ---
    data object Profile : Screens("profile")

    // --- Setting screens ---
    data object Settings : Screens("settings")
    data object UserActivity : Screens("user_activity")

    // --- House screens ---
    data object HouseManagement : Screens("house_management")
    data object AddHouse : Screens("add_house")
    data object CreateHouse : Screens("create_house/{groupId}") {
        fun createRoute(groupId: Int) = "create_house/$groupId"
    }
    data object EditHouse : Screens("edit_house/{houseId}") {
        fun createRoute(houseId: Int) = "edit_house/$houseId"
    }
    data object HouseSearch : Screens("house_search")

    // --- Space screens ---
    data object Spaces : Screens("spaces")

    data object AddSpace : Screens("create_space/{houseId}") {
        fun createRoute(houseId: Int) = "create_space/$houseId"
    }

//    data object AddSpace : Screens("add_space") {
//        fun createRoute(houseId: Int) = "add_space"
//    }

    data object EditSpaceWithHouse : Screens("edit_space/{houseId}") {
        fun createRoute(houseId: Int) = "edit_space/$houseId"
    }

    data object SpaceManagement : Screens("space_management/{houseId}") {
        fun createRoute(houseId: Int) = "space_management/$houseId"
    }
    data object AddSpaceWithHouse : Screens("add_space/{houseId}") {
        fun createRoute(houseId: Int) = "add_space/$houseId"
    }
    data object EditSpace : Screens("edit_space/{spaceId}") {
        fun createRoute(spaceId: Int) = "edit_space/$spaceId"
    }

    data object SpaceDetail : Screens("space_detail/{spaceId}") {
        fun createRoute(spaceId: Int) = "space_detail/$spaceId"
    }

    data object ListSpace : Screens("list_space/{houseId}/{currentUserRole}") {
        fun createRoute(houseId: Int,currentUserRole: String) = "list_space/$houseId/$currentUserRole"
    }

    // --- Group screens ---
    data object Groups : Screens("groups")
    data object CreateGroup : Screens("create_group")
    data object GroupDetail : Screens("group_detail/{groupId}") {
        fun createRoute(groupId: Int) = "group_detail/$groupId"
    }
    data object AddGroupUser : Screens("add_group_user/{groupId}") {
        fun createRoute(groupId: Int) = "add_group_user/$groupId"
    }
    data object AddUser : Screens("add_user/{groupId}") {
        fun createRoute(groupId: Int) = "add_user/$groupId"
    }

    // --- IoT Device screens ---
    data object Devices : Screens("devices")
    data object AddDevice : Screens("add_device")
    // Screens.kt
    object DeviceDetail {
        const val base = "device-detail"
        const val route = "$base/{deviceId}"
        fun build(deviceId: Int): String = "$base/$deviceId"
    }

    data object DeviceByType : Screens("device/{typeID}/{id}") {
        fun createRoute(typeID: Int, id: Int) = "device/$typeID/$id"
    }
    data object AccessPoint : Screens("access_point?id={id}&name={name}") {
        fun createRoute(id: String, name: String) = "access_point?id=$id&name=$name"
    }
    data object WifiConnection : Screens("wifi_connection")
    data object ActivityHistory : Screens("activity_history?deviceId={deviceId}") {
        fun createRoute(deviceId: Int): String = "activity_history?deviceId=$deviceId"
    }
    data object ActivityHistoryDetail : Screens("activity_history_detail/{logDetails}") {
        fun createRoute(logDetails: String): String = "activity_history_detail/$logDetails"
    }
    // Screens.kt
    data object FireAlarmDetail : Screens("fire_alarm_detail/{productJson}") {
        fun createRoute(productJson: String) = "fire_alarm_detail/$productJson"
    }
    data object DefaultDetail : Screens("default_detail/{deviceId}") {
        fun createRoute(deviceId: Int) = "default_detail/$deviceId"
    }
    data object SoftwareVersion : Screens("software_version/{deviceId}") {
        fun createRoute(deviceId: Int) = "software_version/$deviceId"
    }
    data object ReportLostDevice : Screens("report_lost_device/{deviceId}") {
        fun createRoute(deviceId: Int) = "report_lost_device/$deviceId"
    }
    data object DeviceSharing : Screens("device_sharing/{deviceId}") {
        fun createRoute(deviceId: Int) = "device_sharing/$deviceId"
    }
    data object SharedUsers : Screens("shared_users?id={id}") {
        fun createRoute(id: Int) = "shared_users?id=$id"
    }
    data object AddSharedUser : Screens("add_shared_user?id={id}") {
        fun createRoute(id: Int) = "add_shared_user?id=$id"
    }
    data object TransferOwnership : Screens("transfer_ownership/{deviceId}") {
        fun createRoute(deviceId: Int) = "transfer_ownership/$deviceId"
    }
    data object ListDevices : Screens("list_devices")

    data object ListTicket : Screens("list_ticket")
    data object DetailTicket : Screens("detail_ticket/{ticketId}") {
        fun createRoute(ticketId: String) = "detail_ticket/$ticketId"
    }

    // --- Notification screens ---
    data object AllNotifications : Screens("all_notifications")
    data object NotificationDetail : Screens("notification_detail?id={id}") {
        fun createRoute(id: Int) = "notification_detail?id=$id"
    }
    data object ListNotifications : Screens("list_notifications")
    data object DetailNotifications : Screens("detail_notifications/{notificationId}") {
        fun createRoute(notificationId: Int) = "detail_notifications/$notificationId"
    }

    object DynamicDeviceDetail :
        Screens("dynamic_device_detail/{deviceId}/{deviceName}/{serialNumber}/{productId}") {

        fun build(
            deviceId: String,
            deviceName: String,
            serialNumber: String,
            productId: String
        ): String =
            listOf(
                "dynamic_device_detail",
                Uri.encode(deviceId),
                Uri.encode(deviceName),
                Uri.encode(serialNumber),
                Uri.encode(productId)
            ).joinToString("/")
    }

    // --- Camera screens ---
    data object CameraDetail : Screens(
        "camera_detail/{deviceId}/{deviceName}"   // khai báo tham số tuỳ ý
    ) {
        fun createRoute(deviceId: String, deviceName: String) =
            "camera_detail/$deviceId/$deviceName"
    }

    // --- Other screens ---
    // ...add more here if needed...
}
