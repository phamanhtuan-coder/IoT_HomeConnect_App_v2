package com.sns.homeconnect_v2.presentation.screen.permission

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.sns.homeconnect_v2.core.notification.NotificationChannelUtil
import com.sns.homeconnect_v2.core.permission.PermissionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val permissionManager: PermissionManager
) : ViewModel() {

    fun initializePermissions(activity: Activity) {
        val mediaPerms = permissionManager.getMediaPermissions()
        val locationPerms = permissionManager.getLocationWifiPermissions()

        // TODO: Dùng launcher từ Composable để trigger request permissions
        // Gợi ý: truyền launcher từ Compose xuống hoặc gọi trong onResume()
    }

    fun setupNotificationChannel(context: Context) {
        NotificationChannelUtil.createWarningChannel(context)
    }

    fun openAppSettings() {
        permissionManager.openAppSettings()
    }
}