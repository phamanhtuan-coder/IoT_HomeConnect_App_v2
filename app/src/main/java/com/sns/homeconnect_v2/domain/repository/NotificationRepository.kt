package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.response.Notification
import com.sns.homeconnect_v2.data.remote.dto.response.check

interface NotificationRepository {
    suspend fun getUserNotifications(): List<Notification>
    suspend fun getUserNotificationById(id: Int): Notification
    suspend fun updateNotificationStatus(id: Int,is_read: check)
}