package com.sns.homeconnect_v2

import android.app.Application
import com.sns.homeconnect_v2.core.notification.NotificationChannelUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IoTHomeConnectApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationChannelUtil.createWarningChannel(this)
    }
}