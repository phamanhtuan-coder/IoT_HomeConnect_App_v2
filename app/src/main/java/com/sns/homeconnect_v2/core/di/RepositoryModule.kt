package com.sns.homeconnect_v2.core.di

import android.content.Context
import com.sns.homeconnect_v2.core.permission.PermissionManager
import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.repository.AuthRepositoryImpl
import com.sns.homeconnect_v2.data.repository.DeviceRepositoryImpl
import com.sns.homeconnect_v2.data.repository.HouseRepositoryImpl
import com.sns.homeconnect_v2.data.repository.OTPRepositoryImpl
import com.sns.homeconnect_v2.data.repository.SpaceRepositoryImpl
import com.sns.homeconnect_v2.data.repository.UserRepositoryImpl
import com.sns.homeconnect_v2.data.repository.WeatherRepositoryImpl
import com.sns.homeconnect_v2.domain.repository.AuthRepository
import com.sns.homeconnect_v2.domain.repository.DeviceRepository
import com.sns.homeconnect_v2.domain.repository.HouseRepository
import com.sns.homeconnect_v2.domain.repository.OTPRepository
import com.sns.homeconnect_v2.domain.repository.SpaceRepository
import com.sns.homeconnect_v2.domain.repository.UserRepository
import com.sns.homeconnect_v2.domain.repository.WeatherRepository
import com.sns.homeconnect_v2.domain.usecase.SendFcmTokenUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.LoginUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.RegisterUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.CheckEmailUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.LogOutUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.NewPasswordUseCase
import com.sns.homeconnect_v2.domain.usecase.home.FetchSharedWithUseCase
import com.sns.homeconnect_v2.domain.usecase.home.GetListHouseUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.AttributeDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetInfoDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.ConfirmEmailUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.SendOtpUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.VerifyOtpUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.ToggleDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.UnlinkDeviceUseCase
import com.sns.homeconnect_v2.domain.usecase.profile.GetInfoProfileUseCase
import com.sns.homeconnect_v2.domain.usecase.profile.PutInfoProfileUseCase
import com.sns.homeconnect_v2.domain.usecase.weather.GetCurrentWeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

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
        fun provideCheckEmailUseCase(repository: OTPRepository): CheckEmailUseCase {
            return CheckEmailUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideNewPasswordUseCase(repository: AuthRepository): NewPasswordUseCase {
            return NewPasswordUseCase(repository)
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
        fun provideGetInfoProfileUseCase (repository: AuthRepository): GetInfoProfileUseCase {
            return GetInfoProfileUseCase (repository)
        }

        @Provides
        @Singleton
        fun providePutInfoProfileUseCase (repository: UserRepository): PutInfoProfileUseCase {
            return PutInfoProfileUseCase (repository)
        }

        @Provides
        @Singleton
        fun provideFetchSharedWithUseCase (repository: UserRepository): FetchSharedWithUseCase {
            return FetchSharedWithUseCase (repository)
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

    }
}