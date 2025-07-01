package com.sns.homeconnect_v2.data.repository

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.response.Notification
import com.sns.homeconnect_v2.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationImpl @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager
) : NotificationRepository {

    override suspend fun getUserNotifications(): List<Notification> {
        val token = authManager.getJwtToken()
        return apiService.getUserNotifications("Bearer $token")
    }
}