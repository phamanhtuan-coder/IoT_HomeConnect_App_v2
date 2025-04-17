package com.sns.homeconnect_v2.core.di

import android.content.Context
import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.repository.AuthRepositoryImpl
import com.sns.homeconnect_v2.data.repository.OTPRepositoryImpl
import com.sns.homeconnect_v2.data.repository.UserRepositoryImpl
import com.sns.homeconnect_v2.domain.repository.AuthRepository
import com.sns.homeconnect_v2.domain.repository.OTPRepository
import com.sns.homeconnect_v2.domain.repository.UserRepository
import com.sns.homeconnect_v2.domain.usecase.SendFcmTokenUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.LoginUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.RegisterUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.CheckEmailUseCase
import com.sns.homeconnect_v2.domain.usecase.auth.NewPasswordUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.ConfirmEmailUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.SendOtpUseCase
import com.sns.homeconnect_v2.domain.usecase.otp.VerifyOtpUseCase
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
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindOTPRepository(
        otpRepositoryImpl: OTPRepositoryImpl
    ): OTPRepository

    companion object {
        @Provides
        @Singleton
        fun provideAuthManager(@ApplicationContext context: Context): AuthManager {
            return AuthManager(context)
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
        fun provideRegisterUseCase(
            authRepository: AuthRepository
        ): RegisterUseCase {
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

    }
}