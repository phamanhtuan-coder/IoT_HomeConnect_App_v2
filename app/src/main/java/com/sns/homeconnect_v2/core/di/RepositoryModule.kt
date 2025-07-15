package com.sns.homeconnect_v2.core.di

import android.content.Context
import com.sns.homeconnect_v2.PermissionEventHandler
import com.sns.homeconnect_v2.core.permission.PermissionManager
import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.repository.AlertRepositoryImpl
import com.sns.homeconnect_v2.data.repository.AuthRepositoryImpl
import com.sns.homeconnect_v2.data.repository.DeviceRepositoryImpl
import com.sns.homeconnect_v2.data.repository.EcomRepositoryImpl
import com.sns.homeconnect_v2.data.repository.GroupRepositoryImpl
import com.sns.homeconnect_v2.data.repository.HouseRepositoryImpl
import com.sns.homeconnect_v2.data.repository.NotificationImpl
import com.sns.homeconnect_v2.data.repository.OTPRepositoryImpl
import com.sns.homeconnect_v2.data.repository.SharedRepositoryImpl
import com.sns.homeconnect_v2.data.repository.SocketRepositoryImpl
import com.sns.homeconnect_v2.data.repository.SpaceRepositoryImpl
import com.sns.homeconnect_v2.data.repository.TicketRepositoryImpl
import com.sns.homeconnect_v2.data.repository.UserActivityRepositoryImpl
import com.sns.homeconnect_v2.data.repository.UserRepositoryImpl
import com.sns.homeconnect_v2.data.repository.WeatherRepositoryImpl
import com.sns.homeconnect_v2.domain.repository.AlertRepository
import com.sns.homeconnect_v2.domain.repository.AuthRepository
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import com.sns.homeconnect_v2.domain.repository.EcomRepository
import com.sns.homeconnect_v2.domain.repository.GroupRepository
import com.sns.homeconnect_v2.domain.repository.HouseRepository
import com.sns.homeconnect_v2.domain.repository.NotificationRepository
import com.sns.homeconnect_v2.domain.repository.OTPRepository
import com.sns.homeconnect_v2.domain.repository.SharedRepository
import com.sns.homeconnect_v2.domain.repository.SocketRepository
import com.sns.homeconnect_v2.domain.repository.SpaceRepository
import com.sns.homeconnect_v2.domain.repository.TicketRepository
import com.sns.homeconnect_v2.domain.repository.UserActivityRepository
import com.sns.homeconnect_v2.domain.repository.UserRepository
import com.sns.homeconnect_v2.domain.repository.WeatherRepository
import com.sns.homeconnect_v2.domain.usecase.SendFcmTokenUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.CheckEmailUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.ForgotPasswordUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.LoginUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.LogOutUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.RegisterUseCase
import com.sns.homeconnect_v2.domain.usecase.group.CreateGroupUseCase
import com.sns.homeconnect_v2.domain.usecase.group.DeleteGroupUseCase
import com.sns.homeconnect_v2.domain.usecase.group.GetGroupMembersUseCase
import com.sns.homeconnect_v2.domain.usecase.group.GetListHouseByGroupUseCase
import com.sns.homeconnect_v2.domain.usecase.home.FetchSharedWithUseCase
import com.sns.homeconnect_v2.domain.usecase.home.GetListHouseUseCase
import com.sns.homeconnect_v2.domain.usecase.house.CreateHouseUseCase
import com.sns.homeconnect_v2.domain.usecase.house.DeleteHouseUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.AttributeDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetInfoDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.LinkDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.ToggleDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.UnlinkDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.sharing.AddSharedUserUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.sharing.GetSharedUsersUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.sharing.RevokePermissionUseCase
import com.sns.homeconnect_v2.domain.usecase.notification.GetAlertByIdUseCase
import com.sns.homeconnect_v2.domain.usecase.notification.GetAllByUserUseCase
import com.sns.homeconnect_v2.domain.usecase.notification.ReadNotificationUseCase
import com.sns.homeconnect_v2.domain.usecase.notification.SearchNotificationUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.ConfirmEmailUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.SendOtpUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.VerifyOtpUseCase
import com.sns.homeconnect_v2.domain.usecase.profile.GetInfoProfileUseCase
import com.sns.homeconnect_v2.domain.usecase.profile.PutInfoProfileUseCase
import com.sns.homeconnect_v2.domain.usecase.profile.UpdatePasswordUseCase
import com.sns.homeconnect_v2.domain.usecase.space.DeleteSpaceUseCase
import com.sns.homeconnect_v2.domain.usecase.house.FetchHousesUseCase
import com.sns.homeconnect_v2.domain.usecase.house.GetHouseUseCase
import com.sns.homeconnect_v2.domain.usecase.house.UpdateHouseUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDeviceCapabilitiesUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDeviceDisplayInfoUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetDeviceStateUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.ListOfUserOwnedDevicesUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.UpdateDeviceStateUseCase
import com.sns.homeconnect_v2.domain.usecase.space.GetListSpaceUseCase
import com.sns.homeconnect_v2.domain.usecase.space.GetSpaceDetailUseCase
import com.sns.homeconnect_v2.domain.usecase.space.UpdateSpaceUseCase
import com.sns.homeconnect_v2.domain.usecase.ticket.GetDetailTicketUseCase
import com.sns.homeconnect_v2.domain.usecase.ticket.GetListTicketUseCase
import com.sns.homeconnect_v2.domain.usecase.user_activity.GetUserActivitiesUseCase
import com.sns.homeconnect_v2.domain.usecase.weather.GetCurrentWeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit // Thêm import nếu sử dụng Retrofit
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindOTPRepository(otpRepositoryImpl: OTPRepositoryImpl): OTPRepository

    @Binds
    @Singleton
    abstract fun bindDeviceRepository(deviceRepositoryImpl: DeviceRepositoryImpl): DeviceRepository

    @Binds
    @Singleton
    abstract fun bindSpaceRepository(spaceRepositoryImpl: SpaceRepositoryImpl): SpaceRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindHouseRepository(houseRepositoryImpl: HouseRepositoryImpl): HouseRepository

    @Binds
    @Singleton
    abstract fun bindAlertRepository(alertRepositoryImpl: AlertRepositoryImpl): AlertRepository

    @Binds
    @Singleton
    abstract fun bindSharedRepository(sharedRepositoryImpl: SharedRepositoryImpl): SharedRepository

    @Binds
    @Singleton
    abstract fun bindGroupRepository(groupRepositoryImpl: GroupRepositoryImpl): GroupRepository

    @Binds
    @Singleton
    abstract fun bindUserActivityRepository(impl: UserActivityRepositoryImpl): UserActivityRepository

    @Binds
    @Singleton
    abstract fun bindEcomRepository(impl: EcomRepositoryImpl): EcomRepository

    @Binds
    @Singleton
    abstract fun bindTicketRepository(ticketRepositoryImpl: TicketRepositoryImpl): TicketRepository

    companion object {
        @Provides
        @Singleton
        fun provideAuthManager(@ApplicationContext context: Context): AuthManager {
            return AuthManager(context)
        }


        @Provides
        @Singleton
        fun providePermissionManager(@ApplicationContext context: Context): PermissionManager {
            return PermissionManager(context)
        }

        @Provides
        @Singleton
        fun providePermissionEventHandler(): PermissionEventHandler {
            return PermissionEventHandler()
        }

        @Provides
        @Singleton
        fun provideLoginUseCase(
            authRepository: AuthRepository,
            authManager: AuthManager
        ): LoginUseCase {
            return LoginUseCase(authRepository, authManager)
        }

        @Provides
        @Singleton
        fun provideLogOutUseCase(repository: AuthRepository): LogOutUseCase {
            return LogOutUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase {
            return RegisterUseCase(authRepository)
        }

        @Provides
        @Singleton
        fun provideCheckEmailUseCase(repository: AuthRepository): CheckEmailUseCase {
            return CheckEmailUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideForgotPasswordUseCase(authRepository: AuthRepository): ForgotPasswordUseCase {
            return ForgotPasswordUseCase(authRepository)
        }

        @Provides
        @Singleton
        fun provideSendFcmTokenUseCase(repository: UserRepository): SendFcmTokenUseCase {
            return SendFcmTokenUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideSendOtpUseCase(repository: OTPRepository): SendOtpUseCase {
            return SendOtpUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideVerifyOtpUseCase(repository: OTPRepository): VerifyOtpUseCase {
            return VerifyOtpUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideConfirmEmailUseCase(repository: UserRepository): ConfirmEmailUseCase {
            return ConfirmEmailUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideGetInfoProfileUseCase(repository: AuthRepository): GetInfoProfileUseCase {
            return GetInfoProfileUseCase(repository)
        }

        @Provides
        @Singleton
        fun providePutInfoProfileUseCase(repository: UserRepository): PutInfoProfileUseCase {
            return PutInfoProfileUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideUpdatePasswordUseCase(repository: UserRepository): UpdatePasswordUseCase {
            return UpdatePasswordUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideFetchSharedWithUseCase(repository: UserRepository): FetchSharedWithUseCase {
            return FetchSharedWithUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideAttributeDeviceUseCase(repository: DeviceRepository): AttributeDeviceUseCase {
            return AttributeDeviceUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideGetInfoDeviceUseCase(repository: DeviceRepository): GetInfoDeviceUseCase {
            return GetInfoDeviceUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideUnlinkDeviceUseCase(repository: DeviceRepository): UnlinkDeviceUseCase {
            return UnlinkDeviceUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideLinkDeviceUseCase(repository: DeviceRepository): LinkDeviceUseCase {
            return LinkDeviceUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideToggleDeviceUseCase(repository: DeviceRepository): ToggleDeviceUseCase {
            return ToggleDeviceUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideGetCurrentWeatherUseCase(weatherRepository: WeatherRepository): GetCurrentWeatherUseCase {
            return GetCurrentWeatherUseCase(weatherRepository)
        }

        @Provides
        @Singleton
        fun provideGetListHouseUseCase(houseRepository: HouseRepository): GetListHouseUseCase {
            return GetListHouseUseCase(houseRepository)
        }

        @Provides
        @Singleton
        fun provideGetHouseUseCase(houseRepository: HouseRepository): GetHouseUseCase {
            return GetHouseUseCase(houseRepository)
        }

        @Provides
        @Singleton
        fun provideGetListSpaceUseCase(spaceRepository: SpaceRepository): GetListSpaceUseCase {
            return GetListSpaceUseCase(spaceRepository)
        }

        @Provides
        @Singleton
        fun provideUpdateSpaceUseCase(spaceRepository: SpaceRepository): UpdateSpaceUseCase {
            return UpdateSpaceUseCase(spaceRepository)
        }

        @Provides
        @Singleton
        fun provideGetDeviceBySpaceUseCase(spaceRepository: SpaceRepository): GetSpaceDetailUseCase {
            return GetSpaceDetailUseCase(spaceRepository)
        }

        @Provides
        @Singleton
        fun provideFetchHousesUseCase(houseRepository: HouseRepository): FetchHousesUseCase {
            return FetchHousesUseCase(houseRepository)
        }

        @Provides
        @Singleton
        fun provideCreateHouseUseCase(houseRepository: HouseRepository): CreateHouseUseCase {
            return CreateHouseUseCase(houseRepository)
        }

        @Provides
        @Singleton
        fun provideUpdateHouseUseCase(houseRepository: HouseRepository): UpdateHouseUseCase {
            return UpdateHouseUseCase(houseRepository)
        }

        @Provides
        @Singleton
        fun provideDeleteHouseUseCase(houseRepository: HouseRepository): DeleteHouseUseCase {
            return DeleteHouseUseCase(houseRepository)
        }

        @Provides
        @Singleton
        fun provideGetAlertByIdUseCase(alertRepository: NotificationRepository): GetAlertByIdUseCase {
            return GetAlertByIdUseCase(alertRepository)
        }

        @Provides
        @Singleton
        fun provideGetAllByUserUseCase(alertRepository: AlertRepository): GetAllByUserUseCase {
            return GetAllByUserUseCase(alertRepository)
        }

        @Provides
        @Singleton
        fun provideReadNotificationUseCase(alertRepository: AlertRepository): ReadNotificationUseCase {
            return ReadNotificationUseCase(alertRepository)
        }

        @Provides
        @Singleton
        fun provideSearchNotificationUseCase(alertRepository: AlertRepository): SearchNotificationUseCase {
            return SearchNotificationUseCase(alertRepository)
        }

        @Provides
        @Singleton
        fun provideAddSharedUserUseCase(sharedRepository: SharedRepository): AddSharedUserUseCase {
            return AddSharedUserUseCase(sharedRepository)
        }

        @Provides
        @Singleton
        fun provideGetSharedUsersUseCase(sharedRepository: SharedRepository): GetSharedUsersUseCase {
            return GetSharedUsersUseCase(sharedRepository)
        }

        @Provides
        @Singleton
        fun provideRevokePermissionUseCase(sharedRepository: SharedRepository): RevokePermissionUseCase {
            return RevokePermissionUseCase(sharedRepository)
        }

        @Provides
        @Singleton
        fun provideCreateGroupUseCase(groupRepository: GroupRepository): CreateGroupUseCase {
            return CreateGroupUseCase(groupRepository)
        }

        @Provides
        @Singleton
        fun provideDeleteGroupUseCase(groupRepository: GroupRepository): DeleteGroupUseCase {
            return DeleteGroupUseCase(groupRepository)
        }

        @Provides
        @Singleton
        fun provideGetListHouseByGroupUseCase(groupRepository: GroupRepository): GetListHouseByGroupUseCase {
            return GetListHouseByGroupUseCase(groupRepository)
        }

        @Provides
        @Singleton
        fun provideGetGroupMembersUseCase(groupRepository: GroupRepository): GetGroupMembersUseCase {
            return GetGroupMembersUseCase(groupRepository)
        }

        @Provides
        @Singleton
        fun provideGetUserActivitiesUseCase(repository: UserActivityRepository): GetUserActivitiesUseCase {
            return GetUserActivitiesUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideDeleteSpaceUseCase(spaceRepository: SpaceRepository): DeleteSpaceUseCase {
            return DeleteSpaceUseCase(spaceRepository)
        }

        @Provides
        @Singleton
        fun provideTicketUseCase(ticketRepository: TicketRepository): GetListTicketUseCase {
            return GetListTicketUseCase(ticketRepository)
        }

        @Provides
        @Singleton
        fun provideDetailTicketUseCase(ticketRepository: TicketRepository): GetDetailTicketUseCase {
            return GetDetailTicketUseCase(ticketRepository)
        }

        @Provides
        @Singleton
        fun provideListOfUserOwnedDevicesUseCase(deviceRepository: DeviceRepository): ListOfUserOwnedDevicesUseCase {
            return ListOfUserOwnedDevicesUseCase(deviceRepository)
        }

        @Provides
        @Singleton
        fun provideGetDeviceDisplayInfoUseCase(
            ecomRepository: EcomRepository
        ): GetDeviceDisplayInfoUseCase {
            return GetDeviceDisplayInfoUseCase(ecomRepository)
        }

        @Provides
        @Singleton
        fun provideGetDeviceCapabilitiesUseCase(deviceRepository: DeviceRepository): GetDeviceCapabilitiesUseCase {
            return GetDeviceCapabilitiesUseCase(deviceRepository)
        }

        @Provides
        @Singleton
        fun provideGetDeviceStateUseCase(
            deviceRepository: DeviceRepository
        ): GetDeviceStateUseCase {
            return GetDeviceStateUseCase(deviceRepository)
        }

        @Provides
        @Singleton
        fun provideUpdateDeviceStateUseCase(
            repository: DeviceRepository
        ): UpdateDeviceStateUseCase {
            return UpdateDeviceStateUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideSocketRepository(): SocketRepository {
            return SocketRepositoryImpl()
        }
    }
}