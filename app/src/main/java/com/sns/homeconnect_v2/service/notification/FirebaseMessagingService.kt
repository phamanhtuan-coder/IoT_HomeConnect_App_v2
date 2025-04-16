package com.sns.homeconnect_v2.service.notification

import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sns.homeconnect_v2.R
import com.sns.homeconnect_v2.domain.usecase.SendFcmTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.Manifest.permission
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.net.toUri
import javax.inject.Inject
import androidx.core.content.edit
import com.sns.homeconnect_v2.core.notification.NotificationChannelUtil

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var sendFcmTokenUseCase: SendFcmTokenUseCase

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        getSharedPreferences("app_prefs", MODE_PRIVATE)
            .edit { putString("FCM_TOKEN", token) }
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            sendFcmTokenUseCase(token).onSuccess {
                Log.i("MyFirebaseMessagingService", it)
            }.onFailure {
                Log.e("MyFirebaseMessagingService", "sendTokenToServer error: ${it.message}")
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        handleNotification(remoteMessage)
    }

    private fun handleNotification(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                checkSelfPermission(
                    this,
                    permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showAlert(it.title, it.body)
            } else {
                Log.w("MyFirebaseMessaging", "⚠️ Chưa cấp quyền gửi thông báo.")
            }
        }
    }

    private fun showAlert(title: String?, body: String?) {
        NotificationChannelUtil.createWarningChannel(this)
        val soundUri = "android.resource://$packageName/raw/alert".toUri()
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.alert)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, "homeconnect_warning")
            .setContentTitle(title ?: "Tiêu đề thông báo")
            .setContentText(body ?: "Nội dung thông báo")
            .setSmallIcon(R.drawable.alert)
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setSound(soundUri)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}