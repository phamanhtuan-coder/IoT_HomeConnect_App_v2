package com.sns.homeconnect_v2.data.repository

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.response.Alert
import com.sns.homeconnect_v2.data.remote.dto.response.AlertDetail
import com.sns.homeconnect_v2.data.remote.dto.response.AlertResponse
import com.sns.homeconnect_v2.domain.repository.AlertRepository
import javax.inject.Inject

class AlertRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager
) :AlertRepository {

   override suspend fun getAllByUser(): List<AlertResponse> {
       val token = authManager.getJwtToken()
        // Gọi API
        return apiService.getAllNotification(token = "Bearer $token")
    }

    override suspend fun getAlertById(alertId: Int): AlertDetail {
        val token = authManager.getJwtToken()
        // Gọi API
        return apiService.getAlertById(alertId = alertId, token = "Bearer $token")
    }

    override suspend fun readNotification(alertId: Int): Alert {
        val token = authManager.getJwtToken()
        // Gọi API
        return apiService.readNotification(alertId = alertId, token = "Bearer $token")
    }

    override suspend fun searchNotification(search: String): List<AlertResponse> {
        val token = authManager.getJwtToken()
        // Gọi API
        return apiService.searchAlerts(search, token = "Bearer $token")
    }

}