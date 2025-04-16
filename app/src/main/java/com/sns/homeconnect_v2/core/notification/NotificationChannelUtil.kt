package com.sns.homeconnect_v2.core.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import androidx.core.net.toUri

object NotificationChannelUtil {
    @SuppressLint("ObsoleteSdkInt")
    fun createWarningChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundUri = "android.resource://${context.packageName}/raw/alert".toUri()
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            val channel = NotificationChannel(
                "homeconnect_warning",
                "Cảnh báo",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Kênh thông báo cảnh báo nguy hiểm/khẩn cấp"
                setSound(soundUri, audioAttributes)
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
                lockscreenVisibility = 1
                setBypassDnd(true)
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}