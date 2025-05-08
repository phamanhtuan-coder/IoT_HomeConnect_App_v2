package com.sns.homeconnect_v2.core.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.sns.homeconnect_v2.core.permission.PermissionManager
import com.sns.homeconnect_v2.data.repository.WifiRepositoryImpl
import com.sns.homeconnect_v2.domain.repository.WifiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WifiModule {

    @RequiresApi(Build.VERSION_CODES.R)
    @Provides
    @Singleton
    fun provideWifiRepository(
        @ApplicationContext context: Context,
        permissionManager: PermissionManager
    ): WifiRepository {
        return WifiRepositoryImpl(context, permissionManager)
    }
}