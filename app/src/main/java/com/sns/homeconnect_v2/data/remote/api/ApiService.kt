package com.sns.homeconnect_v2.data.remote.api

import com.sns.homeconnect_v2.data.remote.dto.base.ApiResponse
import com.sns.homeconnect_v2.data.remote.dto.base.CreateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.request.AddGroupMemberRequest
import com.sns.homeconnect_v2.data.remote.dto.request.AttributeRequest
import com.sns.homeconnect_v2.data.remote.dto.request.BulkDeviceStateUpdateRequest
import com.sns.homeconnect_v2.data.remote.dto.request.ChangePasswordRequest
import com.sns.homeconnect_v2.data.remote.dto.request.CheckEmailRequest
import com.sns.homeconnect_v2.data.remote.dto.request.CreateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.request.DeviceTokenRequest
import com.sns.homeconnect_v2.data.remote.dto.request.EmailRequest
import com.sns.homeconnect_v2.data.remote.dto.request.LinkDeviceRequest
import com.sns.homeconnect_v2.data.remote.dto.request.LoginRequest
import com.sns.homeconnect_v2.data.remote.dto.request.NewPasswordRequest
import com.sns.homeconnect_v2.data.remote.dto.request.RegisterRequest
import com.sns.homeconnect_v2.data.remote.dto.request.ToggleRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UserRequest
import com.sns.homeconnect_v2.data.remote.dto.response.Alert
import com.sns.homeconnect_v2.data.remote.dto.response.AlertDetail
import com.sns.homeconnect_v2.data.remote.dto.response.AlertResponse
import com.sns.homeconnect_v2.data.remote.dto.response.AttributeResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ChangePasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceTokenResponse
import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse
import com.sns.homeconnect_v2.data.remote.dto.response.HouseWithSpacesResponse
import com.sns.homeconnect_v2.data.remote.dto.response.LinkDeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.LoginResponse
import com.sns.homeconnect_v2.data.remote.dto.response.MemberResponse
import com.sns.homeconnect_v2.data.remote.dto.response.MyGroupsWrapper
import com.sns.homeconnect_v2.data.remote.dto.response.NewPasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.RegisterResponse
import com.sns.homeconnect_v2.data.remote.dto.response.SharedUser
import com.sns.homeconnect_v2.data.remote.dto.response.SharedUserRequest
import com.sns.homeconnect_v2.data.remote.dto.response.SharedWithResponse
import com.sns.homeconnect_v2.data.remote.dto.response.SpaceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ToggleResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UnlinkResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.response.User
import com.sns.homeconnect_v2.data.remote.dto.response.UserActivityResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UserGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UserResponse
import com.sns.homeconnect_v2.data.remote.dto.request.CreateHouseRequest
import com.sns.homeconnect_v2.data.remote.dto.request.CreateSpaceRequest
import com.sns.homeconnect_v2.data.remote.dto.request.CreateTicketRequest
import com.sns.homeconnect_v2.data.remote.dto.request.DeviceCapabilitiesRequest
import com.sns.homeconnect_v2.data.remote.dto.request.RecoveryPasswordRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateDeviceStateRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateGroupMemberRoleRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateSpaceRequest
import com.sns.homeconnect_v2.data.remote.dto.response.CheckEmailResponse
import com.sns.homeconnect_v2.data.remote.dto.response.BulkDeviceStateUpdateResponse
import com.sns.homeconnect_v2.data.remote.dto.response.CreateTicketResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceCapabilitiesResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponseSpace
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceStateResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ForgotPasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.OwnedDeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.RoleResponse
import com.sns.homeconnect_v2.data.remote.dto.response.TicketDetailResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateDeviceStateResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateGroupMemberRoleResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.CreateHouseResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.House
import com.sns.homeconnect_v2.data.remote.dto.response.house.HouseResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.HousesListResponse
import com.sns.homeconnect_v2.data.remote.dto.response.TicketResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.UpdateHouseRequest
import com.sns.homeconnect_v2.data.remote.dto.response.house.UpdateHouseResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("users/reset-password")
    suspend fun newPassword(
        @Body request: NewPasswordRequest
    ): NewPasswordResponse

    @POST("otp/check-email")
    suspend fun checkEmail(
        @Body request: EmailRequest
    ): EmailResponse

    @POST("otp/send")
    suspend fun sendOTP(
        @Body request: EmailRequest
    ): EmailResponse

    @POST("users/confirm-email")
    suspend fun confirmEmail(
        @Body request: EmailRequest,
        @Header("Authorization") token: String
    ): EmailResponse

    @POST("auth/update-device-token")
    suspend fun sendToken(
        @Header("Authorization") token: String,
        @Body request: DeviceTokenRequest
    ): DeviceTokenResponse

    @GET("auth/me")
    suspend fun getInfoProfile(@Header("Authorization") token: String): User

    @PUT("users/{userId}")
    suspend fun putInfoProfile(
        @Path("userId") userId: Int,
        @Body user: UserRequest,
        @Header("Authorization") token: String
    ): UserResponse

    @GET("users/{userId}/shared-with")
    suspend fun sharedWith(
        @Path("userId") userId: Int,
        @Header("Authorization") token: String
    ): List<SharedWithResponse>

    @GET("devices/{deviceId}")
    suspend fun getInfoDevice(
        @Path("deviceId") deviceId: Int,
        @Header("Authorization") token: String
    ): DeviceResponse

    @POST("devices/{deviceId}/toggle")
    suspend fun toggleDevice(
        @Path("deviceId") deviceId: Int,
        @Body toggle: ToggleRequest,
        @Header("Authorization") token: String
    ): ToggleResponse

    @POST("devices/{deviceId}/attributes")
    suspend fun updateAttributes(
        @Path("deviceId") deviceId: Int,
        @Body attribute: AttributeRequest,
        @Header("Authorization") token: String
    ): AttributeResponse

    @POST("devices/{deviceId}/unlink")
    suspend fun unlinkDevice(
        @Path("deviceId") deviceId: Int,
        @Header("Authorization") token: String
    ): UnlinkResponse
  
    @GET("houses/{houseId}")
    suspend fun getHouseId(
        @Path("houseId") houseId: Int,
        @Header("Authorization") token: String
    ): House

//    @GET("spaces/{homeId}")
//    suspend fun getSpacesByHomeId(
//        @Path("homeId") homeId: Int,
//        @Header("Authorization") token: String
//    ): House

    @GET("devices/space/{spaceId}")
    suspend fun getDevicesBySpaceId(
        @Path("spaceId") spaceId: Int,
        @Header("Authorization") token: String
    ): List<DeviceResponseSpace>

    @GET("houses")
    suspend fun getListHome(
        @Header("Authorization") token: String
    ): List<HouseResponse>


    @GET("alerts/getAllByUser")
    suspend fun getAllNotification(
        @Header("Authorization") token: String
    ): List<AlertResponse>

    @GET("alerts/{alertId}")
    suspend fun getAlertById(
        @Path("alertId") alertId: Int,
        @Header("Authorization") token: String
    ): AlertDetail

    @PUT("alerts/{alertId}/resolve")
    suspend fun readNotification(
        @Path("alertId") alertId: Int,
        @Header("Authorization") token: String
    ): Alert

    @GET("alerts/search")
    suspend fun searchAlerts(
        @Query("q") query: String,
        @Header("Authorization") token: String
    ): List<AlertResponse>

    @DELETE("sharedpermissions/revoke/{permissionId}")
    suspend fun revokePermission(
        @Path("permissionId") permissionId: Int,
        @Header("Authorization") token: String
    ):   Response<Unit>

    @GET("sharedpermissions/{deviceId}/shared-users")
    suspend fun getSharedUsers(
        @Path("deviceId") deviceId: Int,
        @Header("Authorization") token: String
    ): List<SharedUser>

    @POST("sharedpermissions/{deviceId}/share")
    suspend fun shareDevice(
        @Path("deviceId") deviceId: Int,
        @Body sharedWithUserEmail: SharedUserRequest,
        @Header("Authorization") token: String
    ):   Response<Unit>

    @PUT("users/{userId}/change-password")
    suspend fun putChangePassword(
        @Path("userId") userId: Int,
        @Body changePasswordRequest: ChangePasswordRequest,
        @Header("Authorization") token: String
    ): ChangePasswordResponse


    @GET("houses")
    suspend fun getHouses(
        @Header("Authorization") token: String
    ): List<HousesListResponse>

        @PUT("houses/{houseId}")
    suspend fun updateHouse(
        @Path("houseId") houseId: Int,
        @Body body: UpdateHouseRequest,
        @Header("Authorization") token: String
    ): UpdateHouseResponse

    @POST("houses")
    suspend fun createHouse(
        @Body request: CreateHouseRequest,
        @Header("Authorization") token: String
    ): CreateHouseResponse

    @POST("groups")
    suspend fun createGroup(
        @Body request: CreateGroupRequest,
        @Header("Authorization") token: String
    ): CreateGroupResponse

    @PUT("groups/{groupId}")
    suspend fun updateGroup(
        @Path("groupId") groupId: Int,
        @Body body: UpdateGroupRequest,
        @Header("Authorization") token: String
    ): UpdateGroupResponse

    @GET("user-devices/me")
    suspend fun getUserActivities(
        @Header("Authorization") token: String
    ): List<UserActivityResponse>

    @GET("groups/my-groups")
    suspend fun getMyGroups(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Header("Authorization") token: String
    ): MyGroupsWrapper

    @GET("groups/{groupId}/members")
    suspend fun getGroupMembers(
        @Path("groupId") groupId: Int,
        @Header("Authorization") token: String
    ): ApiResponse<List<MemberResponse>>

    @POST("groups/members")
    suspend fun addGroupMember(
        @Body request: AddGroupMemberRequest,
        @Header("Authorization") token: String
    ): UserGroupResponse

    @GET("houses/group/{groupId}")
    suspend fun getHousesByGroupId(
        @Path("groupId") groupId: Int,
        @Header("Authorization") token: String
    ): List<HouseWithSpacesResponse>

    @GET("houses/group/{groupId}")
    suspend fun getHousesByGroup(
        @Path("groupId") groupId: Int,
        @Header("Authorization") token: String
    ): List<House>

    @PUT("groups/{groupId}/members/role")
    suspend fun updateGroupMemberRole(
        @Path("groupId") groupId: Int,
        @Body request: UpdateGroupMemberRoleRequest,
        @Header("Authorization") token: String
    ): UpdateGroupMemberRoleResponse

    @POST("notifications/otp")
    suspend fun sendOtp(@Body request: EmailRequest): EmailResponse

//    @POST("otp/verify")
//    suspend fun verifyOTPOld(
//        @Body request: EmailRequest
//    ): EmailResponse

    @POST("notifications/otp/verify")
    suspend fun verifyOTPNew(
        @Body request: EmailRequest
    ): EmailResponse

    @POST("auth/check-email")
    suspend fun checkEmail(
        @Body request: CheckEmailRequest
    ): CheckEmailResponse

    @POST("auth/recovery-password")
    suspend fun recoveryPassword(
        @Body request: RecoveryPasswordRequest
    ): ForgotPasswordResponse

    @GET("devices/account")
    suspend fun getUserOwnedDevices(
        @Header("Authorization") token: String
    ): List<OwnedDeviceResponse>

    @POST("devices/link")
    suspend fun linkDevice(
        @Body body: LinkDeviceRequest,
        @Header("Authorization") token: String
    ): LinkDeviceResponse

    @DELETE("groups/{groupId}")
    suspend fun deleteGroup(
        @Path("groupId") groupId: Int,
        @Header("Authorization") token: String
    ): Response<Unit>

    @DELETE("houses/{houseId}")
    suspend fun deleteHouse(
        @Path("houseId") houseId: Int,
        @Header("Authorization") token: String
    ): Response<Unit>

    @DELETE("spaces/{spaceId}")
    suspend fun deleteSpace(
        @Path("spaceId") spaceId: Int,
        @Header("Authorization") token: String
    ): Response<Unit>

    @POST("devices/{deviceId}/capabilities")
    suspend fun getDeviceCapabilities(
        @Path("deviceId") deviceId: String,
        @Body request: DeviceCapabilitiesRequest,
        @Header("Authorization") token: String
    ): DeviceCapabilitiesResponse

    @GET("devices/{deviceId}/state")
    suspend fun getDeviceState(
        @Path("deviceId") deviceId: String,
        @Query("serial_number") serialNumber: String,
        @Header("Authorization") token: String
    ): DeviceStateResponse

    @POST("devices/{deviceId}/state")
    suspend fun updateDeviceState(
        @Path("deviceId") deviceId: String,
        @Body request: UpdateDeviceStateRequest,
        @Header("Authorization") token: String
    ): UpdateDeviceStateResponse

    @POST("devices/{deviceId}/state/bulk")
    suspend fun updateDeviceStateBulk(
        @Path("deviceId") deviceId: String,
        @Body request: BulkDeviceStateUpdateRequest
    ): BulkDeviceStateUpdateResponse


//    @GET("statistics/daily-averages-sensor/{deviceId}/{startDate}/{endDate}")
//    suspend fun getDailyAveragesSensor(
//        @Path("deviceId") deviceId: Int,
//        @Path("startDate") startDate: String,
//        @Path("endDate") endDate: String,
//        @Header("Authorization") token: String
//    ): DailyAverageSensorResponse
//
//    @GET("statistics/daily-power-usages/{deviceId}/{startDate}/{endDate}")
//    suspend fun getDailyPowerUsages(
//        @Path("deviceId") deviceId: Int,
//        @Path("startDate") startDate: String,
//        @Path("endDate") endDate: String,
//        @Header("Authorization") token: String
//    ): DailyPowerUsageResponse
//
//    @POST("statistics/calculate-daily-average-sensor")
//    suspend fun calculateDailyAverageSensor(
//        @Body request: DailySensorRequest,
//        @Header("Authorization") token: String
//    ): AverageSensorResponse
//
//    @POST("statistics/calculate-weekly-average-sensor")
//    suspend fun calculateWeeklyAverageSensor(
//        @Body request: WeeklySensorRequest,
//        @Header("Authorization") token: String
//    ): AverageSensorResponse
//
//    @POST("statistics/calculate-average-sensor-for-range")
//    suspend fun calculateAverageSensorForRange(
//        @Body request: RangeSensorRequest,
//        @Header("Authorization") token: String
//    ): AverageSensorResponse
//
//    @GET("statistics/weekly-average-sensor/{deviceId}")
//    suspend fun getWeeklyAverageSensor(
//        @Path("deviceId") deviceId: Int,
//        @Header("Authorization") token: String
//    ): WeeklyAverageSensorResponse


    @GET("spaces/house/{houseId}")
    suspend fun getSpaces(
        @Path("houseId") houseId: Int,
        @Header("Authorization") token: String
    ): List<SpaceResponse>

    @GET("groups/role/{groupId}")
    suspend fun getRole(
        @Path("groupId") groupId: Int,
        @Header("Authorization") token: String
    ): RoleResponse
//
//    @PUT("spaces/{id}")
//    suspend fun updateSpace(
//        @Path("id") spaceId: Int,
//        @Body body: UpdateSpaceRequest,
//        @Header("Authorization") token: String
//    ): SpaceResponse3
//

    @POST("spaces")
    suspend fun createSpace(
        @Body body: CreateSpaceRequest,
        @Header("Authorization") token: String
    ): SpaceResponse
  
    @PUT("spaces/{id}")
    suspend fun updateSpace(
        @Path("id") spaceId: Int,
        @Body body: UpdateSpaceRequest,
        @Header("Authorization") token: String
    ): SpaceResponse

    @GET("tickets/user")
    suspend fun getUserTickets(
        @Header("Authorization") token: String
    ): TicketResponse

    @GET("tickets/detail/{ticketId}")
    suspend fun getTicketDetail(
        @Path("ticketId") ticketId: String,
        @Header("Authorization") token: String
    ): TicketDetailResponse

    @POST("tickets")
    suspend fun createTicket(
        @Body body: CreateTicketRequest,
        @Header("Authorization") token: String
    ): CreateTicketResponse

//    @POST("spaces")
//    suspend fun createSpace(
//        @Body body: CreateSpaceRequest,
//        @Header("Authorization") token: String
//    ): CreateSpaceResponse

//    @GET("statistics/daily-room-power-usage/{spaceId}/{startDate}/{endDate}")
//    suspend fun getDailyRoomPowerUsage(
//        @Path("spaceId") spaceId: Int,
//        @Path("startDate") startDate: String,
//        @Path("endDate") endDate: String,
//        @Header("Authorization") token: String
//    ): DailyPowerUsageResponse2
//
//    @GET("statistics/daily-room-averages-sensor/{spaceId}/{startDate}/{endDate}")
//    suspend fun getDailyRoomAveragesSensor(
//        @Path("spaceId") spaceId: Int,
//        @Path("startDate") startDate: String,
//        @Path("endDate") endDate: String,
//        @Header("Authorization") token: String
//    ): DailySensorAveragesResponse
//
//    @POST("statistics/calculate-daily-average-sensor")
//    suspend fun calculateDailyAverageSensor(
//        @Body body: DailyAverageSensorRequest,
//        @Header("Authorization") token: String
//    ): DailyAverageSensorResponse2
//
//    @POST("statistics/calculate-daily-power-usage")
//    suspend fun calculateDailyPowerUsage(
//        @Body body: DailyPowerUsageRequest,
//        @Header("Authorization") token: String
//    ): DailyPowerUsageResponse3



//    @GET("spaces/{spaceId}/detail")
//    suspend fun getSpaceDetail(
//        @Path("spaceId") spaceId: Int,
//        @Header("Authorization") token: String
//    ): SpaceDetailResponse
//
//    data class UpdateSpaceRequest(val Name: String)
}
