package com.sns.homeconnect_v2.service.notification

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
        Log.d("MyFirebaseMessaging", "🔥 Token mới: $token")
        super.onNewToken(token)
        getSharedPreferences("app_prefs", MODE_PRIVATE).edit {
            putString("FCM_TOKEN", token)
        }
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            sendFcmTokenUseCase(token).onSuccess {
                Log.i(TAG, "✅ Token sent to server: ${it.message}")
            }.onFailure {
                Log.e(TAG, "❌ Failed to send token: ${it.message}")
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Ưu tiên xử lý kiểu data message
        val type = remoteMessage.data["type"]
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]

        if (type == "alarm") {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            ) {
                showAlarmNotification(title, body)
            } else {
                Log.w(TAG, "⚠️ Chưa được cấp quyền POST_NOTIFICATIONS.")
            }
        } else {
            Log.d(TAG, "📩 Nhận được tin nhắn khác, không phải 'alarm': type=$type")
        }
    }

    private fun showAlarmNotification(title: String?, body: String?) {
        NotificationChannelUtil.createWarningChannel(this)

        val soundUri = "android.resource://$packageName/raw/alert".toUri()
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.alert)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(this, CHANNEL_WARNING)
            .setContentTitle(title ?: "Thông báo từ thiết bị")
            .setContentText(body ?: "Thiết bị của bạn vừa phát hiện cảnh báo.")
            .setSmallIcon(R.drawable.alert)
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        private const val TAG = "MyFirebaseMessaging"
        private const val CHANNEL_WARNING = "homeconnect_warning"
    }
}
