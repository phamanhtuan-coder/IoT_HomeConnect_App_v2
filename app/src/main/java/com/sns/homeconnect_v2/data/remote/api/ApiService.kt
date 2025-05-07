package com.sns.homeconnect_v2.data.remote.api

import com.sns.homeconnect_v2.data.remote.dto.request.AttributeRequest
import com.sns.homeconnect_v2.data.remote.dto.request.DeviceTokenRequest
import com.sns.homeconnect_v2.data.remote.dto.request.EmailRequest
import com.sns.homeconnect_v2.data.remote.dto.request.LinkDeviceRequest
import com.sns.homeconnect_v2.data.remote.dto.request.LoginRequest
import com.sns.homeconnect_v2.data.remote.dto.request.NewPasswordRequest
import com.sns.homeconnect_v2.data.remote.dto.request.RegisterRequest
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UserRequest
import com.sns.homeconnect_v2.data.remote.dto.response.Alert
import com.sns.homeconnect_v2.data.remote.dto.response.AlertDetail
import com.sns.homeconnect_v2.data.remote.dto.response.AlertResponse
import com.sns.homeconnect_v2.data.remote.dto.response.AttributeResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceTokenResponse
import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse
import com.sns.homeconnect_v2.data.remote.dto.response.HouseResponse
import com.sns.homeconnect_v2.data.remote.dto.response.LinkDeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.LoginResponse
import com.sns.homeconnect_v2.data.remote.dto.response.NewPasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.RegisterResponse
import com.sns.homeconnect_v2.data.remote.dto.response.SharedWithResponse
import com.sns.homeconnect_v2.data.remote.dto.response.SpaceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UnlinkResponse
import com.sns.homeconnect_v2.data.remote.dto.response.User
import com.sns.homeconnect_v2.data.remote.dto.response.UserResponse
import retrofit2.http.*

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("/api/users/reset-password")
    suspend fun newPassword(@Body request: NewPasswordRequest): NewPasswordResponse

    @POST("/api/otp/check-email")
    suspend fun checkEmail(@Body request: EmailRequest): EmailResponse

    @POST("/api/otp/send")
    suspend fun sendOTP(@Body request: EmailRequest): EmailResponse

    @POST("/api/otp/verify")
    suspend fun verifyOTP(@Body request: EmailRequest): EmailResponse

    @POST("/api/users/confirm-email")
    suspend fun confirmEmail(
        @Body request: EmailRequest,
        @Header("Authorization") token: String
    ): EmailResponse

    @POST("/api/auth/update-device-token")
    suspend fun sendToken(
        @Header("Authorization") token: String,
        @Body request: DeviceTokenRequest
    ): DeviceTokenResponse

    @GET("/api/auth/me")
    suspend fun getInfoProfile(@Header("Authorization") token: String): User

    @PUT("/api/users/{userId}")
    suspend fun putInfoProfile(@Path("userId") userId: Int, @Body user: UserRequest, @Header("Authorization") token: String): UserResponse

    @GET("/api/users/{userId}/shared-with")
    suspend fun sharedWith(
        @Path("userId") userId: Int,
        @Header("Authorization") token: String
    ): List<SharedWithResponse>

    @GET("/api/devices/{deviceId}")
    suspend fun getInfoDevice(
        @Path("deviceId") deviceId: Int,
        @Header("Authorization") token: String
    ): DeviceResponse

    @POST("/api/devices/{deviceId}/toggle")
    suspend fun toggleDevice(
        @Path("deviceId") deviceId: Int,
        @Body toggle: ToggleRequest,
        @Header("Authorization") token: String
    ): ToggleResponse

    @POST("/api/devices/{deviceId}/attributes")
    suspend fun updateAttributes(
        @Path("deviceId") deviceId: Int,
        @Body attribute: AttributeRequest,
        @Header("Authorization") token: String
    ): AttributeResponse

    @POST("/api/devices/{deviceId}/unlink")
    suspend fun unlinkDevice(
        @Path("deviceId") deviceId: Int,
        @Header("Authorization") token: String
    ): UnlinkResponse

    @POST("/api/devices/link")
    suspend fun linkDevice(
        @Body body: LinkDeviceRequest,
        @Header("Authorization") token: String
    ): LinkDeviceResponse

    @GET("/api/spaces/{homeId}")
    suspend fun getSpacesByHomeId(
        @Path("homeId") homeId: Int,
        @Header("Authorization") token: String
    ): List<SpaceResponse>

    @GET("/api/spaces/{spaceId}/devices")
    suspend fun getDevicesBySpaceId(
        @Path("spaceId") spaceId: Int,
        @Header("Authorization") token: String
    ): List<DeviceResponse>

    @GET("/api/houses")
    suspend fun getListHome(@Header("Authorization") token: String): List<HouseResponse>


    @GET("/api/alerts/getAllByUser")
    suspend fun getAllNotification(@Header("Authorization") token: String): List<AlertResponse>

    @GET("/api/alerts/{alertId}")
    suspend fun getAlertById(
        @Path("alertId") alertId: Int,
        @Header("Authorization") token: String
    ): AlertDetail

    @PUT("/api/alerts/{alertId}/resolve")
    suspend fun readNotification(
        @Path("alertId") alertId: Int,
        @Header("Authorization") token: String
    ): Alert

    @GET("/api/alerts/search")
    suspend fun searchAlerts(
        @Query("q") query: String,
        @Header("Authorization") token: String
    ): List<AlertResponse>

//
//    @GET("/api/logs/device/{id}")
//    suspend fun getDeviceLogs(
//        @Path("id") deviceId: Int,
//        @Header("Authorization") token: String
//    ): List<LogResponse>
//

//    @GET("/api/logs/latestToggle/{deviceId}")
//    suspend fun getLatestToggle(
//        @Path("deviceId") deviceId: Int,
//        @Header("Authorization") token: String
//    ): LogLastest
//
//    @GET("/api/logs/latestUpdateAttributes/{deviceId}")
//    suspend fun getLatestUpdateAttributes(
//        @Path("deviceId") deviceId: Int,
//        @Header("Authorization") token: String
//    ): LogLastest
//
//    @GET("/api/logs/latestSensor/{deviceId}")
//    suspend fun getLatestSensor(
//        @Path("deviceId") deviceId: Int,
//        @Header("Authorization") token: String
//    ): LogLastest


//    @PUT("/api/users/{userId}/change-password")
//    suspend fun putChangePassword(@Path("userId") userId: Int, @Body changePasswordRequest: ChangePasswordRequest, @Header("Authorization") token: String): ChangePasswordResponce
//
//    @GET("/api/statistics/daily-averages-sensor/{deviceId}/{startDate}/{endDate}")
//    suspend fun getDailyAveragesSensor(
//        @Path("deviceId") deviceId: Int,
//        @Path("startDate") startDate: String,
//        @Path("endDate") endDate: String,
//        @Header("Authorization") token: String
//    ): DailyAverageSensorResponse
//
//    @GET("/api/statistics/daily-power-usages/{deviceId}/{startDate}/{endDate}")
//    suspend fun getDailyPowerUsages(
//        @Path("deviceId") deviceId: Int,
//        @Path("startDate") startDate: String,
//        @Path("endDate") endDate: String,
//        @Header("Authorization") token: String
//    ): DailyPowerUsageResponse
//
//    @POST("/api/statistics/calculate-daily-average-sensor")
//    suspend fun calculateDailyAverageSensor(
//        @Body request: DailySensorRequest,
//        @Header("Authorization") token: String
//    ): AverageSensorResponse
//
//    @POST("/api/statistics/calculate-weekly-average-sensor")
//    suspend fun calculateWeeklyAverageSensor(
//        @Body request: WeeklySensorRequest,
//        @Header("Authorization") token: String
//    ): AverageSensorResponse
//
//    @POST("/api/statistics/calculate-average-sensor-for-range")
//    suspend fun calculateAverageSensorForRange(
//        @Body request: RangeSensorRequest,
//        @Header("Authorization") token: String
//    ): AverageSensorResponse
//
//    @GET("/api/statistics/weekly-average-sensor/{deviceId}")
//    suspend fun getWeeklyAverageSensor(
//        @Path("deviceId") deviceId: Int,
//        @Header("Authorization") token: String
//    ): WeeklyAverageSensorResponse
//
//    @GET("/api/houses")
//    suspend fun getHouses(
//        @Header("Authorization") token: String
//    ): List<HousesListPesponse>
//
//    @PUT("/api/houses/{houseId}")
//    suspend fun updateHouse(
//        @Path("houseId") houseId: Int,
//        @Body body: UpdateHouseRequest,
//        @Header("Authorization") token: String
//    ): UpdateHouseResponse
//
//    @POST("/api/houses")
//    suspend fun createHouse(
//        @Body request: CreateHouseRequest,
//        @Header("Authorization") token: String
//    ): CreateHouseResponse
//
//    @GET("/api/spaces/{houseId}")
//    suspend fun getSpaces(
//        @Path("houseId") houseId: Int,
//        @Header("Authorization") token: String
//    ): List<SpaceResponse2>
//
//    @PUT("/api/spaces/{id}")
//    suspend fun updateSpace(
//        @Path("id") spaceId: Int,
//        @Body body: UpdateSpaceRequest,
//        @Header("Authorization") token: String
//    ): SpaceResponse3
//
//    @POST("/api/spaces")
//    suspend fun createSpace(
//        @Body body: CreateSpaceRequest,
//        @Header("Authorization") token: String
//    ): CreateSpaceResponse

//    @GET("/api/statistics/daily-room-power-usage/{spaceId}/{startDate}/{endDate}")
//    suspend fun getDailyRoomPowerUsage(
//        @Path("spaceId") spaceId: Int,
//        @Path("startDate") startDate: String,
//        @Path("endDate") endDate: String,
//        @Header("Authorization") token: String
//    ): DailyPowerUsageResponse2
//
//    @GET("/api/statistics/daily-room-averages-sensor/{spaceId}/{startDate}/{endDate}")
//    suspend fun getDailyRoomAveragesSensor(
//        @Path("spaceId") spaceId: Int,
//        @Path("startDate") startDate: String,
//        @Path("endDate") endDate: String,
//        @Header("Authorization") token: String
//    ): DailySensorAveragesResponse
//
//    @POST("/api/statistics/calculate-daily-average-sensor")
//    suspend fun calculateDailyAverageSensor(
//        @Body body: DailyAverageSensorRequest,
//        @Header("Authorization") token: String
//    ): DailyAverageSensorResponse2
//
//    @POST("/api/statistics/calculate-daily-power-usage")
//    suspend fun calculateDailyPowerUsage(
//        @Body body: DailyPowerUsageRequest,
//        @Header("Authorization") token: String
//    ): DailyPowerUsageResponse3

//    @DELETE("/api/sharedpermissions/revoke/{permissionId}")
//    suspend fun revokePermission(
//        @Path("permissionId") permissionId: Int,
//        @Header("Authorization") token: String
//    ): retrofit2.Response<Unit>
//
//    @GET("/api/sharedpermissions/{deviceId}/shared-users")
//    suspend fun getSharedUsers(
//        @Path("deviceId") deviceId: Int,
//        @Header("Authorization") token: String
//    ): List<SharedUser>
//
//    @POST("/api/sharedpermissions/{deviceId}/share")
//    suspend fun shareDevice(
//        @Path("deviceId") deviceId: Int,
//        @Body sharedWithUserEmail: SharedUserRequest,
//        @Header("Authorization") token: String
//    ): retrofit2.Response<Unit>
//

//    @GET("/api/spaces/{spaceId}/detail")
//    suspend fun getSpaceDetail(
//        @Path("spaceId") spaceId: Int,
//        @Header("Authorization") token: String
//    ): SpaceDetailResponse
//
//    data class UpdateSpaceRequest(val Name: String)
}