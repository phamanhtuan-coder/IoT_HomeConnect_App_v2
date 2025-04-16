package com.sns.homeconnect_v2.core.di

import android.content.Context
import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.repository.OTPRepositoryImpl
import com.sns.homeconnect_v2.data.repository.UserRepositoryImpl
import com.sns.homeconnect_v2.domain.repository.OTPRepository
import com.sns.homeconnect_v2.domain.repository.UserRepository
import com.sns.homeconnect_v2.domain.usecase.SendFcmTokenUseCase
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
        fun provideSendFcmTokenUseCase(repository: UserRepository): SendFcmTokenUseCase {
            return SendFcmTokenUseCase(repository)
        }
    }
}