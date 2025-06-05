package com.sns.homeconnect_v2.data.repository

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.response.UserActivityResponse
import com.sns.homeconnect_v2.domain.repository.UserActivityRepository
import jakarta.inject.Inject

class UserActivityRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager
) : UserActivityRepository {
    override suspend fun getUserActivities(): List<UserActivityResponse> {
        val token = authManager.getJwtToken()
        return apiService.getUserActivities(token = "Bearer $token")
    }
}