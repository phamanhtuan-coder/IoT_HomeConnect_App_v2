package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.response.Notification

interface NotificationRepository {
    suspend fun getUserNotifications(): List<Notification>
}